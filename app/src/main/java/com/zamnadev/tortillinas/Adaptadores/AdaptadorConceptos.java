package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.zamnadev.tortillinas.Dialogs.MessageDialog;
import com.zamnadev.tortillinas.Dialogs.MessageDialogBuilder;
import com.zamnadev.tortillinas.Moldes.Concepto;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.util.ArrayList;

public class AdaptadorConceptos extends RecyclerView.Adapter<AdaptadorConceptos.ViewHolder> {

    public static final int TIPO_GASTOS = 1000;
    public static final int TIPO_VENTAS_MOSTRADOR = 1001;

    private Context context;
    private ArrayList<Concepto> conceptos;
    private FragmentManager fragmentManager;
    private int tipo;
    private String idVenta;

    public AdaptadorConceptos(Context context, ArrayList<Concepto> conceptos, FragmentManager fragmentManager, int tipo, String idVenta) {
        this.context = context;
        this.conceptos = conceptos;
        this.fragmentManager = fragmentManager;
        this.tipo = tipo;
        this.idVenta = idVenta;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adapatador_concepto, parent, false);
        return new AdaptadorConceptos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Concepto concepto = conceptos.get(position);
        holder.txtPrecio.setText("" + concepto.getPrecio());
        holder.txtNombre.setText("" + concepto.getNombre());
        holder.btnRemove.setOnClickListener(view -> {
            MessageDialog dialog = new MessageDialog(context, new MessageDialogBuilder()
                    .setTitle("Alerta")
                    .setMessage("¿Esta seguro de querer eliminar este concepto?")
                    .setPositiveButtonText("Eliminar")
                    .setNegativeButtonText("Cancelar")
            );
            dialog.show();
            dialog.setPositiveButtonListener(v -> {
                if (tipo == TIPO_GASTOS) {
                    FirebaseDatabase.getInstance().getReference("Gastos")
                            .child(ControlSesiones.ObtenerUsuarioActivo(context))
                            .child(idVenta)
                            .child(concepto.getId())
                            .removeValue();
                } else if (tipo == TIPO_VENTAS_MOSTRADOR) {
                    FirebaseDatabase.getInstance().getReference("Mostrador")
                            .child(ControlSesiones.ObtenerUsuarioActivo(context))
                            .child(idVenta)
                            .child(concepto.getId())
                            .removeValue();
                }
                dialog.dismiss();
            });
            dialog.setNegativeButtonListener(v -> dialog.dismiss());
        });
    }

    @Override
    public int getItemCount() {
        return conceptos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPrecio;
        private TextView txtNombre;
        private ImageButton btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtPrecio = itemView.findViewById(R.id.txtPrecio);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
