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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorRepartidoresVenta;
import com.zamnadev.tortillinas.Dialogos.DialogoAddCampoVentas;
import com.zamnadev.tortillinas.Dialogos.DialogoVentaRepartidor;
import com.zamnadev.tortillinas.Moldes.AuxVenta;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
import com.zamnadev.tortillinas.Moldes.Vuelta;
import com.zamnadev.tortillinas.R;

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
    private ValueEventListener listenerVenta;

    private TextInputEditText txtPrimerVueltaT;
    private TextInputEditText txtPrimerVueltaM;
    private TextInputEditText txtSegundaVueltaT;
    private TextInputEditText txtSegundaVueltaM;

    private ImageButton btnRepartidores;

    private VentaMostrador venta;

    private int controladorRepartidores;

    private RecyclerView recyclerViewRepartidores;

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
        txtSegundaVueltaT = view.findViewById(R.id.txtSegundaVueltaTortilla);
        txtSegundaVueltaM = view.findViewById(R.id.txtSegundaVueltaMasa);

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

        //TODO muestra por primera vez los datos de la sucursal
        DatabaseReference refSucursal = FirebaseDatabase.getInstance().getReference("Sucursales")
                .child(idSucursal);
        refSucursal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Sucursal sucursal = dataSnapshot.getValue(Sucursal.class);
                txtSucursal.setText(sucursal.getNombre());
                txtDomicilio.setText(sucursal.getDireccion().toRecyclerView());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
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
        if (venta != null) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (int x = 0; x < venta.getRepartidores().size(); x++) {
                arrayList.add(venta.getRepartidores().get("repartidor"+x));
            }
            AdaptadorRepartidoresVenta adaptador = new AdaptadorRepartidoresVenta(getContext(),arrayList,getChildFragmentManager(),idVenta);
            recyclerViewRepartidores.setAdapter(adaptador);
        }
    }

    //TODO muestra la sumatoria de las ventas de los repartidores
    private void mostrarVentasRepartidor() {
        if (venta != null) {
            ArrayList<String> arrayList = new ArrayList<>();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                    .child(idVenta);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    double masaUno = 0.0, tortillasUno = 0.0;
                    double masaDos = 0.0, tortillasDos = 0.0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        AuxVenta auxVenta = snapshot.getValue(AuxVenta.class);
                        if (auxVenta.getVuelta1().isConfirmado()) {
                            masaUno += auxVenta.getVuelta1().getMasa();
                            tortillasUno += auxVenta.getVuelta1().getTortillas();
                        }
                        if (auxVenta.getVuelta2().isConfirmado()) {
                            masaDos += auxVenta.getVuelta2().getMasa();
                            tortillasDos += auxVenta.getVuelta2().getTortillas();
                        }
                    }
                    txtPrimerVueltaM.setText("" + masaUno);
                    txtPrimerVueltaT.setText("" + tortillasUno);
                    txtSegundaVueltaM.setText("" + masaDos);
                    txtSegundaVueltaT.setText("" + tortillasDos);
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