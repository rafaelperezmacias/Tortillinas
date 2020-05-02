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
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorRepartidorClientes;
import com.zamnadev.tortillinas.MainActivity;
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.Moldes.Concepto;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaCliente;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.Notificaciones.Data;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class VentasRepartidorBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private Empleado empleado;
    private String idSucursal;
    private boolean isEditable;
    private String idVenta;

    private DatabaseReference refVenta;
    private DatabaseReference refAuxVenta;
    private DatabaseReference refClientes;
    private DatabaseReference refGastos;

    private ValueEventListener listenerVenta;
    private ValueEventListener listenerAuxVenta;
    private ValueEventListener listenerClientes;
    private ValueEventListener listenerGastos;

    private VentaRepartidor ventaRepartidor;
    private AdaptadorRepartidorClientes adaptador;

    public VentasRepartidorBottomSheet(String idVenta, Empleado empleado, String idSucursal)
    {
        this.idVenta = idVenta;
        this.empleado = empleado;
        this.idSucursal = idSucursal;
        isEditable = true;
    }

    public VentasRepartidorBottomSheet(String  idVenta, Empleado empleado, String idSucursal, boolean isEditable)
    {
        this.idVenta = idVenta;
        this.empleado = empleado;
        this.idSucursal = idSucursal;
        this.isEditable = isEditable;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_ventas_repartidor_bottom_sheet, null);
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

        ImageButton btnCerrar = view.findViewById(R.id.btn_cerrar);
        btnCerrar.setOnClickListener((v -> dismiss()));

        LinearLayout lytSegundo = view.findViewById(R.id.lytSecond);

        TextInputEditText txtFecha = view.findViewById(R.id.txt_fecha);

        TextView txtSucursal = view.findViewById(R.id.txtSucursal);

        TextView txtPrimerVuelta = view.findViewById(R.id.txtPrimerVuelta);
        TextView txtEstadoUno = view.findViewById(R.id.txtEstadoUno);
        ImageButton btnPrimero = view.findViewById(R.id.btnPrimero);

        TextView txtVendidosUno = view.findViewById(R.id.txtVendidosUno);
        TextView txtTituloVendidoUno = view.findViewById(R.id.txtTituloVendidoPrimero);
        TextView txtVendidosDos = view.findViewById(R.id.txtVendidosSegunda);
        TextView txtTituloVendidoDos = view.findViewById(R.id.txtTituloVendidoSegunda);

        TextView txtSegundaVuelta = view.findViewById(R.id.txtSegundaVuelta);
        TextView txtEstadoDos = view.findViewById(R.id.txtEstadoDos);
        ImageButton btnSegundo = view.findViewById(R.id.btnSegundo);

        TextInputEditText txtGasto = view.findViewById(R.id.txtGastos);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        String fecha = MainActivity.getFecha();
        ArrayList<VentaCliente> ventaClientes = new ArrayList<>();

        txtGasto.setOnClickListener(view12 -> {
            VentasAdicionalesBottomSheet ventas = new VentasAdicionalesBottomSheet(VentasAdicionalesBottomSheet.TIPO_GASTOS_REPARTIDOR,idVenta,isEditable);
            ventas.show(getChildFragmentManager(),ventas.getTag());
        });

        //TODO muestra la informacion de la venta de tipo repartidor
        refVenta = FirebaseDatabase.getInstance().getReference("VentasRepartidor")
                .child(empleado.getIdEmpleado())
                .child(idVenta);
        listenerVenta = refVenta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ventaRepartidor = dataSnapshot.getValue(VentaRepartidor.class);
                txtFecha.setText(ventaRepartidor.getFecha());
                if (ventaRepartidor.getVuelta1() != null) {
                    if (!ventaRepartidor.getVuelta1().isRegistrada()) {
                        txtPrimerVuelta.setText("Sin registrar");
                        txtEstadoUno.setVisibility(View.GONE);
                        btnPrimero.setVisibility(View.VISIBLE);
                    } else {
                        btnPrimero.setVisibility(View.GONE);
                        txtEstadoUno.setVisibility(View.VISIBLE);
                        String text = "";
                        if (ventaRepartidor.getVuelta1().getTortillas() > 0) {
                            text += "\t\tTortillas: " + ventaRepartidor.getVuelta1().getTortillas() + " kgs.\n";
                        }
                        if (ventaRepartidor.getVuelta1().getMasa() > 0) {
                            text += "\t\tMasa: " + ventaRepartidor.getVuelta1().getMasa() + " kgs.\n";
                        }
                        if (ventaRepartidor.getVuelta1().getTotopos() > 0) {
                            text += "\t\tTotopos: " + ventaRepartidor.getVuelta1().getTotopos() + " kgs.\n";
                        }
                        txtPrimerVuelta.setText(text);
                        if (ventaRepartidor.getVuelta1().isConfirmado()) {
                            txtEstadoUno.setText("Confirmado");
                        } else {
                            txtEstadoUno.setText("Esperando confirmación");
                        }
                    }
                }
                if (ventaRepartidor.getVuelta2() != null) {
                    if (!ventaRepartidor.getVuelta2().isRegistrada()) {
                        txtSegundaVuelta.setText("Sin registrar");
                        btnSegundo.setVisibility(View.VISIBLE);
                        txtEstadoDos.setVisibility(View.GONE);
                    } else {
                        btnSegundo.setVisibility(View.GONE);
                        txtEstadoDos.setVisibility(View.VISIBLE);
                        String text = "";
                        if (ventaRepartidor.getVuelta2().getTortillas() > 0) {
                            text += "\t\tTortillas: " +  ventaRepartidor.getVuelta2().getTortillas() + " kgs.\n";
                        }
                        if (ventaRepartidor.getVuelta2().getMasa() > 0) {
                            text += "\t\tMasa: " +  ventaRepartidor.getVuelta2().getMasa() + " kgs.\n";
                        }
                        if (ventaRepartidor.getVuelta2().getTotopos() > 0) {
                            text += "\t\tTotopos: " +  ventaRepartidor.getVuelta2().getTotopos() + " kgs.\n";
                        }
                        txtSegundaVuelta.setText(text);
                        if (ventaRepartidor.getVuelta2().isConfirmado()) {
                            txtEstadoDos.setText("Confirmado");
                        } else {
                            txtEstadoDos.setText("Esperando confirmación");
                        }
                    }
                } else {
                    lytSegundo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //TODO obtiene el nombre de la sucursal por primera vez
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

        //TODO muestra la cantidad de conceptos que se muestra en la seccion de ventas extra (gastos, especificar en qué)
        refGastos = FirebaseDatabase.getInstance().getReference("Gastos")
                .child(empleado.getIdEmpleado())
                .child(idVenta);
        listenerGastos = refGastos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    txtGasto.setText("");
                } else {
                    double total = 0.0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Concepto concepto = snapshot.getValue(Concepto.class);
                        total += concepto.getPrecio();
                    }
                    txtGasto.setText("$" + total);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //TODO muestra los clientes a los que repartira producto
        refClientes = FirebaseDatabase.getInstance().getReference("Clientes");
        listenerClientes = refClientes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Cliente> clientes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Cliente cliente = snapshot.getValue(Cliente.class);
                    if (!cliente.isEliminado())
                    {
                        clientes.add(cliente);
                    }
                }
                adaptador = new AdaptadorRepartidorClientes(getContext(),clientes, idVenta, getChildFragmentManager());
                recyclerView.setAdapter(adaptador);
                if (adaptador.getVentaClientes() == null && ventaClientes.size() > 0) {
                    adaptador.addVentas(ventaClientes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //TODO obtiene las ventas de cada cliente
        refAuxVenta = FirebaseDatabase.getInstance().getReference("AuxVentaRepartidor")
                .child(ControlSesiones.ObtenerUsuarioActivo(Objects.requireNonNull(getContext())))
                .child(idVenta);

        listenerAuxVenta = refAuxVenta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    txtVendidosUno.setVisibility(View.GONE);
                    txtTituloVendidoUno.setVisibility(View.GONE);
                    txtVendidosDos.setVisibility(View.GONE);
                    txtTituloVendidoDos.setVisibility(View.GONE);
                    if (ventaRepartidor != null) {
                        if (ventaRepartidor.getVuelta1() != null) {
                            adaptador.setTortillasVentaPrimerVuelta(ventaRepartidor.getVuelta1().getTortillas());
                            adaptador.setMasaVentaPrimerVuelta(ventaRepartidor.getVuelta1().getMasa());
                            adaptador.setTotoposVentaPrimerVuelta(ventaRepartidor.getVuelta1().getTotopos());
                        }
                        if (ventaRepartidor.getVuelta2() != null) {
                            adaptador.setTortillasVentaSegundaVuelta(ventaRepartidor.getVuelta2().getTortillas());
                            adaptador.setMasaVentaSegundaVuelta(ventaRepartidor.getVuelta2().getMasa());
                            adaptador.setTotoposVentaSegundaVuelta(ventaRepartidor.getVuelta2().getTotopos());
                        }
                    }
                    return;
                }
                ventaClientes.clear();
                double tortillaP = 0.0, masaP = 0.0, totoposP = 0.0;
                double tortillaS = 0.0, masaS = 0.0, totoposS = 0.0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    VentaCliente ventaCliente = snapshot.getValue(VentaCliente.class);
                    if (ventaCliente.getVuelta1() != null) {
                        if (ventaCliente.getVuelta1().getTortillas() >= 0) {
                            tortillaP += ventaCliente.getVuelta1().getTortillas();
                        }
                        if (ventaCliente.getVuelta1().getMasa() >= 0) {
                            masaP += ventaCliente.getVuelta1().getMasa();
                        }
                        if (ventaCliente.getVuelta1().getTotopos() >= 0) {
                            totoposP += ventaCliente.getVuelta1().getTotopos();
                        }
                    }
                    if (ventaCliente.getVuelta2() != null) {
                        if (ventaCliente.getVuelta2().getTortillas() >= 0) {
                            tortillaS += ventaCliente.getVuelta2().getTortillas();
                        }
                        if (ventaCliente.getVuelta2().getMasa() >= 0) {
                            masaS += ventaCliente.getVuelta2().getMasa();
                        }
                        if (ventaCliente.getVuelta2().getTotopos() >= 0) {
                            totoposS += ventaCliente.getVuelta2().getTotopos();
                        }
                    }
                    ventaClientes.add(ventaCliente);
                }
                String text = "";
                if (tortillaP == 0.0 && masaP == 0.0 && totoposP == 0.0) {
                    txtVendidosUno.setVisibility(View.GONE);
                    txtTituloVendidoUno.setVisibility(View.GONE);
                } else {
                    if (tortillaP > 0.0) {
                        text += "\t\tTortillas: " + tortillaP + " kgs.\n";
                        if (adaptador != null && ventaRepartidor != null && ventaRepartidor.getVuelta1() != null) {
                            adaptador.setTortillasVentaPrimerVuelta(ventaRepartidor.getVuelta1().getTortillas() - tortillaP);
                        }
                    } else {
                        if (adaptador != null && ventaRepartidor != null && ventaRepartidor.getVuelta1() != null) {
                            adaptador.setTortillasVentaPrimerVuelta(ventaRepartidor.getVuelta1().getTortillas());
                        }
                    }
                    if (masaP > 0.0) {
                        text += "\t\tMasa: " + masaP + " kgs.\n";
                        if (adaptador != null && ventaRepartidor != null && ventaRepartidor.getVuelta1() != null) {
                            adaptador.setMasaVentaPrimerVuelta(ventaRepartidor.getVuelta1().getMasa() - masaP);
                        }
                    } else {
                        if (adaptador != null && ventaRepartidor != null && ventaRepartidor.getVuelta1() != null) {
                            adaptador.setMasaVentaPrimerVuelta(ventaRepartidor.getVuelta1().getMasa());
                        }
                    }
                    if (totoposP > 0.0) {
                        text += "\t\tTotopos: " + totoposP + " kgs.\n";
                        if (adaptador != null && ventaRepartidor != null && ventaRepartidor.getVuelta1() != null) {
                            adaptador.setTotoposVentaPrimerVuelta(ventaRepartidor.getVuelta1().getTotopos() - totoposP);
                        }
                    } else {
                        if (adaptador != null && ventaRepartidor != null && ventaRepartidor.getVuelta1() != null) {
                            adaptador.setTotoposVentaPrimerVuelta(ventaRepartidor.getVuelta1().getTotopos());
                        }
                    }
                    txtVendidosUno.setVisibility(View.VISIBLE);
                    txtTituloVendidoUno.setVisibility(View.VISIBLE);
                    txtVendidosUno.setText(text);
                }
                text = "";
                if (tortillaS == 0.0 && masaS == 0.0 && totoposS == 0) {
                    txtVendidosDos.setVisibility(View.GONE);
                    txtTituloVendidoDos.setVisibility(View.GONE);
                } else {
                    if (tortillaS > 0.0) {
                        text += "\t\tTortillas: " + tortillaS + " kgs.\n";
                        if (adaptador != null && ventaRepartidor != null && ventaRepartidor.getVuelta2() != null) {
                            adaptador.setTortillasVentaSegundaVuelta(ventaRepartidor.getVuelta2().getTortillas() - tortillaS);
                        }
                    } else {
                        if (adaptador != null && ventaRepartidor != null && ventaRepartidor.getVuelta2() != null) {
                            adaptador.setTortillasVentaSegundaVuelta(ventaRepartidor.getVuelta2().getTortillas());
                        }
                    }
                    if (masaS > 0.0) {
                        text += "\t\tMasa: " + masaS + " kgs.\n";
                        if (adaptador != null && ventaRepartidor != null && ventaRepartidor.getVuelta2() != null) {
                            adaptador.setMasaVentaSegundaVuelta(ventaRepartidor.getVuelta2().getMasa() - masaS);
                        }
                    } else {
                        if (adaptador != null && ventaRepartidor != null && ventaRepartidor.getVuelta2() != null) {
                            adaptador.setMasaVentaSegundaVuelta(ventaRepartidor.getVuelta2().getMasa());
                        }
                    }
                    if (totoposS > 0.0) {
                        text += "\t\tTotopos: " + totoposS + " kgs.\n";
                        if (adaptador != null && ventaRepartidor != null && ventaRepartidor.getVuelta2() != null) {
                            adaptador.setTotoposVentaSegundaVuelta(ventaRepartidor.getVuelta2().getTotopos() - totoposS);
                        }
                    } else {
                        if (adaptador != null && ventaRepartidor != null && ventaRepartidor.getVuelta2() != null) {
                            adaptador.setTotoposVentaSegundaVuelta(ventaRepartidor.getVuelta2().getTotopos());
                        }
                    }
                    txtVendidosDos.setVisibility(View.VISIBLE);
                    txtTituloVendidoDos.setVisibility(View.VISIBLE);
                    txtVendidosDos.setText(text);
                }
                if (adaptador != null) {
                    adaptador.addVentas(ventaClientes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ((MaterialButton) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    if (adaptador != null) {
                        for (VentaCliente ventaCliente : adaptador.getPagos()) {
                            FirebaseDatabase.getInstance().getReference("AuxVentaRepartidor")
                                    .child(empleado.getIdEmpleado())
                                    .child(idVenta)
                                    .child(ventaCliente.getIdCliente())
                                    .child("pago")
                                    .setValue(ventaCliente.getPago());
                        }
                        dismiss();
                    }
                });

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
    public void onDestroy() {
        super.onDestroy();
        refVenta.removeEventListener(listenerVenta);
        refAuxVenta.removeEventListener(listenerAuxVenta);
        refClientes.removeEventListener(listenerClientes);
        refGastos.removeEventListener(listenerGastos);
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}