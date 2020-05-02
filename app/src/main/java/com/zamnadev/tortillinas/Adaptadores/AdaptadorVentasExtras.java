package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zamnadev.tortillinas.BottomSheets.VentasMostradorBottomSheet;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.VentaDelDia;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.util.ArrayList;
import java.util.HashMap;

public class AdaptadorVentasExtras extends RecyclerView.Adapter<AdaptadorVentasExtras.ViewHolder> {

    private Context context;
    private ArrayList<Producto> productos;
    private ArrayList<VentaDelDia> ventasMotrador;

    private boolean recuperaData;
    private String idVenta;
    private VentasMostradorBottomSheet bottomSheet;
    private boolean isEditable;

    public AdaptadorVentasExtras(Context context, ArrayList<Producto> productos, ArrayList<VentaDelDia> ventasMotrador, VentasMostradorBottomSheet bottomSheet, boolean isEditable) {
        this.context = context;
        this.productos = productos;
        this.ventasMotrador = ventasMotrador;
        this.bottomSheet = bottomSheet;
        this.isEditable = isEditable;
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
        for (VentaDelDia dia: ventasMotrador) {
            if (dia.getIdProducto().equals(producto.getIdProducto())) {
                if (dia.getCantidad() != 0.0) {
                    holder.txtField.setText("" + dia.getCantidad());
                } else {
                    holder.txtField.setText("");
                }
            }
        }

        if (!isEditable) {
            hideTxt(holder.txtField);
        }

        holder.txtField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!holder.txtField.getText().toString().isEmpty()) {
                    try {
                        int x = Integer.parseInt(holder.txtField.getText().toString());
                        ventasMotrador.get(position).setCantidad(x);
                    } catch (Exception ignored) { }
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

        private TextInputLayout lytField;
        private TextInputEditText txtField;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lytField = itemView.findViewById(R.id.lytField);
            txtField = itemView.findViewById(R.id.txtField);
        }
    }

    public ArrayList<VentaDelDia> getVentasMotrador() {
        return ventasMotrador;
    }

    public void addVenta(ArrayList<VentaDelDia> ventasMotrador) {
        this.ventasMotrador = ventasMotrador;
        notifyDataSetChanged();
    }

    private void hideTxt(TextInputEditText txt) {
        txt.setClickable(false);
        txt.setLongClickable(false);
        txt.setFocusable(false);
        txt.setCursorVisible(false);
    }
}
