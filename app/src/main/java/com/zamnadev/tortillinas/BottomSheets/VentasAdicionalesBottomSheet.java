package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorConceptos;
import com.zamnadev.tortillinas.Dialogos.DialogoAddCampoVentas;
import com.zamnadev.tortillinas.Moldes.Concepto;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class VentasAdicionalesBottomSheet extends BottomSheetDialogFragment {

    public static final int TIPO_GASTOS = 1000;
    public static final int TIPO_VENTAS_MOSTRADOR = 1001;
    public static final int TIPO_GASTOS_REPARTIDOR = 1002;

    private BottomSheetBehavior bottomSheetBehavior;

    private int tipo;
    private String idVenta;
    private boolean isEditable;

    public VentasAdicionalesBottomSheet(int tipo, String idVenta, boolean isEditable)
    {
        this.tipo = tipo;
        this.idVenta = idVenta;
        this.isEditable = isEditable;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_ventas_adicionales_bottom_sheet, null);
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

        TextView txtTotal = view.findViewById(R.id.txtTotal);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        if (tipo == TIPO_VENTAS_MOSTRADOR) {
            FirebaseDatabase.getInstance().getReference("Mostrador")
                    .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                    .child(idVenta)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                txtTotal.setText("Total: $0");
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                double total = 0.0;
                                ArrayList<Concepto> conceptos = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Concepto concepto = snapshot.getValue(Concepto.class);
                                    conceptos.add(concepto);
                                    total += concepto.getPrecio();
                                }
                                txtTotal.setText("Total: $" + total);
                                AdaptadorConceptos adaptador = new AdaptadorConceptos(getContext(),conceptos,getFragmentManager(),tipo,idVenta,isEditable);
                                recyclerView.setAdapter(adaptador);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else if (tipo == TIPO_GASTOS) {
            FirebaseDatabase.getInstance().getReference("Gastos")
                    .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                    .child(idVenta)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                txtTotal.setText("Total: $0");
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                double total = 0.0;
                                ArrayList<Concepto> conceptos = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Concepto concepto = snapshot.getValue(Concepto.class);
                                    conceptos.add(concepto);
                                    total += concepto.getPrecio();
                                }
                                txtTotal.setText("Total: $" + total);
                                AdaptadorConceptos adaptador = new AdaptadorConceptos(getContext(),conceptos,getFragmentManager(),tipo,idVenta,isEditable);
                                recyclerView.setAdapter(adaptador);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else if (tipo == TIPO_GASTOS_REPARTIDOR) {
            FirebaseDatabase.getInstance().getReference("Gastos")
                    .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                    .child(idVenta)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                txtTotal.setText("Total: $0");
                                recyclerView.setVisibility(View.GONE);
                            } else {
                                recyclerView.setVisibility(View.VISIBLE);
                                double total = 0.0;
                                ArrayList<Concepto> conceptos = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Concepto concepto = snapshot.getValue(Concepto.class);
                                    conceptos.add(concepto);
                                    total += concepto.getPrecio();
                                }
                                txtTotal.setText("Total: $" + total);
                                AdaptadorConceptos adaptador = new AdaptadorConceptos(getContext(),conceptos,getFragmentManager(),tipo,idVenta,isEditable);
                                recyclerView.setAdapter(adaptador);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        if (!isEditable) {
            ((ImageButton) view.findViewById(R.id.btnAddConcepto))
                    .setVisibility(View.GONE);
        }

        ((ImageButton) view.findViewById(R.id.btnAddConcepto))
                .setOnClickListener(view12 -> {
                    DialogoAddCampoVentas dialogo = new DialogoAddCampoVentas(tipo,idVenta);
                    dialogo.show(getChildFragmentManager(),dialogo.getTag());
                });

        if (tipo == TIPO_GASTOS) {
            ((TextView) view.findViewById(R.id.txtTitulo))
                    .setText("Gastos");
        } else if (tipo == TIPO_VENTAS_MOSTRADOR) {
            ((TextView) view.findViewById(R.id.txtTitulo))
                    .setText("Venta de mostrador");
        } else if (tipo == TIPO_GASTOS_REPARTIDOR) {
            ((TextView) view.findViewById(R.id.txtTitulo))
                    .setText("Gastos");
        }

        ((ImageButton) view.findViewById(R.id.btn_cerrar))
                .setOnClickListener(view1 -> dismiss());

        ((MaterialButton) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> dismiss());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toolbar toolbar = view.findViewById(R.id.toolbar);
            NestedScrollView nestedScrollView = view.findViewById(R.id.nested_scroll_ventas);
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener)
                    (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                        if (scrollY == 0) {
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