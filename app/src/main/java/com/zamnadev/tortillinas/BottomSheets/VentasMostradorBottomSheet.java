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
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
import com.zamnadev.tortillinas.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class VentasMostradorBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private Empleado empleado;
    private String idSucursal;
    private boolean isEditable;
    private String idVenta;

    private DatabaseReference refVenta;
    private ValueEventListener listenerVenta;

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

        TextInputEditText txtPrimerVueltaT = view.findViewById(R.id.txtPrimerVueltaTortilla);
        TextInputEditText txtPrimerVueltaM = view.findViewById(R.id.txtPrimerVueltaMasa);
        TextInputEditText txtSegundaVueltaT = view.findViewById(R.id.txtSegundaVueltaTortilla);
        TextInputEditText txtSegundaVueltaM = view.findViewById(R.id.txtSegundaVueltaTortilla);

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
                Log.e("dsa",dataSnapshot.toString());
                VentaMostrador venta = dataSnapshot.getValue(VentaMostrador.class);
                txtFecha.setText(venta.getFecha());
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
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}