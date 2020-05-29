package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.BottomSheets.TotalBottomSheet;
import com.zamnadev.tortillinas.Moldes.Concepto;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.VentaCliente;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorTotalRepartidores extends RecyclerView.Adapter<AdaptadorTotalRepartidores.ViewHolder> {

    private Context context;
    private ArrayList<String> repartidores;
    private ArrayList<Double> totales;
    private String idVenta;
    private TotalBottomSheet padre;

    public AdaptadorTotalRepartidores(Context context, ArrayList<String> repartidores, String idVenta, TotalBottomSheet padre) {
        this.context = context;
        this.repartidores = repartidores;
        this.idVenta = idVenta;
        this.padre = padre;
        totales = new ArrayList<>();
        for (String r : repartidores) {
            totales.add(0.0);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_total_repartidores,parent,false);
        return new AdaptadorTotalRepartidores.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String id = repartidores.get(position);

        FirebaseDatabase.getInstance().getReference("Empleados")
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Empleado empleado = dataSnapshot.getValue(Empleado.class);
                        holder.txtNombre.setText(empleado.getNombre().getNombres() + " " + empleado.getNombre().getApellidos());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        holder.btnAccion.setOnClickListener(view1 -> {
            if (holder.btnAccion.getTag().equals("down")) {
                holder.btnAccion.setTag("up");
                holder.btnAccion.setImageResource(R.drawable.ic_arrow_up_24dp);
                holder.lytVenta.setVisibility(View.VISIBLE);
            } else {
                holder.btnAccion.setTag("down");
                holder.btnAccion.setImageResource(R.drawable.ic_arrow_down_24dp);
                holder.lytVenta.setVisibility(View.GONE);
            }
        });

        holder.cardView.setOnClickListener(view1 -> {
            if (holder.btnAccion.getTag().equals("down")) {
                holder.btnAccion.setTag("up");
                holder.btnAccion.setImageResource(R.drawable.ic_arrow_up_24dp);
                holder.lytVenta.setVisibility(View.VISIBLE);
            } else {
                holder.btnAccion.setTag("down");
                holder.btnAccion.setImageResource(R.drawable.ic_arrow_down_24dp);
                holder.lytVenta.setVisibility(View.GONE);
            }
        });

        FirebaseDatabase.getInstance().getReference("AuxVentaRepartidor")
                .child(id)
                .child(idVenta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        holder.masaVendidaP = 0.0; holder.masaP = 0.0;
                        holder.tortillaVendidaP = 0.0; holder.toritillaP = 0.0;
                        holder.totoposVendidoP = 0.0; holder.totoposP = 0.0;
                        holder.masaVendidaS = 0.0; holder.masaS = 0.0;
                        holder.tortillaVendidaS = 0.0; holder.toritillaS = 0.0;
                        holder.totoposVendidoS = 0.0; holder.totoposS = 0.0;
                        holder.masaVendidaD = 0.0; holder.masaD = 0.0;
                        holder.tortillaVendidaD = 0.0; holder.toritillaD = 0.0;
                        holder.totoposVendidoD = 0.0; holder.totoposD = 0.0;
                        holder.subTotalP = 0.0; holder.subTotalD = 0.0; holder.subTotalS = 0.0;
                        holder.total = 0.0;
                        holder.totalFinal = 0.0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            VentaCliente ventaCliente = snapshot.getValue(VentaCliente.class);
                            holder.total += ventaCliente.getPago();
                            //TODO PRIMER VUELTA
                            if (ventaCliente.getVuelta1() != null) {
                                if (ventaCliente.getVuelta1().getMasa() >= 0.0) {
                                    holder.masaVendidaP += ventaCliente.getVuelta1().getMasa();
                                    holder.masaP += ventaCliente.getVuelta1().getMasaVenta();
                                }
                                if (ventaCliente.getVuelta1().getTortillas() >= 0.0) {
                                    holder.tortillaVendidaP += ventaCliente.getVuelta1().getTortillas();
                                    holder.toritillaP += ventaCliente.getVuelta1().getTortillaVenta();
                                }
                                if (ventaCliente.getVuelta1().getTotoposVenta() >= 0.0) {
                                    holder.totoposVendidoP += ventaCliente.getVuelta1().getTotopos();
                                    holder.totoposP += ventaCliente.getVuelta1().getTotoposVenta();
                                }

                            }
                            //TODO SEGUNDA VUELTA
                            if (ventaCliente.getVuelta2() != null) {
                                if (ventaCliente.getVuelta2().getMasa() >= 0.0) {
                                    holder.masaVendidaS += ventaCliente.getVuelta2().getMasa();
                                    holder.masaS += ventaCliente.getVuelta2().getMasaVenta();
                                }
                                if (ventaCliente.getVuelta2().getTortillas() >= 0.0) {
                                    holder.tortillaVendidaS += ventaCliente.getVuelta2().getTortillas();
                                    holder.toritillaS += ventaCliente.getVuelta2().getTortillaVenta();
                                }
                                if (ventaCliente.getVuelta2().getTotoposVenta() >= 0.0) {
                                    holder.totoposVendidoS += ventaCliente.getVuelta2().getTotopos();
                                    holder.totoposS += ventaCliente.getVuelta2().getTotoposVenta();
                                }
                            }
                            //TODO DEVOLUCION
                            if (ventaCliente.getDevolucion() != null) {
                                if (ventaCliente.getDevolucion().getMasa() >= 0.0) {
                                    holder.masaVendidaD += ventaCliente.getDevolucion().getMasa();
                                    holder.masaD += ventaCliente.getDevolucion().getMasaVenta();
                                }
                                if (ventaCliente.getDevolucion().getTortillas() >= 0.0) {
                                    holder.tortillaVendidaD += ventaCliente.getDevolucion().getTortillas();
                                    holder.toritillaD += ventaCliente.getDevolucion().getTortillaVenta();
                                }
                                if (ventaCliente.getDevolucion().getTotoposVenta() >= 0.0) {
                                    holder.totoposVendidoD += ventaCliente.getDevolucion().getTotopos();
                                    holder.totoposD += ventaCliente.getDevolucion().getTotoposVenta();
                                }
                            }
                        }

                        holder.subTotalP += holder.totoposP + holder.toritillaP + holder.masaP;
                        holder.txtPSubtotal.setText("+ $ " + holder.subTotalP);
                        holder.subTotalS += holder.totoposS + holder.toritillaS + holder.masaS;
                        holder.txtSSubTotal.setText("+ $" + holder.subTotalS);
                        holder.subTotalD += holder.totoposD + holder.toritillaD + holder.masaD;
                        holder.txtDSubtotal.setText("- $" + holder.subTotalD);
                        holder.totalFinal += holder.subTotalP + holder.subTotalS;
                        holder.totalFinal -= holder.subTotalD;

                        FirebaseDatabase.getInstance().getReference("Gastos")
                                .child(id)
                                .child(idVenta)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            holder.gastos = 0.0;
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                Concepto concepto = snapshot.getValue(Concepto.class);
                                                holder.gastos += concepto.getPrecio();
                                            }
                                            holder.txtGastos.setText("- $" +  holder.gastos);
                                            holder.totalFinal -= holder.gastos;
                                            holder.total -= holder.gastos;
                                            holder.txtTotalFinal.setText("TOTAL: $" + holder.totalFinal);
                                            totales.set(position,holder.total);
                                            if (holder.total < holder.totalFinal) {
                                                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.error_background));
                                                holder.cardView.setStrokeColor(ContextCompat.getColor(context, R.color.error_text));
                                            }
                                            if (position == totales.size()-1) {
                                                if (padre != null) {
                                                    padre.aumentarRepartidores();
                                                }
                                            }
                                            holder.txtTotal.setText("+ $" + holder.total);
                                        } else {
                                            holder.lytGastos.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                        FirebaseDatabase.getInstance().getReference("VentasRepartidor")
                                .child(id)
                                .child(idVenta)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            return;
                                        }
                                        VentaRepartidor ventaRepartidor = dataSnapshot.getValue(VentaRepartidor.class);
                                        //TODO PRIEMER VUELTA
                                        if (ventaRepartidor.getVuelta1() == null) {
                                            holder.lytPrimerVuelta.setVisibility(View.GONE);
                                        } else {
                                            if (ventaRepartidor.getVuelta1().isConfirmado() && ventaRepartidor.getVuelta1().isRegistrada()) {
                                                if (holder.masaVendidaP > 0.0 && holder.masaP > 0.0) {
                                                    holder.txtPMasa.setText("Masa: " + holder.masaVendidaP + "kgs");
                                                    holder.txtPMasaP.setText("$" + holder.masaP);
                                                } else {
                                                    holder.lytPMasa.setVisibility(View.GONE);
                                                }
                                                if (holder.toritillaP > 0.0 && holder.tortillaVendidaP > 0.0) {
                                                    holder.txtPTortilla.setText("Tortilla: " + holder.tortillaVendidaP + "kgs");
                                                    holder.txtPTortillaP.setText("$" + holder.toritillaP);
                                                } else {
                                                    holder.lytPTortilla.setVisibility(View.GONE);
                                                }
                                                if (holder.totoposP > 0.0 && holder.totoposVendidoP > 0.0) {
                                                    holder.txtPTotopo.setText("Totopo: " + holder.totoposVendidoP + "kgs");
                                                    holder.txtPTotopoP.setText("$" + holder.totoposP);
                                                } else {
                                                    holder.lytPTotopo.setVisibility(View.GONE);
                                                }
                                            } else {
                                                holder.lytPrimerVuelta.setVisibility(View.GONE);
                                            }
                                        }

                                        //TODO SEGUNDA VUELTA
                                        if (ventaRepartidor.getVuelta2() == null) {
                                            holder.lytSegundaVuelta.setVisibility(View.GONE);
                                        } else {
                                            if (ventaRepartidor.getVuelta2().isConfirmado() && ventaRepartidor.getVuelta2().isRegistrada()) {
                                                if (holder.masaVendidaS > 0.0 && holder.masaS > 0.0) {
                                                    holder.txtSMasa.setText("Masa: " + holder.masaVendidaS + "kgs");
                                                    holder.txtSMasaS.setText("$" + holder.masaS);
                                                } else {
                                                    holder.lytSMasa.setVisibility(View.GONE);
                                                }
                                                if (holder.toritillaS > 0.0 && holder.tortillaVendidaS > 0.0) {
                                                    holder.txtSTortilla.setText("Tortilla: " + holder.tortillaVendidaS + "kgs");
                                                    holder.txtSTortillaS.setText("$" + holder.toritillaS);
                                                } else {
                                                    holder.lytSTortilla.setVisibility(View.GONE);
                                                }
                                                if (holder.totoposS > 0.0 && holder.totoposVendidoS > 0.0) {
                                                    holder.txtSTotopo.setText("Totopo: " + holder.totoposVendidoS + "kgs");
                                                    holder.txtSTotopoS.setText("$" + holder.totoposS);
                                                } else {
                                                    holder.lytSTotopo.setVisibility(View.GONE);
                                                }
                                            } else {
                                                holder.lytSegundaVuelta.setVisibility(View.GONE);
                                            }
                                        }

                                        //TODO DEVOLUCION
                                        if (holder.totoposD >= 0.0 || holder.toritillaD >= 0.0 || holder.masaD >= 0.0) {
                                            if (holder.masaVendidaD > 0.0 && holder.masaD > 0.0) {
                                                holder.txtDMasa.setText("Masa: " + holder.masaVendidaD + "kgs");
                                                holder.txtDMasaD.setText("$" + holder.masaD);
                                            } else {
                                                holder.lytDMasa.setVisibility(View.GONE);
                                            }
                                            if (holder.toritillaD > 0.0 && holder.tortillaVendidaD > 0.0) {
                                                holder.txtDTortilla.setText("Tortilla: " + holder.tortillaVendidaD + "kgs");
                                                holder.txtDTortillaD.setText("$" + holder.toritillaD);
                                            } else {
                                                holder.lytDTortilla.setVisibility(View.GONE);
                                            }
                                            if (holder.totoposD > 0.0 && holder.totoposVendidoD > 0.0) {
                                                holder.txtDTotopo.setText("Totopo: " + holder.totoposVendidoD + "kgs");
                                                holder.txtDTotopoD.setText("$" + holder.totoposD);
                                            } else {
                                                holder.lytDTotopo.setVisibility(View.GONE);
                                            }
                                        } else {
                                            holder.lytDevoluciones.setVisibility(View.GONE);
                                        }
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
    }

    @Override
    public int getItemCount() {
        return repartidores.size();
    }

    public ArrayList<Double> getTotales() {
        return totales;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private TextView txtTotal;
        private ImageButton btnAccion;
        private MaterialCardView cardView;
        private LinearLayout lytVenta;
        private double total, gastos;
        private LinearLayout lytGastos;
        private TextView txtGastos;
        private TextView txtTotalFinal;

        //TODO PRIMER VUELTA
        private LinearLayout lytPrimerVuelta;
        private TextView txtPMasa;
        private LinearLayout lytPMasa;
        private TextView txtPMasaP;
        private TextView txtPTortilla;
        private LinearLayout lytPTortilla;
        private TextView txtPTortillaP;
        private TextView txtPTotopo;
        private LinearLayout lytPTotopo;
        private TextView txtPTotopoP;
        private TextView txtPSubtotal;
        private double masaVendidaP, masaP;
        private double tortillaVendidaP, toritillaP;
        private double totoposVendidoP, totoposP;
        private double subTotalP;

        //TODO SEGUNDA VUELTA
        private LinearLayout lytSegundaVuelta;
        private TextView txtSMasa;
        private LinearLayout lytSMasa;
        private TextView txtSTortilla;
        private LinearLayout lytSTortilla;
        private TextView txtSTotopo;
        private LinearLayout lytSTotopo;
        private double masaVendidaS, masaS;
        private double tortillaVendidaS, toritillaS;
        private double totoposVendidoS, totoposS;
        private TextView txtSMasaS;
        private TextView txtSTortillaS;
        private TextView txtSTotopoS;
        private TextView txtSSubTotal;
        private double subTotalS;

        //TODO DEVOLUCIONES
        private LinearLayout lytDevoluciones;
        private TextView txtDMasa;
        private LinearLayout lytDMasa;
        private TextView txtDMasaD;
        private TextView txtDTortilla;
        private LinearLayout lytDTortilla;
        private TextView txtDTortillaD;
        private TextView txtDTotopo;
        private LinearLayout lytDTotopo;
        private TextView txtDTotopoD;
        private double masaVendidaD, masaD;
        private double tortillaVendidaD, toritillaD;
        private double totoposVendidoD, totoposD;
        private TextView txtDSubtotal;
        private double subTotalD, totalFinal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            btnAccion = itemView.findViewById(R.id.btnAccion);
            cardView = itemView.findViewById(R.id.card_ventas_extra);
            lytVenta = itemView.findViewById(R.id.lytVenta);
            lytGastos = itemView.findViewById(R.id.lytGastos);
            txtGastos = itemView.findViewById(R.id.txtGastos);
            txtTotalFinal = itemView.findViewById(R.id.txtTotalFinal);

            //TODO PRIMER VUELTA
            lytPrimerVuelta = itemView.findViewById(R.id.lytPrimerVuelta);
            txtPMasa = itemView.findViewById(R.id.txtPMasa);
            lytPMasa = itemView.findViewById(R.id.lytPMasa);
            txtPMasaP = itemView.findViewById(R.id.txtPMasaP);
            txtPTortilla = itemView.findViewById(R.id.txtPTortilla);
            lytPTortilla = itemView.findViewById(R.id.lytPTortilla);
            txtPTotopo = itemView.findViewById(R.id.txtPTotopos);
            lytPTotopo = itemView.findViewById(R.id.lytPTotopos);
            txtPTortillaP = itemView.findViewById(R.id.txtPTortillaP);
            txtPTotopoP = itemView.findViewById(R.id.txtPTotopoP);
            txtPSubtotal = itemView.findViewById(R.id.txtPSubtotal);

            //TODO SEGUNDA VUELTA
            lytSegundaVuelta = itemView.findViewById(R.id.lytSegundaVuelta);
            txtSMasa = itemView.findViewById(R.id.txtSMasa);
            lytSMasa = itemView.findViewById(R.id.lytSMasa);
            txtSTortilla = itemView.findViewById(R.id.txtSTortilla);
            lytSTortilla = itemView.findViewById(R.id.lytSTortilla);
            txtSTotopo = itemView.findViewById(R.id.txtSTotopos);
            lytSTotopo = itemView.findViewById(R.id.lytSTotopos);
            txtSMasaS = itemView.findViewById(R.id.txtSMasaS);
            txtSTortillaS = itemView.findViewById(R.id.txtSTortillaS);
            txtSTotopoS = itemView.findViewById(R.id.txtSTotopoS);
            txtSSubTotal = itemView.findViewById(R.id.txtSSubtotal);

            //TODO DEVOLUCIONES
            lytDevoluciones = itemView.findViewById(R.id.lytDevoluciones);
            txtDMasa = itemView.findViewById(R.id.txtDMasa);
            lytDMasa = itemView.findViewById(R.id.lytDMasa);
            txtDTortilla = itemView.findViewById(R.id.txtDTortilla);
            lytDTortilla = itemView.findViewById(R.id.lytDTortilla);
            txtDTotopo = itemView.findViewById(R.id.txtDTotopos);
            lytDTotopo = itemView.findViewById(R.id.lytDTotopos);
            txtDMasaD = itemView.findViewById(R.id.txtDMasaD);
            txtDTortillaD = itemView.findViewById(R.id.txtDTortillaD);
            txtDTotopoD = itemView.findViewById(R.id.txtDTotopoD);
            txtDSubtotal = itemView.findViewById(R.id.txtDSubtotal);
        }

    }
}
