package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sucursales.DesvincularEmpleadosSucursal;

import java.util.ArrayList;

public class AdaptadorSucursalesEmpleado extends RecyclerView.Adapter<AdaptadorSucursalesEmpleado.ViewHolder> {

    private Context context;
    private ArrayList<Sucursal> sucursals;
    private DesvincularEmpleadosSucursal padre;

    public AdaptadorSucursalesEmpleado(Context context, ArrayList<Sucursal> sucursals, DesvincularEmpleadosSucursal padre) {
        this.context = context;
        this.sucursals = sucursals;
        this.padre = padre;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_sucursales_empleado,parent,false);
        return new AdaptadorSucursalesEmpleado.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sucursal sucursal = sucursals.get(position);

        holder.txtNombre.setText(sucursal.getNombre());

        holder.btnEliminar.setOnClickListener(view -> {
            padre.eliminarSucursal(sucursal.getIdSucursal());
        });
    }

    @Override
    public int getItemCount() {
        return sucursals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private ImageButton btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            btnEliminar = (ImageButton) itemView.findViewById(R.id.btnEliminar);
        }
    }
}
