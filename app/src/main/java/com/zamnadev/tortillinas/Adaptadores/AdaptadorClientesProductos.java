package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorClientesProductos extends RecyclerView.Adapter<AdaptadorClientesProductos.ViewHolder> {

    private Context context;
    private ArrayList<Producto> productos;
    private ArrayList<Producto> nuevosPrecios;

    public AdaptadorClientesProductos(Context context, ArrayList<Producto> productos, ArrayList<Producto> nuevosPrecios)
    {
        this.context = context;
        this.productos = productos;
        this.nuevosPrecios = nuevosPrecios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_clientes_productos,parent,false);
        return new AdaptadorClientesProductos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.txtProducto.setText(producto.getNombre());
        holder.lytPrecio.setHint("Precio: " + producto.getPrecio());

        holder.txtPrecio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.txtPrecio.getText().toString().isEmpty()) {
                    nuevosPrecios.set(position,producto);
                } else {
                    Producto p = new Producto(producto);
                    p.setPrecio(Double.parseDouble(holder.txtPrecio.getText().toString().trim()));
                    nuevosPrecios.set(position,p);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtProducto;
        private TextInputLayout lytPrecio;
        private TextInputEditText txtPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProducto = (TextView) itemView.findViewById(R.id.txtProducto);
            lytPrecio = (TextInputLayout) itemView.findViewById(R.id.lytPrecio);
            txtPrecio = (TextInputEditText) itemView.findViewById(R.id.txtPrecio);
        }
    }

    public boolean validaCampos() {
        return true;
    }

    public ArrayList<Producto> getNuevosPrecios() {
        return nuevosPrecios;
    }

}
