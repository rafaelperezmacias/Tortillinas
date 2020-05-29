package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.BottomSheets.TotalBottomSheet;
import com.zamnadev.tortillinas.BottomSheets.TotalRepartidorBottomSheet;
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.Moldes.Concepto;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.VentaCliente;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.Notificaciones.Client;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorTotalClientes extends RecyclerView.Adapter<AdaptadorTotalClientes.ViewHolder> {

    private Context context;
    private String idRepartidor;
    private ArrayList<String> repartidores;
    private ArrayList<Double> totales;
    private ArrayList<Double> masaP;
    private ArrayList<Double> totoposP;
    private ArrayList<Double> tortillaP;
    private ArrayList<Double> masaS;
    private ArrayList<Double> totoposS;
    private ArrayList<Double> tortillaS;
    private String idVenta;
    private TotalRepartidorBottomSheet padre;

    public AdaptadorTotalClientes(Context context, ArrayList<String> repartidores, String idVenta, TotalRepartidorBottomSheet padre, String idRepartidor) {
        this.context = context;
        this.repartidores = repartidores;
        this.idVenta = idVenta;
        this.idRepartidor = idRepartidor;
        this.padre = padre;
        totales = new ArrayList<>();
        masaP = new ArrayList<>();
        totoposP = new ArrayList<>();
        tortillaP = new ArrayList<>();
        masaS = new ArrayList<>();
        totoposS = new ArrayList<>();
        tortillaS = new ArrayList<>();
        for (String r : repartidores) {
            totales.add(0.0);
            masaP.add(0.0);
            totoposP.add(0.0);
            tortillaP.add(0.0);
            masaS.add(0.0);
            totoposS.add(0.0);
            tortillaS.add(0.0);
        }
    }

    @NonNull
    @Override
    public AdaptadorTotalClientes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_total_repartidores,parent,false);
        return new AdaptadorTotalClientes.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorTotalClientes.ViewHolder holder, int position) {
        String id = repartidores.get(position);

        holder.lytGastos.setVisibility(View.GONE);

        FirebaseDatabase.getInstance().getReference("Clientes")
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Cliente cliente = dataSnapshot.getValue(Cliente.class);
                        holder.txtNombre.setText(cliente.getNombre().getNombres() + " " + cliente.getNombre().getApellidos());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("AuxVentaRepartidor")
                .child(idRepartidor)
                .child(idVenta)
                .child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        holder.subTotalD = 0.0; holder.subTotalP = 0.0;
                        holder.subTotalS = 0.0; holder.total = 0.0;
                        double masaP = 0.0, tortillasP = 0.0, totoposP = 0.0;
                        double masaS = 0.0, tortillasS = 0.0, totoposS = 0.0;
                        VentaCliente vc = dataSnapshot.getValue(VentaCliente.class);
                        if (vc.getVuelta1() == null) {
                            holder.lytPrimerVuelta.setVisibility(View.GONE);
                        } else {
                            if (vc.getVuelta1().getMasa() >= 0.0 && vc.getVuelta1().getMasaVenta() >= 0.0) {
                                holder.txtPMasa.setText("Masa: " + vc.getVuelta1().getMasa() + "kgs");
                                holder.txtPMasaP.setText("$" + vc.getVuelta1().getMasaVenta());
                                holder.subTotalP += vc.getVuelta1().getMasaVenta();
                                masaP += vc.getVuelta1().getMasa();
                            } else {
                                holder.lytPMasa.setVisibility(View.GONE);
                            }
                            if (vc.getVuelta1().getTortillas() >= 0.0 && vc.getVuelta1().getTortillaVenta() >= 0.0) {
                                holder.txtPTortilla.setText("Tortilla: " + vc.getVuelta1().getTortillas() + "kgs");
                                holder.txtPTortillaP.setText("$" + vc.getVuelta1().getTortillaVenta());
                                holder.subTotalP += vc.getVuelta1().getTortillaVenta();
                                tortillasP += vc.getVuelta1().getTortillas();
                            } else {
                                holder.lytPTortilla.setVisibility(View.GONE);
                            }
                            if (vc.getVuelta1().getTotopos() >= 0.0 && vc.getVuelta1().getTotoposVenta() >= 0.0) {
                                holder.txtPTotopo.setText("Totopo: " + vc.getVuelta1().getTotopos() + "kgs");
                                holder.txtPTotopoP.setText("$" + vc.getVuelta1().getTotoposVenta());
                                holder.subTotalP += vc.getVuelta1().getTotoposVenta();
                                totoposP += vc.getVuelta1().getTotopos();
                            } else {
                                holder.lytPTotopo.setVisibility(View.GONE);
                            }
                        }

                        //TODO SEGUNDA VUELTA
                        if (vc.getVuelta2() == null) {
                            holder.lytSegundaVuelta.setVisibility(View.GONE);
                        } else {
                            if (vc.getVuelta2().getMasa() >= 0.0 && vc.getVuelta2().getMasaVenta() >= 0.0) {
                                holder.txtSMasa.setText("Masa: " + vc.getVuelta2().getMasa() + "kgs");
                                holder.txtSMasaS.setText("$" + vc.getVuelta2().getMasaVenta());
                                holder.subTotalS += vc.getVuelta2().getMasaVenta();
                                masaS += vc.getVuelta2().getMasa();
                            } else {
                                holder.lytSMasa.setVisibility(View.GONE);
                            }
                            if (vc.getVuelta2().getTortillas() >= 0.0 && vc.getVuelta2().getTortillaVenta() >= 0.0) {
                                holder.txtSTortilla.setText("Tortilla: " + vc.getVuelta2().getTortillas() + "kgs");
                                holder.txtSTortillaS.setText("$" + vc.getVuelta2().getTortillaVenta());
                                holder.subTotalS += vc.getVuelta2().getTortillaVenta();
                                tortillasS += vc.getVuelta2().getTortillas();
                            } else {
                                holder.lytSTortilla.setVisibility(View.GONE);
                            }
                            if (vc.getVuelta2().getTotopos() >= 0.0 && vc.getVuelta2().getTotoposVenta() >= 0.0) {
                                holder.txtSTotopo.setText("Totopo: " + vc.getVuelta2().getTotopos() + "kgs");
                                holder.txtSTotopoS.setText("$" + vc.getVuelta2().getTotoposVenta());
                                holder.subTotalS += vc.getVuelta2().getTotoposVenta();
                                totoposS += vc.getVuelta2().getTotopos();
                            } else {
                                holder.lytSTotopo.setVisibility(View.GONE);
                            }
                        }

                        //TODO DEVOLUCION
                        if (vc.getDevolucion() != null) {
                            if (vc.getDevolucion().getMasa() >= 0.0 && vc.getDevolucion().getMasaVenta() >= 0.0) {
                                holder.txtDMasa.setText("Masa: " + vc.getDevolucion().getMasa() + "kgs");
                                holder.txtDMasaD.setText("$" + vc.getDevolucion().getMasaVenta());
                                holder.subTotalD += vc.getDevolucion().getMasaVenta();
                            } else {
                                holder.lytDMasa.setVisibility(View.GONE);
                            }
                            if (vc.getDevolucion().getTortillas()  >= 0.0 && vc.getDevolucion().getTortillaVenta() >= 0.0) {
                                holder.txtDTortilla.setText("Tortilla: " + vc.getDevolucion().getTortillas() + "kgs");
                                holder.txtDTortillaD.setText("$" + vc.getDevolucion().getTortillaVenta());
                                holder.subTotalD += vc.getDevolucion().getTortillaVenta();
                            } else {
                                holder.lytDTortilla.setVisibility(View.GONE);
                            }
                            if (vc.getDevolucion().getTotopos() >= 0.0 && vc.getDevolucion().getTotoposVenta() >= 0.0) {
                                holder.txtDTotopo.setText("Totopo: " + vc.getDevolucion().getTotopos() + "kgs");
                                holder.txtDTotopoD.setText("$" + vc.getDevolucion().getTotoposVenta());
                                holder.subTotalD += vc.getDevolucion().getTotoposVenta();
                            } else {
                                holder.lytDTotopo.setVisibility(View.GONE);
                            }
                        } else {
                            holder.lytDevoluciones.setVisibility(View.GONE);
                        }

                        holder.txtPSubtotal.setText("+ $" + holder.subTotalP);
                        holder.txtSSubTotal.setText("+ $" + holder.subTotalS);
                        holder.txtDSubtotal.setText("- $" + holder.subTotalD);

                        holder.total += holder.subTotalP + holder.subTotalS;
                        holder.total -= holder.subTotalD;
                        holder.txtTotalFinal.setText("TOTAL: $" + holder.total);
                        holder.txtTotal.setText("+ $" + vc.getPago());
                        totales.set(position,vc.getPago());
                        //TODO CAMBIO DE COLOR DE LA MIERDA ESA
                        if (vc.getPago() < holder.total) {
                            holder.cardView.setCardBackgroundColor(R.color.error_background);
                            holder.cardView.setStrokeColor(R.color.error_text);
                        }
                        masaP +=  AdaptadorTotalClientes.this.masaP.get(position);
                        AdaptadorTotalClientes.this.masaP.set(position,masaP);
                        masaS +=  AdaptadorTotalClientes.this.masaS.get(position);
                        AdaptadorTotalClientes.this.masaS.set(position,masaS);
                        tortillasP +=  AdaptadorTotalClientes.this.tortillaP.get(position);
                        AdaptadorTotalClientes.this.tortillaP.set(position,tortillasP);
                        tortillasS +=  AdaptadorTotalClientes.this.tortillaS.get(position);
                        tortillaS.set(position,tortillasS);
                        totoposP +=  AdaptadorTotalClientes.this.totoposP.get(position);
                        AdaptadorTotalClientes.this.totoposP.set(position,totoposP);
                        totoposS +=  AdaptadorTotalClientes.this.totoposS.get(position);
                        AdaptadorTotalClientes.this.totoposS.set(position,totoposS);
                        if (totales.size()-1 == position) {
                            padre.aumentarRepartidores();
                        }
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
    }

    @Override
    public int getItemCount() {
        return repartidores.size();
    }

    public ArrayList<Double> getTotales() {
        return totales;
    }

    public ArrayList<Double> getMasaP() {
        return masaP;
    }

    public ArrayList<Double> getTotoposP() {
        return totoposP;
    }

    public ArrayList<Double> getTortillaP() {
        return tortillaP;
    }

    public ArrayList<Double> getMasaS() {
        return masaS;
    }

    public ArrayList<Double> getTotoposS() {
        return totoposS;
    }

    public ArrayList<Double> getTortillaS() {
        return tortillaS;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private TextView txtTotal;
        private ImageButton btnAccion;
        private MaterialCardView cardView;
        private LinearLayout lytVenta;
        private double total;
        private LinearLayout lytGastos;
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
        private double subTotalP;

        //TODO SEGUNDA VUELTA
        private LinearLayout lytSegundaVuelta;
        private TextView txtSMasa;
        private LinearLayout lytSMasa;
        private TextView txtSTortilla;
        private LinearLayout lytSTortilla;
        private TextView txtSTotopo;
        private LinearLayout lytSTotopo;
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
        private TextView txtDSubtotal;
        private double subTotalD;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            btnAccion = itemView.findViewById(R.id.btnAccion);
            cardView = itemView.findViewById(R.id.card_ventas_extra);
            lytVenta = itemView.findViewById(R.id.lytVenta);
            lytGastos = itemView.findViewById(R.id.lytGastos);
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
