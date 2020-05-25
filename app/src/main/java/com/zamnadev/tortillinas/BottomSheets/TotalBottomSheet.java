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
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorTotal;
import com.zamnadev.tortillinas.Moldes.Concepto;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.VentaDelDia;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class TotalBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private String idVenta;
    private String idEmpleado;
    private double ventasDelDia;

    public TotalBottomSheet(String idVenta, String idEmpleado)
    {
        this.idVenta = idVenta;
        this.idEmpleado = idEmpleado;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_total_botttom_sheet, null);
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

        MaterialCardView cardVentasExtras = view.findViewById(R.id.card_ventas_extra);
        MaterialCardView cardVentasMostrador = view.findViewById(R.id.card_ventas_mostrador);
        MaterialCardView cardGastosMostrador = view.findViewById(R.id.card_gastos_mostrador);

        TextView txtTotalMostrador = view.findViewById(R.id.txtTotalVentaMostrador);
        RecyclerView rVentaMostrador = view.findViewById(R.id.recyclerviewVentaMostrador);
        ImageButton btnVentaMostrador = view.findViewById(R.id.btnVentaMostrador);

        TextView txtTotalGastosMostrador = view.findViewById(R.id.txtGastosMostrador);
        RecyclerView rGastosMostrador = view.findViewById(R.id.recyclerviewGastosMostrador);
        ImageButton btnGastosMostrador = view.findViewById(R.id.btnGastosMostrador);

        TextView txtVentasExtra = view.findViewById(R.id.txtVentasExtra);
        RecyclerView rVentasExtra = view.findViewById(R.id.recyclerviewVentasExtra);
        ImageButton btnVentasExtra = view.findViewById(R.id.btnVentasExtra);

        rVentaMostrador.setLayoutManager(new LinearLayoutManager(getContext()));
        rVentaMostrador.setHasFixedSize(true);
        rGastosMostrador.setLayoutManager(new LinearLayoutManager(getContext()));
        rGastosMostrador.setHasFixedSize(true);
        rVentasExtra.setLayoutManager(new LinearLayoutManager(getContext()));
        rVentasExtra.setHasFixedSize(true);

        FirebaseDatabase.getInstance().getReference("Mostrador")
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
                            txtTotalMostrador.setText("$" + total);
                            AdaptadorTotal adaptador = new AdaptadorTotal(getContext(),conceptos);
                            rVentaMostrador.setAdapter(adaptador);
                        } else {
                            txtTotalMostrador.setText("$0");
                        }
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
                            txtTotalGastosMostrador.setText("$" + total);
                            AdaptadorTotal adaptador = new AdaptadorTotal(getContext(),conceptos);
                            rGastosMostrador.setAdapter(adaptador);
                        } else {
                            txtTotalGastosMostrador.setText("$0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference("VentasDelDia")
                .child(idEmpleado)
                .child(idVenta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            ArrayList<Concepto> conceptos = new ArrayList<>();
                            ventasDelDia = 0.0;
                            conceptos.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                VentaDelDia venta = snapshot.getValue(VentaDelDia.class);
                                FirebaseDatabase.getInstance().getReference("Productos")
                                        .child(venta.getIdProducto())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snp) {
                                                Producto producto = snp.getValue(Producto.class);
                                                Concepto concepto = new Concepto();
                                                concepto.setNombre(producto.getNombre() + "\nCantidad: " + venta.getCantidad());
                                                concepto.setPrecio(venta.getTotal());
                                                ventasDelDia += concepto.getPrecio();
                                                conceptos.add(concepto);
                                                txtVentasExtra.setText("$" + ventasDelDia);
                                                AdaptadorTotal adaptador = new AdaptadorTotal(getContext(),conceptos);
                                                rVentasExtra.setAdapter(adaptador);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }
                            txtVentasExtra.setText("$" + ventasDelDia);
                            AdaptadorTotal adaptador = new AdaptadorTotal(getContext(),conceptos);
                            rVentasExtra.setAdapter(adaptador);
                        } else {
                            txtVentasExtra.setText("$0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        btnVentaMostrador.setOnClickListener(view1 -> {
            if (btnVentaMostrador.getTag().equals("down")) {
                btnVentaMostrador.setTag("up");
                btnVentaMostrador.setImageResource(R.drawable.ic_arrow_up_24dp);
                rVentaMostrador.setVisibility(View.VISIBLE);
            } else {
                btnVentaMostrador.setTag("down");
                btnVentaMostrador.setImageResource(R.drawable.ic_arrow_down_24dp);
                rVentaMostrador.setVisibility(View.GONE);
            }
        });

        cardVentasMostrador.setOnClickListener(view1 -> {
            if (btnVentaMostrador.getTag().equals("down")) {
                btnVentaMostrador.setTag("up");
                btnVentaMostrador.setImageResource(R.drawable.ic_arrow_up_24dp);
                rVentaMostrador.setVisibility(View.VISIBLE);
            } else {
                btnVentaMostrador.setTag("down");
                btnVentaMostrador.setImageResource(R.drawable.ic_arrow_down_24dp);
                rVentaMostrador.setVisibility(View.GONE);
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

        btnVentasExtra.setOnClickListener(view1 -> {
            if (btnVentasExtra.getTag().equals("down")) {
                btnVentasExtra.setTag("up");
                btnVentasExtra.setImageResource(R.drawable.ic_arrow_up_24dp);
                rVentasExtra.setVisibility(View.VISIBLE);
            } else {
                btnVentasExtra.setTag("down");
                btnVentasExtra.setImageResource(R.drawable.ic_arrow_down_24dp);
                rVentasExtra.setVisibility(View.GONE);
            }
        });

        cardVentasExtras.setOnClickListener(view1 -> {
            if (btnVentasExtra.getTag().equals("down")) {
                btnVentasExtra.setTag("up");
                btnVentasExtra.setImageResource(R.drawable.ic_arrow_up_24dp);
                rVentasExtra.setVisibility(View.VISIBLE);
            } else {
                btnVentasExtra.setTag("down");
                btnVentasExtra.setImageResource(R.drawable.ic_arrow_down_24dp);
                rVentasExtra.setVisibility(View.GONE);
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
}

