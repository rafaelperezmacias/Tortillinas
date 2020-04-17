package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorVentasExtras extends RecyclerView.Adapter<AdaptadorVentasExtras.ViewHolder> {

    private Context context;
    private ArrayList<Producto> productos;

    public AdaptadorVentasExtras(Context context, ArrayList<Producto> productos) {
        this.context = context;
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_ventas_extra,parent,false);
        return new AdaptadorVentasExtras.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.lytField.setHint(producto.getNombre());
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextInputLayout lytField;
        private TextInputEditText txtField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lytField = itemView.findViewById(R.id.lytField);
            txtField = itemView.findViewById(R.id.txtField);
        }
    }
}
