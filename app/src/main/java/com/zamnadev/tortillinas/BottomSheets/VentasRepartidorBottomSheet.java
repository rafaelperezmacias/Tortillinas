package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorRepartidorClientes;
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class VentasRepartidorBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private Empleado empleado;
    private String idSucursal;
    private boolean isEditable;
    private String idVenta;

    private DatabaseReference refVenta;
    private ValueEventListener listenerVenta;

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

        TextInputEditText txtFecha = view.findViewById(R.id.txt_fecha);

        TextView txtSucursal = view.findViewById(R.id.txtSucursal);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Calendar.YEAR);
        calendar.set(Calendar.MONTH, Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fecha = sdf.format(calendar.getTime());

        ArrayList<Cliente> clientes = new ArrayList<>();
        DatabaseReference refClientes = FirebaseDatabase.getInstance().getReference("Clientes");
        refClientes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Cliente cliente = snapshot.getValue(Cliente.class);
                    if (!cliente.isEliminado())
                    {
                        clientes.add(cliente);
                    }
                }
                AdaptadorRepartidorClientes adaptador = new AdaptadorRepartidorClientes(getContext(),clientes,getChildFragmentManager());
                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refVenta = FirebaseDatabase.getInstance().getReference("Ventas")
                .child(empleado.getIdEmpleado())
                .child(idVenta);
        listenerVenta = refVenta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                VentaRepartidor venta = dataSnapshot.getValue(VentaRepartidor.class);
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