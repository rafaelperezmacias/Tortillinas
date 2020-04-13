package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

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
import com.zamnadev.tortillinas.Dialogos.DialogoVentaRepartidor;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
import com.zamnadev.tortillinas.R;

import java.text.SimpleDateFormat;
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

    private VentaMostrador venta;

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

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.YEAR);
        calendar.set(Calendar.MONTH, Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fecha = sdf.format(calendar.getTime());

        refVenta = FirebaseDatabase.getInstance().getReference("VentasMostrador")
                .child(empleado.getIdEmpleado())
                .child(idVenta);
        listenerVenta = refVenta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                venta = dataSnapshot.getValue(VentaMostrador.class);
                txtFecha.setText(venta.getFecha());

                if (venta.getRepartidores().get("repartidor0").equals("null")) {
                    primeraVuelta(false);
                    segundaVuelta(false);
                } else {
                    //showVentasRepartidor();
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
                txtDomicilio.setText(sucursal.getDireccion().toRecyclerView());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ((ImageButton) view.findViewById(R.id.btnAddRepartidor))
                .setOnClickListener(view1 -> {
                    if (venta != null) {
                        DialogoVentaRepartidor dialogoVentaRepartidor = new DialogoVentaRepartidor(getMe(),idSucursal,venta);
                        dialogoVentaRepartidor.show(getChildFragmentManager(),dialogoVentaRepartidor.getTag());
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

    public void addRepartidor(String idRepartidor) {
        //TODO Invocar otro dialogo para cantidad de masa y tortillas

        if (venta != null) {
            HashMap<String, String> repartidores = new HashMap<>();
            if (venta.getRepartidores().get("repartidor0").equals("null")) {
                repartidores.put("repartidor"+repartidores.size(),idRepartidor);
            } else {
                repartidores = venta.getRepartidores();
                repartidores.put("repartidor"+repartidores.size(),idRepartidor);
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("repartidores",repartidores);
            FirebaseDatabase.getInstance().getReference("VentasMostrador")
                    .child(empleado.getIdEmpleado())
                    .child(idVenta)
                    .updateChildren(hashMap);
            enviarNotificacion(idRepartidor);
        }
    }

    private void enviarNotificacion(String idRepartidor) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id",venta.getIdVenta());
        hashMap.put("idRepartidor",idRepartidor);
        hashMap.put("idMostrador",venta.getIdEmpleado());
        hashMap.put("visto",false);
        hashMap.put("confirmado",false);
        hashMap.put("hora", ServerValue.TIMESTAMP);
        FirebaseDatabase.getInstance().getReference("Confirmaciones")
                .child(venta.getIdVenta())
                .updateChildren(hashMap);
    }

    private void primeraVuelta(boolean visible) {
        hideTxt(txtPrimerVueltaM,visible);
        hideTxt(txtPrimerVueltaT,visible);
    }

    private void segundaVuelta(boolean visible) {
        hideTxt(txtSegundaVueltaT,visible);
        hideTxt(txtSegundaVueltaM,visible);
    }

    private void hideTxt(TextInputEditText txt, boolean visible) {
        txt.setCursorVisible(visible);
        txt.setLongClickable(visible);
        txt.setFocusable(visible);
        txt.setClickable(visible);
        txt.setEnabled(visible);
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

    public VentasMostradorBottomSheet getMe() {
        return this;
    }
}