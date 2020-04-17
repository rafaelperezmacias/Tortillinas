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
import com.zamnadev.tortillinas.Dialogos.DialogoVentaRepartidor;
import com.zamnadev.tortillinas.Moldes.AuxVenta;
import com.zamnadev.tortillinas.Moldes.Concepto;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
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
    private ValueEventListener listenerVenta;
    private ValueEventListener listenerGastos;
    private ValueEventListener listenerVentaMostrador;
    private ValueEventListener listenerProductos;

    private TextInputEditText txtPrimerVueltaT;
    private TextInputEditText txtPrimerVueltaM;
    private TextInputEditText txtPrimerVueltaTo;
    private TextInputEditText txtSegundaVueltaT;
    private TextInputEditText txtSegundaVueltaM;
    private TextInputEditText txtSegundaVueltaTo;

    private ImageButton btnRepartidores;

    private VentaMostrador venta;

    private int controladorRepartidores;

    private RecyclerView recyclerViewRepartidores;

    private Sucursal sucursal;

    public VentasMostradorBottomSheet(String idVenta, Empleado empleado, String idSucursal)
    {
        this.idVenta = idVenta;
        this.empleado = empleado;
        this.idSucursal = idSucursal;
        isEditable = true;
    }

    public VentasMostradorBottomSheet(String  idVenta, Empleado empleado, String idSucursal, boolean isEditable)
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

        TextView txtSucursal = view.findViewById(R.id.txtSucursal);

        recyclerViewRepartidores = view.findViewById(R.id.recyclerviewRepartidores);
        recyclerViewRepartidores.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewRepartidores.setHasFixedSize(true);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.YEAR);
        calendar.set(Calendar.MONTH, Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fecha = sdf.format(calendar.getTime());

        //TODO muestra los datos de la venta
        refVenta = FirebaseDatabase.getInstance().getReference("VentasMostrador")
                .child(empleado.getIdEmpleado())
                .child(idVenta);
        listenerVenta = refVenta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                venta = dataSnapshot.getValue(VentaMostrador.class);
                txtFecha.setText(venta.getFecha());

                if (venta.getRepartidores().get("repartidor0").equals("null")) {
                    btnRepartidores.setVisibility(View.GONE);
                } else {
                    btnRepartidores.setVisibility(View.VISIBLE);
                    controladorRepartidores = -1;
                    mostrarVentasRepartidor();
                    mostrarRepartidores();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txtGastos.setOnClickListener(view12 -> {
            VentasAdicionalesBottomSheet ventas = new VentasAdicionalesBottomSheet(VentasAdicionalesBottomSheet.TIPO_GASTOS,idVenta);
            ventas.show(getChildFragmentManager(),ventas.getTag());
        });

        txtVentasMostrador.setOnClickListener(view12 -> {
            VentasAdicionalesBottomSheet ventas = new VentasAdicionalesBottomSheet(VentasAdicionalesBottomSheet.TIPO_VENTAS_MOSTRADOR,idVenta);
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
        refProductos = FirebaseDatabase.getInstance().getReference("Productos");
        listenerProductos = refProductos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    ArrayList<Producto> productos = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Producto producto = snapshot.getValue(Producto.class);
                        if (!producto.isEliminado() && producto.isFormulario()) {
                            productos.add(producto);
                        }
                    }
                    AdaptadorVentasExtras adaptador = new AdaptadorVentasExtras(getContext(),productos);
                    recyclerView.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

        //TODO Guarda la informacion de la venta
        ((MaterialButton) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {

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
            AdaptadorRepartidoresVenta adaptador = new AdaptadorRepartidoresVenta(getContext(),arrayList,getChildFragmentManager(),idVenta,sucursal.getNombre());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        refVenta.removeEventListener(listenerVenta);
        refGastos.removeEventListener(listenerGastos);
        refVentaMostrador.removeEventListener(listenerVentaMostrador);
        refProductos.removeEventListener(listenerProductos);
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