package com.zamnadev.tortillinas.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ViewHolder> {

    private Context context;
    private ArrayList<Producto> productos;
    private boolean isEditable;
    private FragmentManager fragmentManager;

    public AdaptadorProductos(Context context, ArrayList<Producto> productos, boolean isEditable, FragmentManager fragmentManager)
    {
        this.context = context;
        this.productos = productos;
        this.isEditable = isEditable;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isEditable) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_productos,parent,false);
            return new AdaptadorProductos.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_productos_preferencial,parent,false);
            return new AdaptadorProductos.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.txtNombre.setText(producto.getNombre());
        holder.txtPrecio.setText("$ " + producto.getPrecio());

        holder.btnOpciones.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context,holder.btnOpciones);
            popupMenu.inflate(R.menu.menu_productos);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId())
                {
                    case R.id.menuEditar:
                        ProductosBottomSheet bottomSheet = new ProductosBottomSheet(producto);
                        bottomSheet.show(fragmentManager,bottomSheet.getTag());
                        return true;
                    case R.id.menuEliminar:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Alerta")
                                .setMessage("¿Esta seguro que desea elimanar este producto?")
                                .setPositiveButton("Eliminar", (dialogInterface, i) -> {
                                    DatabaseReference refEmpleado = FirebaseDatabase.getInstance().getReference("Productos")
                                            .child(producto.getIdProducto())
                                            .child("eliminado");
                                    refEmpleado.setValue(true)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(context, "Producto eliminado con exito", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context, "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                })
                                .setNegativeButton("Cancelar",null)
                                .show();
                        return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private TextView txtPrecio;
        private ImageButton btnOpciones;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtPrecio = (TextView) itemView.findViewById(R.id.txtPrecio);
            btnOpciones = (ImageButton) itemView.findViewById(R.id.btnOpciones);
        }
    }
}
