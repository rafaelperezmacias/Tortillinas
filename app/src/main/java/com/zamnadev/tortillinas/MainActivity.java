package com.zamnadev.tortillinas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference refEmpleado;

    private ValueEventListener listenerEmpleado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()) != null) {
            refEmpleado = FirebaseDatabase.getInstance().getReference("Empleados")
                    .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()));
            listenerEmpleado = refEmpleado.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.e("Data", dataSnapshot.toString());
                    Empleado empleado = dataSnapshot.getValue(Empleado.class);
                    switch (empleado.getTipo()) {
                        case Empleado.TIPO_ADMIN: {
                            break;
                        }
                        case Empleado.TIPO_REPARTIDOR: {
                            /*((Button) findViewById(R.id.btnEmpleados)).setVisibility(View.GONE);
                            ((Button) findViewById(R.id.btnSucursales)).setVisibility(View.GONE);*/
                            break;
                        }
                        case Empleado.TIPO_MOSTRADOR: {
                            /*((Button) findViewById(R.id.btnEmpleados)).setVisibility(View.GONE);
                            ((Button) findViewById(R.id.btnSucursales)).setVisibility(View.GONE);*/
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuCerrarSesion) {
            ControlSesiones.EliminaUsuario(getApplicationContext());
            refEmpleado.removeEventListener(listenerEmpleado);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}