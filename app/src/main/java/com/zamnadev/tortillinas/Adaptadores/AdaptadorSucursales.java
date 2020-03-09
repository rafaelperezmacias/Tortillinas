package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorSucursales extends RecyclerView.Adapter<AdaptadorSucursales.ViewHolder> {

    private Context context;
    private ArrayList<Sucursal> sucursals;

    public AdaptadorSucursales(Context context, ArrayList<Sucursal> sucursals)
    {
        this.context = context;
        this.sucursals = sucursals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_sucursal,parent,false);
        return new AdaptadorSucursales.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sucursal sucursal = sucursals.get(position);

        holder.txtNombre.setText(sucursal.getNombre());
        holder.txtDireccion.setText(sucursal.getDireccion().toStringRecyclerView());
    }

    @Override
    public int getItemCount() {
        return sucursals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private TextView txtDireccion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtDireccion = (TextView) itemView.findViewById(R.id.txtDireccion);
        }
    }
}
