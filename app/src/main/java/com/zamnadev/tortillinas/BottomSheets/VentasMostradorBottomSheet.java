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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorRepartidoresVenta;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorVentasExtras;
import com.zamnadev.tortillinas.Dialogos.DialogoAddCampoVentas;
import com.zamnadev.tortillinas.Dialogos.DialogoMaizCocido;
import com.zamnadev.tortillinas.Dialogos.DialogoVentaRepartidor;
import com.zamnadev.tortillinas.MainActivity;
import com.zamnadev.tortillinas.Moldes.AuxVenta;
import com.zamnadev.tortillinas.Moldes.Concepto;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaCliente;
import com.zamnadev.tortillinas.Moldes.VentaDelDia;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.Moldes.Vuelta;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class VentasMostradorBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private Empleado empleado;
    private String idSucursal;
    private boolean isEditable;
    private String idVenta;

    private DatabaseReference refVenta;
    private DatabaseReference refGastos;
    private DatabaseReference refVentaMostrador;
    private DatabaseReference refProductos;
    private DatabaseReference refVentasDia;
    private ValueEventListener listenerVenta;
    private ValueEventListener listenerGastos;
    private ValueEventListener listenerVentaMostrador;
    private ValueEventListener listenerProductos;
    private ValueEventListener listenerVentasDia;

    private TextInputEditText txtPrimerVueltaT;
    private TextInputEditText txtPrimerVueltaM;
    private TextInputEditText txtPrimerVueltaTo;
    private TextInputEditText txtSegundaVueltaT;
    private TextInputEditText txtSegundaVueltaM;
    private TextInputEditText txtSegundaVueltaTo;

    private AdaptadorVentasExtras adaptadorVentasExtra;

    private ImageButton btnRepartidores;

    private VentaMostrador venta;

    private int controladorRepartidores;

    private RecyclerView recyclerViewRepartidores;

    private Sucursal sucursal;

    private Double masaDevolucion;
    private Double tortillaDevolucion;
    private Double totoposDevolucion;

    public VentasMostradorBottomSheet(String  idVenta, Empleado empleado, String idSucursal, boolean isEditable)
    {
        this.idVenta = idVenta;
        this.empleado = empleado;
        this.idSucursal = idSucursal;
        this.isEditable = isEditable;
        masaDevolucion = 0.0;
        tortillaDevolucion = 0.0;
        totoposDevolucion = 0.0;
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_ventas_mostrador_bottom_sheet, null);
        bottomSheet.setContentView(view);

        txtPrimerVueltaT = view.findViewById(R.id.txtPrimerVueltaTortilla);
        txtPrimerVueltaM = view.findViewById(R.id.txtPrimerVueltaMasa);
        txtPrimerVueltaTo = view.findViewById(R.id.txtPrimerVueltaTotopos);
        txtSegundaVueltaT = view.findViewById(R.id.txtSegundaVueltaTortilla);
        txtSegundaVueltaM = view.findViewById(R.id.txtSegundaVueltaMasa);
        txtSegundaVueltaTo = view.findViewById(R.id.txtSegundaVueltaTotopos);

        TextInputEditText txtGastos = view.findViewById(R.id.txtGastos);
        TextInputEditText txtVentasMostrador = view.findViewById(R.id.txtVentasMostrador);

        TextInputEditText txtMaizCocido = view.findViewById(R.id.txtMaizCocido);

        btnRepartidores = view.findViewById(R.id.btnMostrarRepartidores);

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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        ImageButton btnCerrar = view.findViewById(R.id.btn_cerrar);
        btnCerrar.setOnClickListener((v -> dismiss()));

        TextInputEditText txtFecha = view.findViewById(R.id.txt_fecha);
        TextInputEditText txtDomicilio = view.findViewById(R.id.txtDomicilio);
        TextInputEditText txtNixtamalSobra = view.findViewById(R.id.txtNixtamalSobra);
        TextInputEditText txtMermaMolino = view.findViewById(R.id.txtMermasMolino);
        TextInputEditText txtMermaMaquina = view.findViewById(R.id.txtMermaMaquina);
        TextInputEditText txtMermaTortilla = view.findViewById(R.id.txtMerMaTortilla);
        TextInputEditText txtMasaVendia = view.findViewById(R.id.txtMasaVendida);
        TextInputEditText txtTortillaSobra = view.findViewById(R.id.txtTortillaSobra);
        TextInputEditText txtDevoluciones = view.findViewById(R.id.txtDevoluciones);

        TextView txtSucursal = view.findViewById(R.id.txtSucursal);

        recyclerViewRepartidores = view.findViewById(R.id.recyclerviewRepartidores);
        recyclerViewRepartidores.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRepartidores.setHasFixedSize(true);

        String fecha = MainActivity.getFecha();

        if (!isEditable) {
            hideTxt(txtNixtamalSobra);
            hideTxt(txtMasaVendia);
            hideTxt(txtTortillaSobra);
            hideTxt(txtMermaMaquina);
            hideTxt(txtMermaMolino);
            hideTxt(txtMermaTortilla);
            ((ImageButton) view.findViewById(R.id.btnAddRepartidor))
                    .setVisibility(View.GONE);
            ((MaterialButton) view.findViewById(R.id.btnGuardar))
                    .setText("CERRAR");
        }

        //TODO muestra los datos de la venta
        refVenta = FirebaseDatabase.getInstance().getReference("VentasMostrador")
                .child(empleado.getIdEmpleado())
                .child(idVenta);
        listenerVenta = refVenta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                venta = dataSnapshot.getValue(VentaMostrador.class);
                txtFecha.setText(venta.getFecha());

                String text = "";
                if (venta.getCostales() >= 0.0) {
                    text += venta.getCostales() + " costales ";
                }
                if (venta.getBotes() >= 0.0) {
                    text += ", + " + venta.getBotes() +  " botes";
                }
                txtMaizCocido.setText(text);

                if (venta.getMaizNixtamalizado() >= 0.0) {
                    txtNixtamalSobra.setText("" + venta.getMaizNixtamalizado());
                }
                if (venta.getMaquinaMasa() >= 0.0) {
                    txtMermaMaquina.setText("" + venta.getMaquinaMasa());
                }
                if (venta.getMolino() >= 0.0) {
                    txtMermaMolino.setText("" + venta.getMolino());
                }
                if (venta.getMermaTortilla() >= 0.0) {
                    txtMermaTortilla.setText("" + venta.getMermaTortilla());
                }
                if (venta.getTortillaSobra() >= 0.0) {
                    txtTortillaSobra.setText("" + venta.getTortillaSobra());
                }
                if (venta.getMasaVendida() >= 0.0) {
                    txtMasaVendia.setText("" + venta.getMasaVendida());
                }

                if (venta.getRepartidores().get("repartidor0").equals("null")) {
                    btnRepartidores.setVisibility(View.GONE);
                } else {
                    btnRepartidores.setVisibility(View.VISIBLE);
                    controladorRepartidores = -1;
                    mostrarVentasRepartidor();
                    mostrarRepartidores();
                }

                txtMaizCocido.setOnClickListener(view1 -> {
                    DialogoMaizCocido dialogoMaizCocido = new DialogoMaizCocido(getContext(),idVenta,venta.getCostales(),venta.getBotes(),isEditable);
                    dialogoMaizCocido.show(getChildFragmentManager(),dialogoMaizCocido.getTag());
                });

                //TODO Mostrar las devoluciones totales
                if (venta.getRepartidores().size() > 0 && !(venta.getRepartidores().get("repartidor0").equals("null"))) {
                    masaDevolucion = 0.0;
                    tortillaDevolucion = 0.0;
                    totoposDevolucion = 0.0;
                    for (int x = 0; x < venta.getRepartidores().size(); x++) {
                        FirebaseDatabase.getInstance().getReference("AuxVentaRepartidor")
                                .child(venta.getRepartidores().get("repartidor"+x))
                                .child(idVenta)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                VentaCliente cliente = snapshot.getValue(VentaCliente.class);
                                                if (cliente.getDevolucion() != null) {
                                                    if (cliente.getDevolucion().getMasa() >= 0.0) {
                                                        masaDevolucion += cliente.getDevolucion().getMasa();
                                                    }
                                                    if (cliente.getDevolucion().getTotopos() >= 0.0) {
                                                        totoposDevolucion += cliente.getDevolucion().getTotopos();
                                                    }
                                                    if (cliente.getDevolucion().getTortillas() >= 0.0) {
                                                        tortillaDevolucion += cliente.getDevolucion().getTortillas();
                                                    }
                                                }
                                            }
                                            if (tortillaDevolucion == 0.0 && masaDevolucion == 0.0 && totoposDevolucion == 0.0) {
                                                txtDevoluciones.setText("Ninguna devolución");
                                            } else {
                                                txtDevoluciones.setOnClickListener(view1 -> {
                                                    DevolucionBottomSheet devolucionBottomSheet = new DevolucionBottomSheet(
                                                            tortillaDevolucion,
                                                            masaDevolucion,
                                                            totoposDevolucion
                                                    );
                                                    devolucionBottomSheet.show(
                                                            getChildFragmentManager(),
                                                            devolucionBottomSheet.getTag()
                                                    );
                                                });
                                                String text = "";
                                                if (tortillaDevolucion > 0) {
                                                    text += ", " + tortillaDevolucion;
                                                }
                                                if (masaDevolucion > 0) {
                                                    text += ", " + masaDevolucion;
                                                }
                                                if (totoposDevolucion > 0) {
                                                    text += ", " + totoposDevolucion;
                                                }
                                                text = text.replaceFirst(",","");
                                                text = text.replaceFirst(" ","");
                                                txtDevoluciones.setText(text);
                                            }
                                        } else {
                                            txtDevoluciones.setText("Ninguna devolución");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txtGastos.setOnClickListener(view12 -> {
            VentasAdicionalesBottomSheet ventas = new VentasAdicionalesBottomSheet(VentasAdicionalesBottomSheet.TIPO_GASTOS,idVenta,isEditable);
            ventas.show(getChildFragmentManager(),ventas.getTag());
        });

        txtVentasMostrador.setOnClickListener(view12 -> {
            VentasAdicionalesBottomSheet ventas = new VentasAdicionalesBottomSheet(VentasAdicionalesBottomSheet.TIPO_VENTAS_MOSTRADOR,idVenta,isEditable);
            ventas.show(getChildFragmentManager(),ventas.getTag());
        });

        //TODO muestra la cantidad de conceptos que se muestra en la seccion de ventas extra (gastos, especificar en qué)
        refGastos = FirebaseDatabase.getInstance().getReference("Gastos")
                .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                .child(idVenta);
        listenerGastos = refGastos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    txtGastos.setText("");
                } else {
                    double total = 0.0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Concepto concepto = snapshot.getValue(Concepto.class);
                        total += concepto.getPrecio();
                    }
                    txtGastos.setText("$" + total);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //TODO muestra la cantidad de conceptos alamcenados en las cventas de mostrador
        refVentaMostrador = FirebaseDatabase.getInstance().getReference("Mostrador")
                .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                .child(idVenta);
        listenerVentaMostrador = refVentaMostrador.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    txtVentasMostrador.setText("");
                } else {
                    double total = 0.0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Concepto concepto = snapshot.getValue(Concepto.class);
                        total += concepto.getPrecio();
                    }
                    txtVentasMostrador.setText("$" + total);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //TODO muestra los productos de la seccion de ventas extra
        if (isEditable) {
            refProductos = FirebaseDatabase.getInstance().getReference("Productos");
            listenerProductos = refProductos.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        ArrayList<Producto> productos = new ArrayList<>();
                        ArrayList<VentaDelDia> ventaDelDia = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Producto producto = snapshot.getValue(Producto.class);
                            if (!producto.isEliminado() && producto.isFormulario()) {
                                ventaDelDia.add(new VentaDelDia(producto.getIdProducto(),0));
                                productos.add(producto);
                            }
                        }
                        adaptadorVentasExtra = new AdaptadorVentasExtras(getContext(),productos,ventaDelDia,getMe(),isEditable);
                        recyclerView.setAdapter(adaptadorVentasExtra);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        //TODO muestra por primera vez los datos de la sucursal
        DatabaseReference refSucursal = FirebaseDatabase.getInstance().getReference("Sucursales")
                .child(idSucursal);
        refSucursal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sucursal = dataSnapshot.getValue(Sucursal.class);
                txtSucursal.setText(sucursal.getNombre());
                txtDomicilio.setText(sucursal.getDireccion().toRecyclerView());
                mostrarRepartidores();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //TODO muestra la cantidad de conceptos alamcenados en las cventas de mostrador
        refVentasDia = FirebaseDatabase.getInstance().getReference("VentasDelDia")
                .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                .child(idVenta);
        listenerVentasDia = refVentasDia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<VentaDelDia> productosMostrador = new ArrayList<>();
                    ArrayList<Producto> productos = new ArrayList<>();
                    ArrayList<VentaDelDia> ventaDelDia = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        VentaDelDia venta = snapshot.getValue(VentaDelDia.class);
                        productosMostrador.add(venta);
                        if (!isEditable) {
                            FirebaseDatabase.getInstance().getReference("Productos")
                                    .child(venta.getIdProducto())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snp) {
                                            Producto producto = snp.getValue(Producto.class);
                                            ventaDelDia.add(new VentaDelDia(producto.getIdProducto(),0));
                                            productos.add(producto);
                                            if (!isEditable) {
                                                recyclerView.setVisibility(View.VISIBLE);
                                                adaptadorVentasExtra = new AdaptadorVentasExtras(getContext(),productos,ventaDelDia,getMe(),isEditable);
                                                recyclerView.setAdapter(adaptadorVentasExtra);
                                            }
                                            if (adaptadorVentasExtra != null) {
                                                adaptadorVentasExtra.addVenta(productosMostrador);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                            }
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //TODO Guarda la informacion de la venta
        ((MaterialButton) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    if (!isEditable) {
                        dismiss();
                        return;
                    }
                    HashMap<String,Object> hashMap = new HashMap<>();
                    if (!txtNixtamalSobra.getText().toString().isEmpty()) {
                        hashMap.put("maizNixtamalizado",Integer.parseInt(txtNixtamalSobra.getText().toString()));
                    }
                    if (!txtMermaMolino.getText().toString().isEmpty()) {
                        hashMap.put("molino",Double.parseDouble(txtMermaMolino.getText().toString()));
                    }
                    if (!txtMermaTortilla.getText().toString().isEmpty()) {
                        hashMap.put("mermaTortilla",Double.parseDouble(txtMermaTortilla.getText().toString()));
                    }
                    if (!txtMermaMaquina.getText().toString().isEmpty()) {
                        hashMap.put("maquinaMasa",Double.parseDouble(txtMermaMaquina.getText().toString()));
                    }
                    if (!txtMasaVendia.getText().toString().isEmpty()) {
                        hashMap.put("masaVendida",Double.parseDouble(txtMasaVendia.getText().toString()));
                    }
                    if (!txtTortillaSobra.getText().toString().isEmpty()) {
                        hashMap.put("tortillaSobra",Double.parseDouble(txtTortillaSobra.getText().toString()));
                    }
                    if (hashMap.size() > 0) {
                        FirebaseDatabase.getInstance().getReference("VentasMostrador")
                                .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                                .child(idVenta)
                                .updateChildren(hashMap);
                    }
                    if (adaptadorVentasExtra != null) {
                        for (VentaDelDia ventaDelDia : adaptadorVentasExtra.getVentasMotrador()) {
                            HashMap<String, Object> productoMap = new HashMap<>();
                            productoMap.put("idProducto",ventaDelDia.getIdProducto());
                            productoMap.put("cantidad",ventaDelDia.getCantidad());
                            FirebaseDatabase.getInstance().getReference("VentasDelDia")
                                    .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                                    .child(idVenta)
                                    .child(ventaDelDia.getIdProducto())
                                    .updateChildren(productoMap);
                        }
                    }
                    dismiss();
                });

        //TODO añadir un repartidor a la venta
        ((ImageButton) view.findViewById(R.id.btnAddRepartidor))
                .setOnClickListener(view1 -> {
                    if (venta != null) {
                        DialogoVentaRepartidor dialogoVentaRepartidor = new DialogoVentaRepartidor(getMe(),idSucursal,venta);
                        dialogoVentaRepartidor.show(getChildFragmentManager(),dialogoVentaRepartidor.getTag());
                    }
                });

        //TODO muestra  y/o oculta la lista de repartidores
        btnRepartidores.setOnClickListener(view1 -> {
            if (venta != null) {
                if (controladorRepartidores > 0) {
                    controladorRepartidores *= -1;
                    btnRepartidores.setImageResource(R.drawable.ic_arrow_down_24dp);
                    recyclerViewRepartidores.setVisibility(View.GONE);
                } else {
                    controladorRepartidores *= -1;
                    btnRepartidores.setImageResource(R.drawable.ic_arrow_up_24dp);
                    recyclerViewRepartidores.setVisibility(View.VISIBLE);
                }
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

    //TODO muestra los repartidores en el recyclerview
    private void mostrarRepartidores() {
        if (sucursal == null) {
            return;
        }
        if (venta != null) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (int x = 0; x < venta.getRepartidores().size(); x++) {
                arrayList.add(venta.getRepartidores().get("repartidor"+x));
            }
            AdaptadorRepartidoresVenta adaptador = new AdaptadorRepartidoresVenta(getContext(),arrayList,getChildFragmentManager(),idVenta,sucursal.getNombre(),isEditable);
            recyclerViewRepartidores.setAdapter(adaptador);
        }
    }

    //TODO muestra la sumatoria de las ventas de los repartidores
    private void mostrarVentasRepartidor() {
        if (venta != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                    .child(idVenta);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double masaUno = 0.0, tortillasUno = 0.0, totoposUno = 0.0;
                    double masaDos = 0.0, tortillasDos = 0.0, totoposDos = 0.0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        AuxVenta auxVenta = snapshot.getValue(AuxVenta.class);
                        if (auxVenta.getVuelta1().isConfirmado()) {
                            if (auxVenta.getVuelta1().getMasa() > 0) {
                                masaUno += auxVenta.getVuelta1().getMasa();
                            }
                            if (auxVenta.getVuelta1().getTortillas() > 0) {
                                tortillasUno += auxVenta.getVuelta1().getTortillas();
                            }
                            if (auxVenta.getVuelta1().getTotopos() > 0 ) {
                                totoposUno += auxVenta.getVuelta1().getTotopos();
                            }
                        }
                        if (auxVenta.getVuelta2().isConfirmado()) {
                            if (auxVenta.getVuelta2().getMasa() > 0) {
                                masaDos += auxVenta.getVuelta2().getMasa();
                            }
                            if (auxVenta.getVuelta2().getTortillas() > 0) {
                                tortillasDos += auxVenta.getVuelta2().getTortillas();
                            }
                            if (auxVenta.getVuelta2().getTotopos() > 0) {
                                totoposDos += auxVenta.getVuelta2().getTotopos();
                            }
                        }
                    }
                    txtPrimerVueltaM.setText("" + masaUno);
                    txtPrimerVueltaT.setText("" + tortillasUno);
                    txtPrimerVueltaTo.setText("" + totoposUno);
                    txtSegundaVueltaM.setText("" + masaDos);
                    txtSegundaVueltaT.setText("" + tortillasDos);
                    txtSegundaVueltaTo.setText("" + totoposDos);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    //TODO añade un repartidor a la venta, desde el dialogo
    public void addRepartidor(String idRepartidor) {
        if (venta != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AuxVentaMostrador");
            HashMap<String, Object> hashMap = new HashMap<>();
            Vuelta vuelta = new Vuelta();
            vuelta.setRegistrada(false);
            vuelta.setMasa(-1.0);
            vuelta.setTotopos(-1.0);
            vuelta.setTortillas(-1.0);
            vuelta.setHora(-1);
            hashMap.put("vuelta1",vuelta);
            hashMap.put("vuelta2",vuelta);
            reference.child(venta.getIdVenta()).child(idRepartidor).updateChildren(hashMap);
            HashMap<String, String> repartidores = new HashMap<>();
            if (venta.getRepartidores().get("repartidor0").equals("null")) {
                repartidores.put("repartidor"+repartidores.size(),idRepartidor);
            } else {
                repartidores = venta.getRepartidores();
                repartidores.put("repartidor"+repartidores.size(),idRepartidor);
            }
            hashMap.clear();
            hashMap.put("repartidores",repartidores);
            FirebaseDatabase.getInstance().getReference("VentasMostrador")
                    .child(empleado.getIdEmpleado())
                    .child(idVenta)
                    .updateChildren(hashMap);
        } else {
            Toast.makeText(getContext(), "Ha ocurrido un error, intentelo más tarde", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideTxt(TextInputEditText txt) {
       txt.setClickable(false);
       txt.setLongClickable(false);
       txt.setFocusable(false);
       txt.setCursorVisible(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refVenta.removeEventListener(listenerVenta);
        refGastos.removeEventListener(listenerGastos);
        refVentaMostrador.removeEventListener(listenerVentaMostrador);
        if (isEditable) {
            refProductos.removeEventListener(listenerProductos);
        }
        refVentasDia.removeEventListener(listenerVentasDia);
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private VentasMostradorBottomSheet getMe() {
        return this;
    }
}