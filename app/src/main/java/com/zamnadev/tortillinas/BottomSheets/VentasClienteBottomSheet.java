package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.VentaCliente;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.util.HashMap;
import java.util.Objects;
import java.util.StringTokenizer;

public class VentasClienteBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private boolean primero;
    private Cliente cliente;
    private VentaRepartidor ventaRepartidor;
    private VentaCliente ventaCliente;
    private boolean devolucion;

    public VentasClienteBottomSheet(boolean primero, Cliente cliente, VentaRepartidor ventaRepartidor, VentaCliente ventaCliente) {
        this.primero = primero;
        this.cliente = cliente;
        this.ventaRepartidor = ventaRepartidor;
        this.ventaCliente = ventaCliente;
        devolucion = false;
    }

    public VentasClienteBottomSheet(boolean primero, Cliente cliente, VentaRepartidor ventaRepartidor, VentaCliente ventaCliente, boolean devolucion) {
        this.primero = primero;
        this.cliente = cliente;
        this.ventaRepartidor = ventaRepartidor;
        this.ventaCliente = ventaCliente;
        this.devolucion = devolucion;
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_ventas_cliente_bottom_sheet, null);
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (BottomSheetBehavior.STATE_HIDDEN == i) dismiss();
                if (BottomSheetBehavior.STATE_DRAGGING == i) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });

        TextInputEditText txtTortilla = view.findViewById(R.id.txtTortilla);
        TextInputEditText txtMasa = view.findViewById(R.id.txtMasa);
        TextInputEditText txtTotopos = view.findViewById(R.id.txtTotopos);

        if (ventaCliente != null) {
            if (devolucion) {
                if (ventaCliente.getDevolucion() != null) {
                    if (ventaCliente.getDevolucion().getTortillas() >= 0) {
                        txtTortilla.setText("" + ventaCliente.getDevolucion().getTortillas());
                    }
                    if (ventaCliente.getDevolucion().getTotopos() >= 0) {
                        txtTotopos.setText("" + ventaCliente.getDevolucion().getTotopos());
                    }
                    if (ventaCliente.getDevolucion().getMasa() >= 0) {
                        txtMasa.setText("" + ventaCliente.getDevolucion().getMasa());
                    }
                }
            } else if (primero) {
                if (ventaCliente.getVuelta1() != null) {
                    if (ventaCliente.getVuelta1().getTortillas() >= 0) {
                        txtTortilla.setText("" + ventaCliente.getVuelta1().getTortillas());
                    }
                    if (ventaCliente.getVuelta1().getTotopos() >= 0) {
                        txtTotopos.setText("" + ventaCliente.getVuelta1().getTotopos());
                    }
                    if (ventaCliente.getVuelta1().getMasa() >= 0) {
                        txtMasa.setText("" + ventaCliente.getVuelta1().getMasa());
                    }
                }
            } else {
                if (ventaCliente.getVuelta2() != null) {
                    if (ventaCliente.getVuelta2().getTortillas() >= 0) {
                        txtTortilla.setText("" + ventaCliente.getVuelta2().getTortillas());
                    }
                    if (ventaCliente.getVuelta2().getTotopos() >= 0) {
                        txtTotopos.setText("" + ventaCliente.getVuelta2().getTotopos());
                    }
                    if (ventaCliente.getVuelta2().getMasa() >= 0) {
                        txtMasa.setText("" + ventaCliente.getVuelta2().getMasa());
                    }
                }
            }
        }

        if (devolucion) {
            ((TextView) view.findViewById(R.id.txtTitulo))
                    .setText("Devolución");
            if (ventaRepartidor.getVuelta1().getMasa() < 0 && ventaRepartidor.getVuelta2().getMasa() < 0) {
                txtMasa.setVisibility(View.GONE);
            }
            if (ventaRepartidor.getVuelta1().getTortillas() < 0 && ventaRepartidor.getVuelta2().getTortillas() < 0) {
                txtTortilla.setVisibility(View.GONE);
            }
            if (ventaRepartidor.getVuelta1().getTotopos() < 0 && ventaRepartidor.getVuelta2().getTotopos() < 0) {
                txtTotopos.setVisibility(View.GONE);
            }
        } else if (primero) {
            ((TextView) view.findViewById(R.id.txtTitulo))
                    .setText("Primer vuelta");
            if (ventaRepartidor.getVuelta1().getMasa() < 0) {
                txtMasa.setVisibility(View.GONE);
            }
            if (ventaRepartidor.getVuelta1().getTortillas() < 0) {
                txtTortilla.setVisibility(View.GONE);
            }
            if (ventaRepartidor.getVuelta1().getTotopos() < 0) {
                txtTotopos.setVisibility(View.GONE);
            }
        } else {
            ((TextView) view.findViewById(R.id.txtTitulo))
                    .setText("Segunda vuelta");
            if (ventaRepartidor.getVuelta2().getMasa() < 0) {
                txtMasa.setVisibility(View.GONE);
            }
            if (ventaRepartidor.getVuelta2().getTortillas() < 0) {
                txtTortilla.setVisibility(View.GONE);
            }
            if (ventaRepartidor.getVuelta2().getTotopos() < 0) {
                txtTotopos.setVisibility(View.GONE);
            }
        }


        ((TextView) view.findViewById(R.id.txtSubTitulo))
                .setText(cliente.getNombre().getNombres() + cliente.getNombre().getApellidos());

        ((MaterialButton) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    double masa = -1, tortilla = -1, totopos = -1;
                    if (!txtMasa.getText().toString().isEmpty()) {
                        masa = Double.parseDouble(txtMasa.getText().toString());
                    }
                    if (!txtTortilla.getText().toString().isEmpty()) {
                        tortilla = Double.parseDouble(txtTortilla.getText().toString());
                    }
                    if (!txtTotopos.getText().toString().isEmpty()) {
                        totopos = Double.parseDouble(txtTotopos.getText().toString());
                    }
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("idCliente",cliente.getIdCliente());
                    HashMap<String, Object> vueltaMap = new HashMap<>();
                    vueltaMap.put("totopos",totopos);
                    vueltaMap.put("masa",masa);
                    vueltaMap.put("tortillas",tortilla);
                    DatabaseReference ref =  FirebaseDatabase.getInstance().getReference("AuxVentaRepartidor")
                            .child(ventaRepartidor.getIdEmpleado())
                            .child(ventaRepartidor.getIdVenta())
                            .child(cliente.getIdCliente());
                    if (devolucion) {
                        hashMap.put("devolucion",vueltaMap);
                    } else if (primero) {
                        hashMap.put("vuelta1",vueltaMap);
                    } else {
                        hashMap.put("vuelta2",vueltaMap);
                    }
                    double finalTortilla = tortilla;
                    double finalMasa = masa;
                    double finalTotopos = totopos;
                    ref.updateChildren(hashMap)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase.getInstance().getReference("Productos")
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    double tortillaVenta = 0.0, totoposVenta = 0.0, masaVenta = 0.0;
                                                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                                    {
                                                        Producto p = snapshot.getValue(Producto.class);
                                                        if (p.getNombre().toUpperCase().contains("TORTILLA")) {
                                                            if (cliente.isPreferencial()) {
                                                                for (int x = 0; x < cliente.getPrecios().size(); x++) {
                                                                    StringTokenizer a = new StringTokenizer(cliente.getPrecios().get("p"+x),"?");
                                                                    double precio = Double.parseDouble(a.nextToken());
                                                                    String id = a.nextToken();
                                                                    if (id.equals(p.getIdProducto())) {
                                                                        tortillaVenta = precio * finalTortilla;
                                                                    }
                                                                }
                                                            } else {
                                                                tortillaVenta = p.getPrecio() * finalTortilla;
                                                            }
                                                        }
                                                        if (p.getNombre().toUpperCase().contains("MASA")) {
                                                            if (cliente.isPreferencial()) {
                                                                for (int x = 0; x < cliente.getPrecios().size(); x++) {
                                                                    StringTokenizer a = new StringTokenizer(cliente.getPrecios().get("p"+x),"?");
                                                                    double precio = Double.parseDouble(a.nextToken());
                                                                    String id = a.nextToken();
                                                                    if (id.equals(p.getIdProducto())) {
                                                                        masaVenta = precio * finalMasa;
                                                                    }
                                                                }
                                                            } else {
                                                                masaVenta = p.getPrecio() * finalMasa;
                                                            }
                                                        }
                                                        if (p.getNombre().toUpperCase().contains("TOTOPO")) {
                                                            if (cliente.isPreferencial()) {
                                                                for (int x = 0; x < cliente.getPrecios().size(); x++) {
                                                                    StringTokenizer a = new StringTokenizer(cliente.getPrecios().get("p"+x),"?");
                                                                    double precio = Double.parseDouble(a.nextToken());
                                                                    String id = a.nextToken();
                                                                    if (id.equals(p.getIdProducto())) {
                                                                        totoposVenta = precio * finalTotopos;
                                                                    }
                                                                }
                                                            } else {
                                                                totoposVenta = p.getPrecio() * finalTotopos;
                                                            }
                                                        }
                                                        vueltaMap.put("totoposVenta",totoposVenta);
                                                        vueltaMap.put("masaVenta",masaVenta);
                                                        vueltaMap.put("tortillaVenta",tortillaVenta);
                                                        if (devolucion) {
                                                            hashMap.put("devolucion",vueltaMap);
                                                        } else if (primero) {
                                                            hashMap.put("vuelta1",vueltaMap);
                                                        } else {
                                                            hashMap.put("vuelta2",vueltaMap);
                                                        }
                                                        ref.updateChildren(hashMap);
                                                    }
                                                    dismiss();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                }
                            });
                });

        ((ImageButton) view.findViewById(R.id.btn_cerrar))
                .setOnClickListener(view1 ->  dismiss() );

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
}