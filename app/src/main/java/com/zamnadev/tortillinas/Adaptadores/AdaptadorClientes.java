package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.ProductoModificado;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorClientes extends RecyclerView.Adapter<AdaptadorClientes.ViewHolder> {

    private Context context;
    private ArrayList<Cliente> clientes;

    public AdaptadorClientes(Context context, ArrayList<Cliente> clientes) {
        this.context = context;
        this.clientes = clientes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_clientes,parent,false);
        return new AdaptadorClientes.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);

        holder.txtNombre.setText(cliente.getNombre().getNombres() + " " +  cliente.getNombre().getApellidos());

        //holder.txtTelefono.setText(cliente.getTelefono());

        holder.txtTelefono.setText(cliente.toString());

        if (cliente.isPreferencial()) {
            for (int x = 0; x < cliente.getPrecios().size(); x++) {
                ProductoModificado p = new ProductoModificado(cliente.getPrecios().get("p"+x));
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Productos")
                        .child(p.getIdProducto());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Producto producto = dataSnapshot.getValue(Producto.class);
                        Log.e("Producto Modificado", producto.getNombre() + ", " + p.getPrecio());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        //holder.txtDireccion.setText(cliente.getDireccion().toString());
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private TextView txtTelefono;
        private TextView txtDireccion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtTelefono = (TextView) itemView.findViewById(R.id.txtTelefono);
            txtDireccion = (TextView) itemView.findViewById(R.id.txtDireccion);
        }
    }
}
