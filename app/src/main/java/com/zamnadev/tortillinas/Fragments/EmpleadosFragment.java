package com.zamnadev.tortillinas.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorEmpleado;
import com.zamnadev.tortillinas.BottomSheets.EmpleadosBottomSheet;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.util.ArrayList;

public class EmpleadosFragment extends Fragment {

    private Activity activity;

    private boolean existeSucursal;

    private DatabaseReference refSucursal;
    private ValueEventListener listenerSucuarsal;
    private AdaptadorEmpleado adaptadorEmpleado;

    public EmpleadosFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empleados, container, false);
        validaExistenciaSucursales();
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        ArrayList<Empleado> empleados = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Empleados");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                empleados.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Empleado empleado = snapshot.getValue(Empleado.class);
                    if (!(empleado != null && empleado.isEliminado()) &&
                            !(empleado != null && empleado.getIdEmpleado().equals(ControlSesiones.ObtenerUsuarioActivo(activity)))) {
                        empleados.add(empleado);
                    }
                }
                adaptadorEmpleado = new AdaptadorEmpleado(activity, empleados);
                recyclerView.setAdapter(adaptadorEmpleado);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_empleados);
        fab.setOnClickListener(v -> {
            if (existeSucursal) {
                EmpleadosBottomSheet empleadosBottomSheet = new EmpleadosBottomSheet(getMe());
                if (getFragmentManager() != null) {
                    empleadosBottomSheet.show(getFragmentManager(), empleadosBottomSheet.getTag());
                }
            } else {
                //TODO notifica error de sucursal
                Toast.makeText(activity, "Ninguna sucursal activa", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private EmpleadosFragment getMe() {
        return this;
    }

    //Repinta de nuevo el recycler, las inserciones de cuentas se hacen un poco lentas
    public void notificarCambios() {
        adaptadorEmpleado.notifyDataSetChanged();
    }

    private void validaExistenciaSucursales() {
        refSucursal = FirebaseDatabase.getInstance().getReference("Sucursales");
        listenerSucuarsal = refSucursal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                existeSucursal = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Sucursal sucursal = snapshot.getValue(Sucursal.class);
                    if (!(sucursal != null && sucursal.isEliminado())) {
                        existeSucursal = true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroy() {
        refSucursal.removeEventListener(listenerSucuarsal);
        super.onDestroy();
    }
}