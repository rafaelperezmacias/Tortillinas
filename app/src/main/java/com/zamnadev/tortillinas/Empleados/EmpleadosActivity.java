package com.zamnadev.tortillinas.Empleados;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

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

public class EmpleadosActivity extends AppCompatActivity {

    private boolean banderaSucursalExistencia;
    private DatabaseReference refSucursal;
    private ValueEventListener listenerSucuarsal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Empleados");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        validaExistenciaSucursales();

        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        ArrayList<Empleado> empleados = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Empleados");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                empleados.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Empleado empleado = snapshot.getValue(Empleado.class);
                    if (!empleado.isEliminado() && !empleado.getIdEmpleado().equals(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()))) {
                        empleados.add(empleado);
                    }
                }
                AdaptadorEmpleado adaptadorEmpleado = new AdaptadorEmpleado(getApplicationContext(),empleados);
                recyclerView.setAdapter(adaptadorEmpleado);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ((FloatingActionButton) findViewById(R.id.btnAddEmpleado))
                .setOnClickListener(view -> {
                    if (banderaSucursalExistencia) {
                        EmpleadosBottomSheet empleadosBottomSheet = new EmpleadosBottomSheet();
                        empleadosBottomSheet.show(getSupportFragmentManager(),empleadosBottomSheet.getTag());
                    } else {
                       //TODO notifica error de sucursal
                    }
                });
    }

    private void validaExistenciaSucursales()
    {
        refSucursal = FirebaseDatabase.getInstance().getReference("Sucursales");
        listenerSucuarsal = refSucursal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                banderaSucursalExistencia = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Sucursal sucursal = snapshot.getValue(Sucursal.class);
                    if (!sucursal.isEliminado())
                    {
                        banderaSucursalExistencia = true;
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
    protected void onDestroy() {
        refSucursal.removeEventListener(listenerSucuarsal);
        super.onDestroy();
    }
}
