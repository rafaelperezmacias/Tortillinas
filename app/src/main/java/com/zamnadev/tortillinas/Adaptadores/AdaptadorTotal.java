package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zamnadev.tortillinas.Moldes.Concepto;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorTotal extends RecyclerView.Adapter<AdaptadorTotal.ViewHolder> {

    private Context context;
    private ArrayList<Concepto> conceptos;

    public AdaptadorTotal(Context context, ArrayList<Concepto> conceptos)
    {
        this.context = context;
        this.conceptos = conceptos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_total, parent, false);
        return new AdaptadorTotal.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Concepto concepto = conceptos.get(position);
        holder.txtNombre.setText(concepto.getNombre());
        holder.txtPrecio.setText("$" + concepto.getPrecio());
    }

    @Override
    public int getItemCount() {
        return conceptos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private TextView txtPrecio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
        }
    }
}
