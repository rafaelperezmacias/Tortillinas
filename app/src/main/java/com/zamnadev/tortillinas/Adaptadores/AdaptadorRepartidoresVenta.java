package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.BottomSheets.VueltaBottomSheet;
import com.zamnadev.tortillinas.Moldes.AuxVenta;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdaptadorRepartidoresVenta extends RecyclerView.Adapter<AdaptadorRepartidoresVenta.ViewHolder> {

    private Context context;
    private ArrayList<String> ids;
    private FragmentManager fragmentManager;
    private String idVenta;
    private String nombreSucursal;
    private boolean isEditable;

    public AdaptadorRepartidoresVenta(Context context, ArrayList<String> ids, FragmentManager fragmentManager, String idVenta, String nombreSucursal, boolean isEditable) {
        this.context = context;
        this.ids = ids;
        this.fragmentManager = fragmentManager;
        this.idVenta = idVenta;
        this.nombreSucursal = nombreSucursal;
        this.isEditable = isEditable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_repartidore_venta,parent,false);
        return new AdaptadorRepartidoresVenta.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String id = ids.get(position);
        DatabaseReference refEmpleado = FirebaseDatabase.getInstance().getReference("Empleados")
                .child(id);
        refEmpleado.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.empleado = dataSnapshot.getValue(Empleado.class);
                holder.txtNombre.setText(holder.empleado.getNombre().getNombres() + " " + holder.empleado.getNombre().getApellidos());

                if (!isEditable) {
                    holder.btnPrimero.setVisibility(View.GONE);
                    holder.btnSegundo.setVisibility(View.GONE);
                }

                holder.btnPrimero.setOnClickListener(view -> {
                    VueltaBottomSheet vueltaBottomSheet = new VueltaBottomSheet(true, holder.empleado,idVenta, context, nombreSucursal);
                    vueltaBottomSheet.show(fragmentManager,vueltaBottomSheet.getTag());
                });

                holder.btnSegundo.setOnClickListener(view -> {
                    VueltaBottomSheet vueltaBottomSheet = new VueltaBottomSheet(false, holder.empleado,idVenta, context, nombreSucursal);
                    vueltaBottomSheet.show(fragmentManager,vueltaBottomSheet.getTag());
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                .child(idVenta)
                .child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AuxVenta auxVenta = dataSnapshot.getValue(AuxVenta.class);
                if (auxVenta == null) {
                    return;
                }
                if (!auxVenta.getVuelta1().isRegistrada()) {
                    holder.txtPrimeraVuelta.setText("Sin registrar");
                    holder.txtEstadoUno.setVisibility(View.GONE);
                    holder.btnEditarPrimer.setVisibility(View.GONE);
                    holder.btnPrimero.setVisibility(View.VISIBLE);
                } else {
                    holder.btnPrimero.setVisibility(View.GONE);
                    holder.txtEstadoUno.setVisibility(View.VISIBLE);
                    String text = "";
                    if (auxVenta.getVuelta1().getTortillas() > 0) {
                        text += "\t\tTortillas: " +  auxVenta.getVuelta1().getTortillas() + " kgs.\n";
                    }
                    if (auxVenta.getVuelta1().getMasa() > 0) {
                        text += "\t\tMasa: " +  auxVenta.getVuelta1().getMasa() + " kgs.\n";
                    }
                    if (auxVenta.getVuelta1().getTotopos() > 0) {
                        text += "\t\tTotopos: " +  auxVenta.getVuelta1().getTotopos() + " kgs.\n";
                    }
                    holder.txtPrimeraVuelta.setText(text);
                    if (auxVenta.getVuelta1().isConfirmado()) {
                        holder.txtEstadoUno.setText("Confirmado");
                        holder.btnEditarPrimer.setVisibility(View.GONE);
                    } else {
                        if (isEditable) {
                            holder.txtEstadoUno.setText("Esperando confirmación");
                        } else {
                            holder.txtEstadoUno.setText("Vuelta no confirmada");
                        }
                        if (isEditable) {
                            holder.btnEditarPrimer.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (!auxVenta.getVuelta2().isRegistrada()) {
                    holder.txtSegundaVuelta.setText("Sin registrar");
                    holder.btnSegundo.setVisibility(View.VISIBLE);
                    holder.btnEditarSegunda.setVisibility(View.GONE);
                    holder.txtEstadoDos.setVisibility(View.GONE);
                } else {
                    holder.btnSegundo.setVisibility(View.GONE);
                    holder.txtEstadoDos.setVisibility(View.VISIBLE);
                    String text = "";
                    if (auxVenta.getVuelta2().getTortillas() > 0) {
                        text += "\t\tTortillas: " +  auxVenta.getVuelta2().getTortillas() + " kgs.\n";
                    }
                    if (auxVenta.getVuelta2().getMasa() > 0) {
                        text += "\t\tMasa: " +  auxVenta.getVuelta2().getMasa() + " kgs.\n";
                    }
                    if (auxVenta.getVuelta2().getTotopos() > 0) {
                        text += "\t\tTotopos: " +  auxVenta.getVuelta2().getTotopos() + " kgs.\n";
                    }
                    holder.txtSegundaVuelta.setText(text);
                    if (auxVenta.getVuelta2().isConfirmado()) {
                        holder.txtEstadoDos.setText("Confirmado");
                        holder.btnEditarSegunda.setVisibility(View.GONE);
                    } else {
                        if (isEditable) {
                            holder.txtEstadoDos.setText("Esperando confirmación");
                        } else {
                            holder.txtEstadoDos.setText("Vuelta no confirmada");
                        }
                        if (isEditable) {
                            holder.btnEditarSegunda.setVisibility(View.VISIBLE);
                        }
                    }
                }

                holder.btnEditarPrimer.setOnClickListener(view -> {
                    if (holder.empleado != null) {
                        VueltaBottomSheet vueltaBottomSheet = new VueltaBottomSheet(true, holder.empleado,idVenta, context, nombreSucursal, true, auxVenta.getVuelta1());
                        vueltaBottomSheet.show(fragmentManager,vueltaBottomSheet.getTag());
                    }
                });

                holder.btnEditarSegunda.setOnClickListener(view -> {
                    if (holder.empleado != null) {
                        VueltaBottomSheet vueltaBottomSheet = new VueltaBottomSheet(false, holder.empleado,idVenta, context, nombreSucursal, true, auxVenta.getVuelta2());
                        vueltaBottomSheet.show(fragmentManager,vueltaBottomSheet.getTag());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return ids.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private ImageButton btnPrimero;
        private ImageButton btnSegundo;
        private TextView txtPrimeraVuelta;
        private TextView txtSegundaVuelta;
        private TextView txtEstadoUno;
        private TextView txtEstadoDos;
        private ImageButton btnEditarPrimer;
        private ImageButton btnEditarSegunda;
        private Empleado empleado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            btnPrimero = itemView.findViewById(R.id.btnPrimero);
            btnSegundo = itemView.findViewById(R.id.btnSegundo);
            txtPrimeraVuelta = itemView.findViewById(R.id.txtPrimerVuelta);
            txtSegundaVuelta = itemView.findViewById(R.id.txtSegundaVuelta);
            txtEstadoUno = itemView.findViewById(R.id.txtEstadoUno);
            txtEstadoDos = itemView.findViewById(R.id.txtEstadoDos);
            btnEditarPrimer = itemView.findViewById(R.id.btnEditarPrimero);
            btnEditarSegunda = itemView.findViewById(R.id.btnEditarSegundo);
        }
    }
}
