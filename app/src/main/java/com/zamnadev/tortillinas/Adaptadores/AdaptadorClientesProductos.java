package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.Objects;

public class AdaptadorClientesProductos extends
        RecyclerView.Adapter<AdaptadorClientesProductos.ViewHolder> {
    private Context context;

    private ArrayList<Producto> productos;
    private ArrayList<Producto> nuevosPrecios;

    private boolean isEditable;
    private boolean isOnlyShow;

    public AdaptadorClientesProductos(Context context, ArrayList<Producto> productos,
                                      ArrayList<Producto> nuevosPrecios, boolean isEditable,
                                      boolean isOnlyShow) {
        this.context = context;
        this.productos = productos;
        this.nuevosPrecios = nuevosPrecios;
        this.isEditable = isEditable;
        this.isOnlyShow = isOnlyShow;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == 1) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_adaptador_clientes_productos_2, parent,false);
        } else if(viewType == 2) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_adaptador_clientes_productos, parent,false);
        }
        return new ViewHolder(Objects.requireNonNull(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.lytPrecio.setHint(producto.getNombre());
        holder.txtPrecio.setText(String.valueOf(producto.getPrecio()));
        holder.txtPrecio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Objects.requireNonNull(holder.txtPrecio.getText()).toString().isEmpty()) {
                    nuevosPrecios.set(position, producto);
                } else {
                    Producto p = new Producto(producto);
                    p.setPrecio(Double.parseDouble(holder.txtPrecio.getText().toString().trim()));
                    nuevosPrecios.set(position, p);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        if (isOnlyShow) {
            ocultaCampo(holder.txtPrecio);
        }

        if (isEditable) { holder.txtPrecio.setText(String.valueOf(producto.getPrecio())); }
    }

    @Override
    public int getItemCount() { return productos.size(); }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextInputLayout lytPrecio;

        private TextInputEditText txtPrecio;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lytPrecio = itemView.findViewById(R.id.lytPrecio);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
        }
    }

    private void ocultaCampo(TextInputEditText txt) {
        txt.setCursorVisible(false);
        txt.setLongClickable(false);
        txt.setFocusable(false);
        txt.setClickable(false);
    }

    public ArrayList<Producto> getNuevosPrecios() { return nuevosPrecios; }
}