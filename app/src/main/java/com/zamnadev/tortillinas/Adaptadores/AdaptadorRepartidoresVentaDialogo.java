package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorRepartidoresVentaDialogo extends RecyclerView.Adapter<AdaptadorRepartidoresVentaDialogo.ViewHolder> {

    private Context context;
    private ArrayList<Empleado> empleados;
    private int repartidor;

    public AdaptadorRepartidoresVentaDialogo(Context context, ArrayList<Empleado> empleados)
    {
        this.context = context;
        this.empleados = empleados;
    }

    @NonNull
    @Override
    public AdaptadorRepartidoresVentaDialogo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_sucursales_dialogo,parent,false);
        return new AdaptadorRepartidoresVentaDialogo.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdaptadorRepartidoresVentaDialogo.ViewHolder holder, final int position) {
        final Empleado empleado = empleados.get(position);

        holder.cbSucursal.setText(empleado.getNombre().getNombres() + " " + empleado.getNombre().getApellidos());

        if (repartidor == position) {
            holder.cbSucursal.setChecked(true);
        } else {
            holder.cbSucursal.setChecked(false);
        }

        holder.cbSucursal.setOnClickListener(view -> {
            if (holder.cbSucursal.isChecked()) {
                repartidor = position;
                notifyDataSetChanged();
            } else {
                holder.cbSucursal.setChecked(true);
                repartidor = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return empleados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cbSucursal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbSucursal = (CheckBox) itemView.findViewById(R.id.cbSucursal);
        }
    }

    public String getIdEmpleado() {
        return empleados.get(repartidor).getIdEmpleado();
    }
}