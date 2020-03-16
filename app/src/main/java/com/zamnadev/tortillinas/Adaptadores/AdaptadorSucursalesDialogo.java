package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorSucursalesDialogo extends RecyclerView.Adapter<AdaptadorSucursalesDialogo.ViewHolder> {

    private Context context;
    private ArrayList<Sucursal> sucursals;
    private ArrayList<Sucursal> tmpSucursales;

    public AdaptadorSucursalesDialogo(Context context, ArrayList<Sucursal> sucursals)
    {
        this.context = context;
        this.sucursals = sucursals;
        tmpSucursales = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_sucursales_dialogo,parent,false);
        return new AdaptadorSucursalesDialogo.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Sucursal sucursal = sucursals.get(position);

        holder.cbSucursal.setText(sucursal.getNombre());

        holder.cbSucursal.setOnClickListener(view -> {
            if (holder.cbSucursal.isChecked()) {
                tmpSucursales.add(sucursal);
            } else {
                tmpSucursales.remove(sucursal);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sucursals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cbSucursal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbSucursal = (CheckBox) itemView.findViewById(R.id.cbSucursal);
        }
    }

    public ArrayList<Sucursal> getTmpSucursales() {
        return tmpSucursales;
    }
}
