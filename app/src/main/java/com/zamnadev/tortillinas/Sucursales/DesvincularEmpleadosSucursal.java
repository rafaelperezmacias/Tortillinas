package com.zamnadev.tortillinas.Sucursales;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorSucursalesDialogo;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorSucursalesEmpleado;
import com.zamnadev.tortillinas.Dialogos.DialogoAddSucursalEmpleado;
import com.zamnadev.tortillinas.Dialogos.DialogoEmpleadoSucursal;
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DesvincularEmpleadosSucursal extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private ArrayList<Empleado> empleados;
    private int contador;
    private String idSucursal;
    private boolean eliminado;
    private int contadorDeSucursales;

    private RecyclerView recyclerView;
    private ArrayList<Sucursal> sucursals;
    private AdaptadorSucursalesEmpleado adaptador;

    private FragmentManager fragmentManager;

    public DesvincularEmpleadosSucursal(ArrayList<Empleado> empleados, String idSucursal, FragmentManager fragmentManager) {
        this.empleados = empleados;
        this.idSucursal = idSucursal;
        this.fragmentManager = fragmentManager;
        contador = 0;
        contadorDeSucursales = 0;
        eliminado = false;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_desvincular_empleados_sucursal, null);
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

        ((ImageButton) view.findViewById(R.id.btn_cerrar))
                .setOnClickListener((v -> dismiss()));

        MaterialButton btnGuardar = view.findViewById(R.id.btnGuardar);
        TextView txtNombre = view.findViewById(R.id.txtNombre);
        Button btnAgregar = view.findViewById(R.id.btnAgregarSucursal);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        btnGuardar.setText("Siguiente");
        txtNombre.setText("" + empleados.get(contador).getNombre().getNombres() + " " + empleados.get(contador).getNombre().getApellidos());
        sucursals = new ArrayList<>();

        btnAgregar.setOnClickListener(view12 -> {
            if (contadorDeSucursales == sucursals.size()) {
                Toast.makeText(getContext(), "El empleado pertenece a todas las sucursales", Toast.LENGTH_SHORT).show();
                return;
            }
            DialogoAddSucursalEmpleado dialog = new DialogoAddSucursalEmpleado(getMe(),idSucursal);
            dialog.show(fragmentManager,dialog.getTag());
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sucursals.clear();
                contadorDeSucursales = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    contadorDeSucursales++;
                    Sucursal sucursal = snapshot.getValue(Sucursal.class);
                    if (sucursal.isEliminado()) {
                        continue;
                    }
                    for (int x = 0; x < empleados.get(contador).getSucursales().size(); x++) {
                        if (sucursal.getIdSucursal().equals(empleados.get(contador).getSucursales().get("s"+x)))
                        {
                            sucursals.add(sucursal);
                        }
                    }
                }
                adaptador = new AdaptadorSucursalesEmpleado(getContext(),sucursals,getMe());
                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnGuardar.setOnClickListener(view1 -> {
            if (!eliminado) {
                Toast.makeText(getContext(), "Desvincule la sucursal para poder continuar", Toast.LENGTH_SHORT).show();
                return;
            }

            if (sucursals.isEmpty()) {
                Toast.makeText(getContext(), "Agrege una sucursal para el empleado", Toast.LENGTH_SHORT).show();
                return;
            }

            contador++;
            if (contador == empleados.size()) {
                dismiss();
                return;
            }
            if (contador == (empleados.size() - 1)) {
                btnGuardar.setText("Finalizar");
            }
            txtNombre.setText("" + empleados.get(contador).getNombre().getNombres() + " " + empleados.get(contador).getNombre().getApellidos());
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

    private DesvincularEmpleadosSucursal getMe() {
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void addSucursal(ArrayList<Sucursal> sucursal) {
        for (Sucursal s : sucursal) {
            int x = 0;
            for (Sucursal s1 : sucursals) {
                if (s.getIdSucursal().equals(s1.getIdSucursal()))
                {
                    x++;
                }
            }
            if (x == 0) {
                sucursals.add(s);
            }
        }
        adaptador = new AdaptadorSucursalesEmpleado(getContext(),sucursals,getMe());
        recyclerView.setAdapter(adaptador);
    }

    public void eliminarSucursal(String idSucursal) {
        int x = 0;
        for (Sucursal s : sucursals) {
            if (idSucursal.equals(s.getIdSucursal())) {
                sucursals.remove(x);
                if (this.idSucursal.equals(idSucursal)) {
                    eliminado = true;
                }
                adaptador = new AdaptadorSucursalesEmpleado(getContext(),sucursals,getMe());
                recyclerView.setAdapter(adaptador);
                break;
            }
            x++;
        }
    }
}
