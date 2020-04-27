package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.BottomSheets.VentasRepartidorBottomSheet;
import com.zamnadev.tortillinas.Dialogs.MessageDialog;
import com.zamnadev.tortillinas.Dialogs.MessageDialogBuilder;
import com.zamnadev.tortillinas.Firma.FirmaActivity;
import com.zamnadev.tortillinas.Fragments.ConfirmacionesFragment;
import com.zamnadev.tortillinas.Moldes.Confirmacion;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.Moldes.Vuelta;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.util.ArrayList;
import java.util.HashMap;

public class AdaptadorConfirmaciones extends RecyclerView.Adapter<AdaptadorConfirmaciones.ViewHolder> {

    private Context context;
    private ArrayList<Confirmacion> confirmaciones;
    private ConfirmacionesFragment frmPadre;

    public AdaptadorConfirmaciones(Context context, ArrayList<Confirmacion> confirmaciones, ConfirmacionesFragment frmPadre) {
        this.context = context;
        this.confirmaciones = confirmaciones;
        this.frmPadre = frmPadre;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_confirmaciones, parent, false);
        return new AdaptadorConfirmaciones.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.confirmacion = confirmaciones.get(position);
        if (holder.confirmacion.getVuelta1() != null && holder.confirmacion.getVuelta2() != null) {
            if (holder.confirmacion.getVuelta1().isConfirmado()) {
                pintarRecyclerView(holder,holder.confirmacion,false);
            } else if (holder.confirmacion.getVuelta2().isConfirmado()) {
                pintarRecyclerView(holder,holder.confirmacion,true);
            } else {
                pintarRecyclerView(holder,holder.confirmacion,false);
                confirmaciones.get(position-1).setVuelta2(null);
            }
        } else if (holder.confirmacion.getVuelta1() != null) {
            pintarRecyclerView(holder, holder.confirmacion, true);
        } else if (holder.confirmacion.getVuelta2() != null ) {
            pintarRecyclerView(holder, holder.confirmacion, false);
        }
    }

    private void pintarRecyclerView(ViewHolder holder, Confirmacion confirmacion,boolean primero) {
        String text = "";
        if (primero) {
            text += "Primer vuelta";
            if (confirmacion.getVuelta1().getTortillas() > 0) {
                text += "\n\t\tTortillas: " +  confirmacion.getVuelta1().getTortillas() + " kgs.";
            }
            if (confirmacion.getVuelta1().getMasa() > 0) {
                text += "\n\t\tMasa: " +  confirmacion.getVuelta1().getMasa() + " kgs.";
            }
            if (confirmacion.getVuelta1().getTotopos() > 0) {
                text += "\n\t\tTotopos: " +  confirmacion.getVuelta1().getTotopos() + " kgs.";
            }
        } else {
            text += "Segunda vuelta";
            if (confirmacion.getVuelta2().getTortillas() > 0) {
                text += "\n\t\tTortillas: " +  confirmacion.getVuelta2().getTortillas() + " kgs.";
            }
            if (confirmacion.getVuelta2().getMasa() > 0) {
                text += "\n\t\tMasa: " +  confirmacion.getVuelta2().getMasa() + " kgs.";
            }
            if (confirmacion.getVuelta2().getTotopos() > 0) {
                text += "\n\t\tTotopos: " +  confirmacion.getVuelta2().getTotopos() + " kgs.";
            }
        }
        holder.txtInfo.setText(text);
        FirebaseDatabase.getInstance().getReference("Empleados")
                .child(confirmacion.getIdEmpleado())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Empleado empleado = dataSnapshot.getValue(Empleado.class);
                        holder.txtMostrador.setText("Mostrador: " + empleado.getNombre().getNombres() + " " +  empleado.getNombre().getApellidos());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference("VentasMostrador")
                .child(confirmacion.getIdEmpleado())
                .child(confirmacion.getIdVenta())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        VentaMostrador venta = dataSnapshot.getValue(VentaMostrador.class);
                        FirebaseDatabase.getInstance().getReference("Sucursales")
                                .child(venta.getIdSucursal())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Sucursal sucursal = dataSnapshot.getValue(Sucursal.class);
                                        holder.txtNombre.setText(sucursal.getNombre());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        String finalText = text;
        holder.itemView.setOnClickListener(view -> {
            MessageDialog dialog = new MessageDialog(context, new MessageDialogBuilder()
                    .setTitle("Alerta")
                    .setMessage(finalText + "\nIngrese su firma para confirmar")
                    .setPositiveButtonText("Firmar")
                    .setNegativeButtonText("Cancelar")
            );
            dialog.show();
            dialog.setPositiveButtonListener(v -> {
                frmPadre.addIntent(confirmacion.getIdVenta(),primero);
                dialog.dismiss();
            });
            dialog.setNegativeButtonListener(v -> dialog.dismiss());
        });
    }

    @Override
    public int getItemCount() {
        return confirmaciones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private TextView txtMostrador;
        private TextView txtInfo;
        private Confirmacion confirmacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtMostrador = itemView.findViewById(R.id.txtMostrador);
            txtInfo = itemView.findViewById(R.id.txtInfo);
        }
    }
}
