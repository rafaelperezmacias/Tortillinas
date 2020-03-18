package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.BottomSheets.ClientesBottomSheet;
import com.zamnadev.tortillinas.Dialogs.MessageDialog;
import com.zamnadev.tortillinas.Dialogs.MessageDialogBuilder;
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.ProductoModificado;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorClientes extends RecyclerView.Adapter<AdaptadorClientes.ViewHolder> {
    private Context context;

    private ArrayList<Cliente> clientes;

    private ArrayList<Boolean> showProductos;

    private FragmentManager fragmentManager;

    public AdaptadorClientes(Context context, ArrayList<Cliente> clientes,
                             FragmentManager fragmentManager) {
        this.context = context;
        this.clientes = clientes;
        this.fragmentManager = fragmentManager;
        showProductos = new ArrayList<>();
        for (Cliente c : clientes) {
            showProductos.add(false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_adaptador_clientes,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.txtNombre.setText(cliente.getNombre().getNombres() + " " +
                cliente.getNombre().getApellidos());
        holder.txtTelefono.setText(cliente.getTelefono());
        holder.txtDireccion.setText(cliente.getDireccion().toRecyclerView());
        PopupMenu popupMenu = new PopupMenu(context,holder.btnOpciones);
        if (cliente.isPreferencial()) {
            popupMenu.inflate(R.menu.menu_c_productos_recyclerview);
        } else {
            popupMenu.inflate(R.menu.menu_clientes_recyclerview);
        }
        if (showProductos.get(position) && cliente.isPreferencial()) {
            popupMenu.getMenu().getItem(0).setTitle("Ocultar precios preferenciales");
        } else if (cliente.isPreferencial()) {
            popupMenu.getMenu().getItem(0).setTitle("Mostrar precios preferenciales");
        }
        holder.btnOpciones.setOnClickListener(view -> {
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.menuProductos: {
                        if (showProductos.get(position)) {
                            showProductos.set(position, false);
                        } else {
                            showProductos.set(position, true);
                        }
                        notifyDataSetChanged();
                        return false;
                    }
                    case R.id.menuEditar: {
                        ClientesBottomSheet bottomSheet = new ClientesBottomSheet(cliente);
                        bottomSheet.show(fragmentManager, bottomSheet.getTag());
                        return false;
                    }
                    case R.id.menuEliminar: {
                        MessageDialog dialog = new MessageDialog(context, new MessageDialogBuilder()
                                .setTitle("Alerta")
                                .setMessage("¿Estás seguro de que quieres eliminar a este cliente?")
                                .setPositiveButtonText("Sí, Eliminar")
                                .setNegativeButtonText("No, cancelar")
                        );
                        dialog.show();
                        dialog.setPositiveButtonListener(v -> {
                            DatabaseReference refEmpleado = FirebaseDatabase.getInstance()
                                    .getReference("Clientes")
                                    .child(cliente.getIdCliente())
                                    .child("eliminado");
                            refEmpleado.setValue(true).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Cliente eliminado con exito",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Error, intentelo mas tarde",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        });
                        dialog.setNegativeButtonListener(v -> dialog.dismiss());
                        return false;
                    }
                }
                return true;
            });
            popupMenu.show();
        });

        if (cliente.isPreferencial()) {
            if (showProductos.get(position)) {
                holder.lytPreferenciales.setVisibility(View.VISIBLE);
                holder.lytPreferenciales.setVisibility(View.VISIBLE);
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                holder.recyclerView.setHasFixedSize(true);
                ArrayList<Producto> productos = new ArrayList<>();
                DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReference("Productos");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        productos.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Producto producto = snapshot.getValue(Producto.class);
                            if (!producto.isEliminado()) {
                                for (int x = 0; x < cliente.getPrecios().size(); x++) {
                                    ProductoModificado p =
                                            new ProductoModificado(cliente.getPrecios().get("p" + x));
                                    if (p.getIdProducto().equals(producto.getIdProducto())) {
                                        producto.setPrecio(p.getPrecio());
                                        productos.add(producto);
                                    }
                                }
                            }
                        }
                        AdaptadorProductos adaptadorProductos =
                                new AdaptadorProductos(context,productos,false, fragmentManager);
                        holder.recyclerView.setAdapter(adaptadorProductos);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            } else {
                holder.lytPreferenciales.setVisibility(View.GONE);
            }
        } else {
            holder.lytPreferenciales.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return clientes.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNombre;
        private TextView txtTelefono;
        private TextView txtDireccion;

        private LinearLayout lytPreferenciales;

        private RecyclerView recyclerView;

        private ImageButton btnOpciones;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTelefono = itemView.findViewById(R.id.txtTelefono);
            txtDireccion = itemView.findViewById(R.id.txtDireccion);
            lytPreferenciales = itemView.findViewById(R.id.lytPreferenciales);
            recyclerView = itemView.findViewById(R.id.recyclerview);
            btnOpciones = itemView.findViewById(R.id.btnOpciones);
        }
    }
}