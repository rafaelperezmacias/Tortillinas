package com.zamnadev.tortillinas.Dialogos;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorRepartidoresVentaDialogo;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorSucursalesDialogo;
import com.zamnadev.tortillinas.BottomSheets.VentasMostradorBottomSheet;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Nombre;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class DialogoVentaRepartidor extends DialogFragment {

    private AdaptadorRepartidoresVentaDialogo adaptador;

    private VentasMostradorBottomSheet ventasMostradorBottomSheet;
    private String idSucursal;
    private VentaMostrador venta;
    private boolean ningunEmpleado;

    public DialogoVentaRepartidor(VentasMostradorBottomSheet ventasMostradorBottomSheet, String idSucursal, VentaMostrador venta) {
        this.ventasMostradorBottomSheet = ventasMostradorBottomSheet;
        this.idSucursal = idSucursal;
        this.venta = venta;
        ningunEmpleado = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogo_empleado_sucursal, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        TextView txt = view.findViewById(R.id.tv_dialog_title);
        txt.setText("Seleccione al repartidor");
        final ArrayList<Empleado> empleados = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Empleados");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                empleados.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Empleado empleado  = snapshot.getValue(Empleado.class);
                    boolean c = false;
                    if (venta != null) {
                        for (int x = 0; x < venta.getRepartidores().size(); x++) {
                            if (venta.getRepartidores().get("repartidor"+x).equals(empleado.getIdEmpleado())) {
                                c = true;
                                break;
                            }
                        }
                    }
                    if (c) {
                        continue;
                    }
                    if (!empleado.isEliminado() && empleado.getTipo() == Empleado.TIPO_REPARTIDOR) {
                        if (empleado.getSucursales().size() == 1) {
                            if (empleado.getSucursales().get("s0").equals(idSucursal)){
                                empleados.add(empleado);
                            }
                        } else {
                            for (int x = 0; x < empleado.getSucursales().size(); x++) {
                                if (empleado.getSucursales().get("s"+x).equals(idSucursal)) {
                                    empleados.add(empleado);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (empleados.size() == 0) {
                    Empleado empleado = new Empleado();
                    empleado.setNombre(new Nombre("Ningun repartidor disponible",""));
                    ningunEmpleado = true;
                    empleados.add(empleado);
                }
                adaptador = new AdaptadorRepartidoresVentaDialogo(getContext(),empleados);
                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        (view.findViewById(R.id.btnSiguiente)).setOnClickListener(view1 -> {
            if (ningunEmpleado) {
                dismiss();
            } else {
                ventasMostradorBottomSheet.addRepartidor(adaptador.getIdEmpleado());
                dismiss();
            }
        });
        (view.findViewById(R.id.btnCancelar)).setOnClickListener(view1 -> dismiss());
        return view;
    }
}
