package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zamnadev.tortillinas.BottomSheets.ProductosBottomSheet;
import com.zamnadev.tortillinas.Dialogs.MessageDialog;
import com.zamnadev.tortillinas.Dialogs.MessageDialogBuilder;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ViewHolder> {
    private Context context;

    private ArrayList<Producto> productos;

    private boolean isEditable;

    private FragmentManager fragmentManager;

    public AdaptadorProductos(Context context, ArrayList<Producto> productos, boolean isEditable,
                              FragmentManager fragmentManager) {
        this.context = context;
        this.productos = productos;
        this.isEditable = isEditable;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isEditable) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_adaptador_productos, parent,false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_adaptador_productos_preferencial, parent,false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.txtNombre.setText(producto.getNombre());
        holder.txtPrecio.setText("$ " + producto.getPrecio());
        PopupMenu popupMenu = new PopupMenu(context, holder.btnOpciones);
        popupMenu.inflate(R.menu.menu_productos);
        if (producto.isFormulario()) {
            holder.txtFormulario.setText("Agregado al formulario");
            popupMenu.getMenu().getItem(0).setTitle("Eliminar del formulario");
        } else {
            holder.txtFormulario.setText("Eliminado del formulario");
            popupMenu.getMenu().getItem(0).setTitle("Agregar al formulario");
        }
        holder.btnOpciones.setOnClickListener(view -> {
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.menuMostrar: {
                        if (producto.isFormulario()) {
                            FirebaseDatabase.getInstance().getReference("Productos")
                                    .child(producto.getIdProducto())
                                    .child("formulario")
                                    .setValue(false);
                        } else {
                            FirebaseDatabase.getInstance().getReference("Productos")
                                    .child(producto.getIdProducto())
                                    .child("formulario")
                                    .setValue(true);
                        }
                        return true;
                    }
                    case R.id.menuEditar: {
                        ProductosBottomSheet bottomSheet = new ProductosBottomSheet(producto);
                        bottomSheet.show(fragmentManager, bottomSheet.getTag());
                        return true;
                    }
                    case R.id.menuEliminar: {
                        MessageDialog dialog = new MessageDialog(context, new MessageDialogBuilder()
                                .setTitle("Alerta")
                                .setMessage("¿Estás seguro de que quieres eliminar este producto?")
                                .setPositiveButtonText("Sí, Eliminar")
                                .setNegativeButtonText("No, cancelar")
                        );
                        dialog.show();
                        dialog.setPositiveButtonListener(v -> {
                            DatabaseReference refEmpleado = FirebaseDatabase.getInstance()
                                    .getReference("Productos")
                                    .child(producto.getIdProducto())
                                    .child("eliminado");
                            refEmpleado.setValue(true).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Producto eliminado con exito", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        });
                        dialog.setNegativeButtonListener(v -> dialog.dismiss());
                        return true;
                    }
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() { return productos.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private TextView txtPrecio;
        private TextView txtFormulario;

        private ImageButton btnOpciones;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            btnOpciones = itemView.findViewById(R.id.btnOpciones);
            txtFormulario = itemView.findViewById(R.id.txtFormulario);
        }
    }
}