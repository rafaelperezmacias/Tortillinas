package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.HashMap;

public class AdaptadorRepartidorClientes extends RecyclerView.Adapter<AdaptadorRepartidorClientes.ViewHolder> {

    private Context context;
    private ArrayList<Cliente> clientes;
    private VentaRepartidor venta;
    private FragmentManager fragmentManager;
    private ArrayList<VentaCliente> ventaClientes;
    private ArrayList<VentaCliente> pagos;

    private double tortillasVentaPrimerVuelta;
    private double masaVentaPrimerVuelta;
    private double totoposVentaPrimerVuelta;
    private double tortillasVentaSegundaVuelta;
    private double masaVentaSegundaVuelta;
    private double totoposVentaSegundaVuelta;

    private boolean isEditable;

    public AdaptadorRepartidorClientes(VentaRepartidor venta, Context context, ArrayList<Cliente> clientes, String idVenta, FragmentManager fragmentManager, boolean isEditable, String idEmpleado) {
        this.context = context;
        this.clientes = clientes;
        this.fragmentManager = fragmentManager;
        this.isEditable = isEditable;
        this.venta = venta;
        pagos = new ArrayList<>();
        tortillasVentaPrimerVuelta = -1;
        masaVentaPrimerVuelta = -1;
        totoposVentaPrimerVuelta = -1;
        tortillasVentaSegundaVuelta = -1;
        masaVentaSegundaVuelta = -1;
        totoposVentaSegundaVuelta = -1;
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
        holder.total = 0.0;

        if (!isEditable) {
            hideTxt(holder.txtPago);
        }

        if (ventaClientes != null && ventaClientes.size() > 0) {
            for (VentaCliente v : ventaClientes) {
                if (v.getIdCliente().equals(cliente.getIdCliente())) {
                    for (int x = 0; x < ventaClientes.size(); x++) {
                        Log.e("asd","" + ventaClientes.get(x).getPago() + ", id = " + ventaClientes.get(x).getIdCliente());
                    }
                    for (int x = 0; x < pagos.size(); x++) {
                        Log.e("das","" + pagos.get(x).getPago()  + ", id = " + pagos.get(x).getIdCliente());
                    }
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
                    break;
                }
            }
        }

        if (venta != null) {
            if (venta.getVuelta1() == null && venta.getVuelta2() == null) {
                holder.txtDevolucion.setEnabled(false);
                holder.txtPago.setEnabled(true);
                return;
            }
            if (venta.getVuelta1() == null) {
                holder.txtPrimer.setVisibility(View.GONE);
            } else {
                VentaCliente finalVentaCliente = ventaCliente;
                if (ventaCliente != null) {
                    boolean mostrarDevolucion = false;
                    if (ventaCliente.getVuelta1() != null) {
                        if (ventaCliente.getVuelta1().getMasa() >= 0.0) {
                            holder.total += ventaCliente.getVuelta1().getMasaVenta();
                            mostrarDevolucion = true;
                        }
                        if (ventaCliente.getVuelta1().getTortillas() >= 0.0) {
                            holder.total += ventaCliente.getVuelta1().getTortillaVenta();
                            mostrarDevolucion = true;
                        }
                        if (ventaCliente.getVuelta1().getTotopos() >= 0.0) {
                            holder.total += ventaCliente.getVuelta1().getTotoposVenta();
                            mostrarDevolucion = true;
                        }
                        holder.txtPendiente.setText("Pendiente: $" + holder.total);
                    }
                    if (mostrarDevolucion) {
                        holder.txtDevolucion.setEnabled(true);
                        holder.txtPago.setEnabled(true);
                        holder.txtDevolucion.setOnClickListener(view -> {
                            VentasClienteBottomSheet ventas = new VentasClienteBottomSheet(getMe(),true,cliente,venta, finalVentaCliente,true, isEditable);
                            ventas.show(fragmentManager,ventas.getTag());
                        });
                    }
                }
                holder.txtPrimer.setOnClickListener(view -> {
                    VentasClienteBottomSheet ventas = new VentasClienteBottomSheet(getMe(), true,cliente,venta, finalVentaCliente,false,isEditable);
                    ventas.show(fragmentManager,ventas.getTag());
                });
                holder.txtPrimer.setEnabled(true);
            }

            if (venta.getVuelta2() == null) {
                holder.txtSegunda.setVisibility(View.GONE);
            } else {
                VentaCliente finalVentaCliente = ventaCliente;
                if (ventaCliente != null) {
                    boolean mostrarDevolucion = false;
                    if (ventaCliente.getVuelta2() != null) {
                        if (ventaCliente.getVuelta2().getMasa() >= 0.0) {
                            holder.total += ventaCliente.getVuelta2().getMasaVenta();
                            mostrarDevolucion = true;
                        }
                        if (ventaCliente.getVuelta2().getTortillas() >= 0.0) {
                            holder.total += ventaCliente.getVuelta2().getTortillaVenta();
                            mostrarDevolucion = true;
                        }
                        if (ventaCliente.getVuelta2().getTotopos() >= 0.0) {
                            holder.total += ventaCliente.getVuelta2().getTotoposVenta();
                            mostrarDevolucion = true;
                        }
                        if (mostrarDevolucion) {
                            holder.txtDevolucion.setEnabled(true);
                            holder.txtPago.setEnabled(true);
                            holder.txtDevolucion.setOnClickListener(view -> {
                                VentasClienteBottomSheet ventas = new VentasClienteBottomSheet(getMe(), true,cliente,venta, finalVentaCliente,true, isEditable);
                                ventas.show(fragmentManager,ventas.getTag());
                            });
                        }
                        holder.txtPendiente.setText("Pendiente: $" + holder.total);
                    }
                }
                holder.txtSegunda.setOnClickListener(view -> {
                    VentasClienteBottomSheet ventas = new VentasClienteBottomSheet(getMe(), false,cliente,venta, finalVentaCliente,false,isEditable);
                    ventas.show(fragmentManager,ventas.getTag());
                });
                holder.txtSegunda.setEnabled(true);
            }

            if (ventaCliente != null) {
                if (ventaCliente.getVuelta1() != null || ventaCliente.getVuelta2() != null) {
                    VentaCliente finalVentaCliente1 = ventaCliente;
                    holder.txtPago.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            double pago = 0.0;
                            try {
                                pago = Double.parseDouble(holder.txtPago.getText().toString());
                            } catch (Exception ignored) {

                            }
                            for (int x = 0; x < pagos.size(); x++) {
                                if (finalVentaCliente1.getIdCliente().equals(pagos.get(x).getIdCliente())) {
                                    pagos.get(x).setPago(pago);
                                }
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }
                if (ventaCliente.getDevolucion() != null) {
                    if (ventaCliente.getDevolucion().getMasaVenta() >= 0.0) {
                        holder.total -= ventaCliente.getDevolucion().getMasaVenta();
                    }
                    if (ventaCliente.getDevolucion().getTortillaVenta() >= 0.0) {
                        holder.total -= ventaCliente.getDevolucion().getTortillaVenta();
                    }
                    if (ventaCliente.getDevolucion().getTotoposVenta() >= 0.0) {
                        holder.total -= ventaCliente.getDevolucion().getTotoposVenta();
                    }
                    holder.txtPendiente.setText("Pendiente: $" + holder.total);
                }
                if (ventaCliente.getPago() >= 0.0) {
                    holder.total -= ventaCliente.getPago();
                    holder.txtPendiente.setText("Pendiente: $" + holder.total);
                    holder.txtPago.setText("" + ventaCliente.getPago());
                }
            } else {
                holder.txtPendiente.setText("Pendiente: $0");
            }
        } else {
            holder.txtDevolucion.setEnabled(false);
        }

        holder.btnCliente.setOnClickListener(view -> {
            ClientesBottomSheet bottomSheet = new ClientesBottomSheet(cliente,true);
            bottomSheet.show(fragmentManager,bottomSheet.getTag());
        });

        holder.btnFormulario.setOnClickListener(view -> {
            if (holder.btnFormulario.getTag().equals("down")) {
                holder.btnFormulario.setTag("up");
                holder.btnFormulario.setImageResource(R.drawable.ic_arrow_up_24dp);
                holder.lytBody.setVisibility(View.VISIBLE);
            } else {
                holder.btnFormulario.setTag("down");
                holder.btnFormulario.setImageResource(R.drawable.ic_arrow_down_24dp);
                holder.lytBody.setVisibility(View.GONE);
            }
        });
    }

    private void hideTxt(TextInputEditText txt) {
        txt.setClickable(false);
        txt.setLongClickable(false);
        txt.setFocusable(false);
        txt.setCursorVisible(false);
    }

    private AdaptadorRepartidorClientes getMe() {
        return this;
    }

    public ArrayList<VentaCliente> getPagos() {
        return pagos;
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
        pagos.clear();
        pagos.addAll(ventaClientes);
        notifyDataSetChanged();
    }

    public double getTortillasVentaPrimerVuelta() {
        return tortillasVentaPrimerVuelta;
    }

    public double getMasaVentaPrimerVuelta() {
        return masaVentaPrimerVuelta;
    }

    public double getTotoposVentaPrimerVuelta() {
        return totoposVentaPrimerVuelta;
    }

    public double getTortillasVentaSegundaVuelta() {
        return tortillasVentaSegundaVuelta;
    }

    public double getMasaVentaSegundaVuelta() {
        return masaVentaSegundaVuelta;
    }

    public double getTotoposVentaSegundaVuelta() {
        return totoposVentaSegundaVuelta;
    }

    public void setTortillasVentaPrimerVuelta(double tortillasVentaPrimerVuelta) {
        this.tortillasVentaPrimerVuelta = tortillasVentaPrimerVuelta;
    }

    public void setMasaVentaPrimerVuelta(double masaVentaPrimerVuelta) {
        this.masaVentaPrimerVuelta = masaVentaPrimerVuelta;
    }

    public void setTotoposVentaPrimerVuelta(double totoposVentaPrimerVuelta) {
        this.totoposVentaPrimerVuelta = totoposVentaPrimerVuelta;
    }

    public void setTortillasVentaSegundaVuelta(double tortillasVentaSegundaVuelta) {
        this.tortillasVentaSegundaVuelta = tortillasVentaSegundaVuelta;
    }

    public void setMasaVentaSegundaVuelta(double masaVentaSegundaVuelta) {
        this.masaVentaSegundaVuelta = masaVentaSegundaVuelta;
    }

    public void setTotoposVentaSegundaVuelta(double totoposVentaSegundaVuelta) {
        this.totoposVentaSegundaVuelta = totoposVentaSegundaVuelta;
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
        private double total;

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
            total = 0.0;
        }
    }

}