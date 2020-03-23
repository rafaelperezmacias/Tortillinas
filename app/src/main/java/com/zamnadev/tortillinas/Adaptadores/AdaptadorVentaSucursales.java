package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorVentaSucursales extends RecyclerView.Adapter<AdaptadorVentaSucursales.ViewHolder> {

    private Context context;
    private ArrayList<Sucursal> sucursals;
    private int sucursalSeleccionada = 0;
    private ArrayList<Sucursal> sucursalsFinal;

    public AdaptadorVentaSucursales(Context context, ArrayList<Sucursal> sucursals)
    {
        this.context = context;
        this.sucursals = sucursals;

        sucursalsFinal = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Sucursal s = snapshot.getValue(Sucursal.class);
                    for (Sucursal sucursal : sucursals) {
                        if (s.getIdSucursal().equals(sucursal.getIdSucursal())) {
                            sucursalsFinal.add(s);
                            break;
                        }
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @NonNull
    @Override
    public AdaptadorVentaSucursales.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_sucursales_dialogo,parent,false);
        return new AdaptadorVentaSucursales.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdaptadorVentaSucursales.ViewHolder holder, final int position) {
        if (!sucursalsFinal.isEmpty()) {
            final Sucursal sucursal = sucursalsFinal.get(position);

            holder.cbSucursal.setText(sucursal.getNombre());

            if (sucursalSeleccionada == position) {
                holder.cbSucursal.setChecked(true);
            } else {
                holder.cbSucursal.setChecked(false);
            }

            holder.cbSucursal.setOnClickListener(view -> {
                if (holder.cbSucursal.isChecked()) {
                    sucursalSeleccionada = position;
                    notifyDataSetChanged();
                } else {
                    holder.cbSucursal.setChecked(true);
                    sucursalSeleccionada = position;
                    notifyDataSetChanged();
                }
            });
        }
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
        return sucursalsFinal.get(sucursalSeleccionada).getIdSucursal();
    }
}
