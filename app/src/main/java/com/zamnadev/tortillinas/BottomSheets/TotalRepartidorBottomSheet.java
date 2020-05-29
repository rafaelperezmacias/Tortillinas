package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorTotal;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorTotalClientes;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorTotalRepartidores;
import com.zamnadev.tortillinas.Moldes.AuxVenta;
import com.zamnadev.tortillinas.Moldes.Concepto;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaCliente;
import com.zamnadev.tortillinas.Moldes.VentaDelDia;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TotalRepartidorBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private String idVenta;
    private String idEmpleado;
    private String idSucursal;

    private double total;

    private AdaptadorTotalClientes adaptador;
    private TextView txtTotal;
    private TextView txtVendidoUno;
    private TextView txtVendidoDos;

    public TotalRepartidorBottomSheet(String idVenta, String idEmpleado, String idSucursal)
    {
        this.idVenta = idVenta;
        this.idEmpleado = idEmpleado;
        this.idSucursal = idSucursal;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_total_repartidores_bottom_sheet, null);
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if(BottomSheetBehavior.STATE_HIDDEN == i) dismiss();
                if(BottomSheetBehavior.STATE_DRAGGING == i) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override public void onSlide(@NonNull View view, float v) { }
        });

        view.findViewById(R.id.btnGuardar).setVisibility(View.GONE);

        TextView txtSucursal = view.findViewById(R.id.txtSucursal);

        MaterialCardView cardGastosMostrador = view.findViewById(R.id.card_gastos_mostrador);
        TextView txtTotalGastosMostrador = view.findViewById(R.id.txtGastosMostrador);
        RecyclerView rGastosMostrador = view.findViewById(R.id.recyclerviewGastosMostrador);
        ImageButton btnGastosMostrador = view.findViewById(R.id.btnGastosMostrador);

        RecyclerView rClientes = view.findViewById(R.id.recyclerview_repartidores);

        txtTotal = view.findViewById(R.id.txtTotal);

        LinearLayout lytPrimerVuelta = view.findViewById(R.id.lytPrimerVuelta);
        TextView txtProducidoUno = view.findViewById(R.id.txtProducidoUno);
        txtVendidoUno = view.findViewById(R.id.txtVendidoUno);

        LinearLayout lytSegundaVuelta = view.findViewById(R.id.lytSegundaVuelta);
        TextView txtProducidoDos = view.findViewById(R.id.txtProducidoDos);
        txtVendidoDos = view.findViewById(R.id.txtVendidoDos);

        rGastosMostrador.setLayoutManager(new LinearLayoutManager(getContext()));
        rGastosMostrador.setHasFixedSize(true);
        rClientes.setLayoutManager(new LinearLayoutManager(getContext()));
        rClientes.setHasFixedSize(true);

        //TODO muestra la informacion de la venta de tipo repartidor
       FirebaseDatabase.getInstance().getReference("VentasRepartidor")
                .child(idEmpleado)
                .child(idVenta)
               .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        VentaRepartidor ventaRepartidor = dataSnapshot.getValue(VentaRepartidor.class);

                        if (ventaRepartidor.getVuelta1() != null) {
                            if (ventaRepartidor.getVuelta1().isRegistrada() && ventaRepartidor.getVuelta1().isConfirmado()) {
                                String text = "";
                                if (ventaRepartidor.getVuelta1().getTortillas() > 0) {
                                    text += "Tortillas: " + ventaRepartidor.getVuelta1().getTortillas() + " kgs.\n";
                                }
                                if (ventaRepartidor.getVuelta1().getMasa() > 0) {
                                    text += "Masa: " + ventaRepartidor.getVuelta1().getMasa() + " kgs.\n";
                                }
                                if (ventaRepartidor.getVuelta1().getTotopos() > 0) {
                                    text += "Totopos: " + ventaRepartidor.getVuelta1().getTotopos() + " kgs.\n";
                                }
                                txtProducidoUno.setText(text);
                            } else {
                                lytPrimerVuelta.setVisibility(View.GONE);
                            }
                        } else {
                            lytPrimerVuelta.setVisibility(View.GONE);
                        }
                        if (ventaRepartidor.getVuelta2() != null) {
                            if (ventaRepartidor.getVuelta2().isRegistrada() && ventaRepartidor.getVuelta2().isConfirmado()) {
                                String text = "";
                                if (ventaRepartidor.getVuelta2().getTortillas() > 0) {
                                    text += "Tortillas: " + ventaRepartidor.getVuelta2().getTortillas() + " kgs.\n";
                                }
                                if (ventaRepartidor.getVuelta2().getMasa() > 0) {
                                    text += "Masa: " + ventaRepartidor.getVuelta2().getMasa() + " kgs.\n";
                                }
                                if (ventaRepartidor.getVuelta2().getTotopos() > 0) {
                                    text += "Totopos: " + ventaRepartidor.getVuelta2().getTotopos() + " kgs.\n";
                                }
                                txtProducidoDos.setText(text);
                            } else {
                                lytSegundaVuelta.setVisibility(View.GONE);
                            }
                        } else {
                            lytSegundaVuelta.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

       FirebaseDatabase.getInstance().getReference("AuxVentaRepartidor")
                .child(idEmpleado)
                .child(idVenta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> clientes = new ArrayList<>();
                        clientes.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            VentaCliente vc = snapshot.getValue(VentaCliente.class);
                            clientes.add(vc.getIdCliente());
                        }
                        adaptador = new AdaptadorTotalClientes(getContext(),clientes,idVenta,TotalRepartidorBottomSheet.this,idEmpleado);
                        rClientes.setAdapter(adaptador);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


       FirebaseDatabase.getInstance().getReference("Gastos")
            .child(idEmpleado)
            .child(idVenta)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<Concepto> conceptos = new ArrayList<>();
                    if (dataSnapshot.exists()) {
                        double total = 0.0;
                        conceptos.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Concepto concepto = snapshot.getValue(Concepto.class);
                            total += concepto.getPrecio();
                            conceptos.add(concepto);
                        }
                        txtTotalGastosMostrador.setText("- $" + total);
                        TotalRepartidorBottomSheet.this.total -= total;
                        txtTotal.setText("TOTAL: $" + TotalRepartidorBottomSheet.this.total);
                        AdaptadorTotal adaptador = new AdaptadorTotal(getContext(),conceptos);
                        rGastosMostrador.setAdapter(adaptador);
                    } else {
                        txtTotalGastosMostrador.setText("+ $0.0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

       DatabaseReference refSucursal = FirebaseDatabase.getInstance().getReference("Sucursales")
                .child(idSucursal);
       refSucursal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Sucursal sucursal = dataSnapshot.getValue(Sucursal.class);
                txtSucursal.setText(sucursal.getNombre());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
       });

       btnGastosMostrador.setOnClickListener(view1 -> {
            if (btnGastosMostrador.getTag().equals("down")) {
                btnGastosMostrador.setTag("up");
                btnGastosMostrador.setImageResource(R.drawable.ic_arrow_up_24dp);
                rGastosMostrador.setVisibility(View.VISIBLE);
            } else {
                btnGastosMostrador.setTag("down");
                btnGastosMostrador.setImageResource(R.drawable.ic_arrow_down_24dp);
                rGastosMostrador.setVisibility(View.GONE);
            }
        });

       cardGastosMostrador.setOnClickListener(view1 -> {
            if (btnGastosMostrador.getTag().equals("down")) {
                btnGastosMostrador.setTag("up");
                btnGastosMostrador.setImageResource(R.drawable.ic_arrow_up_24dp);
                rGastosMostrador.setVisibility(View.VISIBLE);
            } else {
                btnGastosMostrador.setTag("down");
                btnGastosMostrador.setImageResource(R.drawable.ic_arrow_down_24dp);
                rGastosMostrador.setVisibility(View.GONE);
            }
       });

        ((ImageButton) view.findViewById(R.id.btn_cerrar))
                .setOnClickListener(view1 -> dismiss());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toolbar toolbar = view.findViewById(R.id.toolbar);
            NestedScrollView nestedScrollView = view.findViewById(R.id.nested_scroll_ventas);
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener)
                    (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                        if(scrollY == 0) {
                            toolbar.setElevation(0);
                        } else {
                            toolbar.setElevation(8);
                        }
                    });
        }
        setCancelable(false);
        return bottomSheet;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void aumentarRepartidores() {
        double tmpPrecio = 0.0;
        double masaP = 0.0, tortillaP = 0.0, totoposP = 0.0;
        double masaS = 0.0, tortillaS = 0.0, totoposS = 0.0;
        int x = 0;
        for (double p : adaptador.getTotales()) {
            tmpPrecio += p;
            masaP += adaptador.getMasaP().get(x);
            masaS += adaptador.getMasaS().get(x);
            tortillaP += adaptador.getTortillaP().get(x);
            tortillaS += adaptador.getTortillaS().get(x);
            totoposP += adaptador.getTotoposP().get(x);
            totoposS += adaptador.getTotoposS().get(x);
            x++;
        }
        total += tmpPrecio;
        txtTotal.setText("TOTAL: $" + total);
        String text = "";
        if (tortillaP > 0) {
            text += "Tortillas: " + tortillaP + " kgs.\n";
        }
        if (masaP > 0) {
            text += "Masa: " + masaP + " kgs.\n";
        }
        if (totoposP > 0) {
            text += "Totopos: " + totoposP + " kgs.\n";
        }
        txtVendidoUno.setText(text);
        text = "";
        if (tortillaS > 0) {
            text += "Tortillas: " + tortillaS + " kgs.\n";
        }
        if (masaS > 0) {
            text += "Masa: " + masaS + " kgs.\n";
        }
        if (totoposS > 0) {
            text += "Totopos: " + totoposS + " kgs.\n";
        }
        txtVendidoDos.setText(text);
    }

    public double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
        return resultado;
    }
}

