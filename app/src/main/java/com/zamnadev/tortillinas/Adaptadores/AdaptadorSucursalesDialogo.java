package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
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
    private int positionCheckBox;

    public AdaptadorSucursalesDialogo(Context context, ArrayList<Sucursal> sucursals)
    {
        positionCheckBox = 0;
        this.context = context;
        this.sucursals = sucursals;
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

        if (positionCheckBox == position) {
            holder.cbSucursal.setChecked(true);
        } else {
            holder.cbSucursal.setChecked(false);
        }

        holder.cbSucursal.setText(sucursal.getNombre());

        holder.cbSucursal.setOnClickListener(view -> {
            holder.cbSucursal.setChecked(true);
            positionCheckBox = position;
            notifyDataSetChanged();
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

    public String getIdSucursalActiva() {
        return sucursals.get(positionCheckBox).getIdSucursal();
    }

    public String getSucursalActiva() {
        return sucursals.get(positionCheckBox).getNombre();
    }
}
