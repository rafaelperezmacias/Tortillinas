package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.BottomSheets.ClientesBottomSheet;
import com.zamnadev.tortillinas.BottomSheets.VentasClienteBottomSheet;
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.Moldes.VentaCliente;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.util.ArrayList;

public class AdaptadorRepartidorClientes extends RecyclerView.Adapter<AdaptadorRepartidorClientes.ViewHolder> {

    private Context context;
    private ArrayList<Cliente> clientes;
    private VentaRepartidor venta;
    private FragmentManager fragmentManager;
    private ArrayList<VentaCliente> ventaClientes;

    public AdaptadorRepartidorClientes(Context context, ArrayList<Cliente> clientes, String idVenta, FragmentManager fragmentManager) {
        this.context = context;
        this.clientes = clientes;
        this.fragmentManager = fragmentManager;
        FirebaseDatabase.getInstance().getReference("VentasRepartidor")
                .child(ControlSesiones.ObtenerUsuarioActivo(context))
                .child(idVenta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        venta = dataSnapshot.getValue(VentaRepartidor.class);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_repartidor_clientes,parent,false);
        return new AdaptadorRepartidorClientes.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.txtNombre.setText(cliente.getNombre().getNombres() + " " + cliente.getNombre().getApellidos());
        holder.txtPseudonimo.setText(cliente.getPseudonimo());
        VentaCliente ventaCliente = null;

        if (ventaClientes != null && ventaClientes.size() > 0) {
            for (VentaCliente v : ventaClientes) {
                if (v.getIdCliente().equals(cliente.getIdCliente())) {
                    ventaCliente = v;
                    if (v.getVuelta1() != null) {
                        String text = "";
                        if (v.getVuelta1().getTortillas() >= 0) {
                            text += ", " + v.getVuelta1().getTortillas();
                        }
                        if (v.getVuelta1().getMasa() >= 0) {
                            text += ", " + v.getVuelta1().getMasa();
                        }
                        if (v.getVuelta1().getTotopos() >= 0) {
                            text += ", " + v.getVuelta1().getTotopos();
                        }
                        text = text.replaceFirst(",","");
                        text = text.replaceFirst(" ","");
                        holder.txtPrimer.setText(text);
                    }
                    if (v.getVuelta2() != null) {
                        String text = "";
                        if (v.getVuelta2().getTortillas() >= 0) {
                            text += ", " + v.getVuelta2().getTortillas();
                        }
                        if (v.getVuelta2().getMasa() >= 0) {
                            text += ", " + v.getVuelta2().getMasa();
                        }
                        if (v.getVuelta2().getTotopos() >= 0) {
                            text += ", " + v.getVuelta2().getTotopos();
                        }
                        text = text.replaceFirst(",","");
                        text = text.replaceFirst(" ","");
                        holder.txtSegunda.setText(text);
                    }
                    if (v.getDevolucion() != null) {
                        String text = "";
                        if (v.getDevolucion().getTortillas() >= 0) {
                            text += ", " + v.getDevolucion().getTortillas();
                        }
                        if (v.getDevolucion().getMasa() >= 0) {
                            text += ", " + v.getDevolucion().getMasa();
                        }
                        if (v.getDevolucion().getTotopos() >= 0) {
                            text += ", " + v.getDevolucion().getTotopos();
                        }
                        text = text.replaceFirst(",","");
                        text = text.replaceFirst(" ","");
                        holder.txtDevolucion.setText(text);
                    }
                }
            }
        }

        double total = 0;
        if (venta != null) {
            if (venta.getVuelta1() == null && venta.getVuelta2() == null) {
                //TODO algo
            } else {
                holder.txtDevolucion.setEnabled(true);
                VentaCliente finalVentaCliente = ventaCliente;
                holder.txtDevolucion.setOnClickListener(view -> {
                    VentasClienteBottomSheet ventas = new VentasClienteBottomSheet(true,cliente,venta, finalVentaCliente,true);
                    ventas.show(fragmentManager,ventas.getTag());
                });
            }

            if (venta.getVuelta1() == null) {
                holder.txtPrimer.setVisibility(View.GONE);
            } else {
                VentaCliente finalVentaCliente = ventaCliente;
                if (ventaCliente.getVuelta1() != null) {
                    if (ventaCliente.getVuelta1().getMasaVenta() >= 0.0) {
                        total += ventaCliente.getVuelta1().getMasaVenta();
                    }
                    if (ventaCliente.getVuelta1().getTortillaVenta() >= 0.0) {
                        total += ventaCliente.getVuelta1().getTortillaVenta();
                    }
                    if (ventaCliente.getVuelta1().getTotoposVenta() >= 0.0) {
                        total += ventaCliente.getVuelta1().getTotoposVenta();
                    }
                    holder.txtPendiente.setText("Pendiente: $" + total);
                }
                holder.txtPrimer.setOnClickListener(view -> {
                    VentasClienteBottomSheet ventas = new VentasClienteBottomSheet(true,cliente,venta, finalVentaCliente);
                    ventas.show(fragmentManager,ventas.getTag());
                });
                holder.txtPrimer.setEnabled(true);
            }

            if (venta.getVuelta2() == null) {
                holder.txtSegunda.setVisibility(View.GONE);
            } else {
                VentaCliente finalVentaCliente = ventaCliente;
                if (ventaCliente.getVuelta2() != null) {
                    if (ventaCliente.getVuelta2().getMasaVenta() >= 0.0) {
                        total += ventaCliente.getVuelta2().getMasaVenta();
                    }
                    if (ventaCliente.getVuelta2().getTortillaVenta() >= 0.0) {
                        total += ventaCliente.getVuelta2().getTortillaVenta();
                    }
                    if (ventaCliente.getVuelta2().getTotoposVenta() >= 0.0) {
                        total += ventaCliente.getVuelta2().getTotoposVenta();
                    }
                    holder.txtPendiente.setText("Pendiente: $" + total);
                }
                holder.txtSegunda.setOnClickListener(view -> {
                    VentasClienteBottomSheet ventas = new VentasClienteBottomSheet(false,cliente,venta, finalVentaCliente);
                    ventas.show(fragmentManager,ventas.getTag());
                });
                holder.txtSegunda.setEnabled(true);
            }

            if (ventaCliente.getDevolucion() != null) {
                if (ventaCliente.getDevolucion().getMasaVenta() >= 0.0) {
                    total -= ventaCliente.getDevolucion().getMasaVenta();
                }
                if (ventaCliente.getDevolucion().getTortillaVenta() >= 0.0) {
                    total -= ventaCliente.getDevolucion().getTortillaVenta();
                }
                if (ventaCliente.getDevolucion().getTotoposVenta() >= 0.0) {
                    total -= ventaCliente.getDevolucion().getTotoposVenta();
                }
                holder.txtPendiente.setText("Pendiente: $" + total);
            }
        }

        holder.btnCliente.setOnClickListener(view -> {
            ClientesBottomSheet bottomSheet = new ClientesBottomSheet(cliente,true);
            bottomSheet.show(fragmentManager,bottomSheet.getTag());
        });

        holder.btnFormulario.setOnClickListener(view -> {
            if (holder.btnFormulario.getTag().equals("down")) {
                holder.btnFormulario.setTag("up");
                holder.btnFormulario.setImageResource(R.drawable.ic_arrow_down_24dp);
                holder.lytBody.setVisibility(View.VISIBLE);
            } else {
                holder.btnFormulario.setTag("down");
                holder.btnFormulario.setImageResource(R.drawable.ic_arrow_up_24dp);
                holder.lytBody.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public ArrayList<VentaCliente> getVentaClientes() {
        return ventaClientes;
    }

    public void addVentas(ArrayList<VentaCliente> ventaClientes) {
        this.ventaClientes = ventaClientes;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private ImageButton btnCliente;
        private ImageButton btnFormulario;
        private RelativeLayout lytBody;
        private TextView txtPendiente;
        private TextInputEditText txtPrimer;
        private TextInputEditText txtSegunda;
        private TextInputEditText txtPago;
        private TextInputEditText txtDevolucion;
        private TextView txtPseudonimo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            btnCliente = itemView.findViewById(R.id.btnMostrarCliente);
            btnFormulario = itemView.findViewById(R.id.btnMostarFormulario);
            lytBody = itemView.findViewById(R.id.lytBody);
            txtPendiente = itemView.findViewById(R.id.txtPendiente);
            txtPrimer = itemView.findViewById(R.id.txtPrimer);
            txtSegunda = itemView.findViewById(R.id.txtSegunda);
            txtPago = itemView.findViewById(R.id.txtPago);
            txtDevolucion = itemView.findViewById(R.id.txtDevolucion);
            txtPseudonimo = itemView.findViewById(R.id.txtPseudonimo);
        }

    }
}
