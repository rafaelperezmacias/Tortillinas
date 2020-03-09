package com.zamnadev.tortillinas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Clientes.ClientesActivity;
import com.zamnadev.tortillinas.Empleados.EmpleadosActivity;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;
import com.zamnadev.tortillinas.Sucursales.SucursalesActivity;
import com.zamnadev.tortillinas.Ventas.VentasActivity;

public class MainActivity extends AppCompatActivity {

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
                    Log.e("Data",dataSnapshot.toString());
                    Empleado empleado = dataSnapshot.getValue(Empleado.class);
                    switch (empleado.getTipo())
                    {
                        case Empleado.TIPO_ADMIN:
                        {

                        } break;
                        case Empleado.TIPO_REPARTIDOR:
                        {
                            ((Button) findViewById(R.id.btnEmpleados)).setVisibility(View.GONE);
                            ((Button) findViewById(R.id.btnSucursales)).setVisibility(View.GONE);
                        } break;
                        case Empleado.TIPO_MOSTRADOR:
                        {
                            ((Button) findViewById(R.id.btnEmpleados)).setVisibility(View.GONE);
                            ((Button) findViewById(R.id.btnSucursales)).setVisibility(View.GONE);
                        } break;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        ((Button) findViewById(R.id.btnClientes))
                .setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ClientesActivity.class)));

        ((Button) findViewById(R.id.btnVentas))
                .setOnClickListener(view -> startActivity(new Intent(MainActivity.this, VentasActivity.class)));

        ((Button) findViewById(R.id.btnEmpleados))
                .setOnClickListener(view -> startActivity(new Intent(MainActivity.this, EmpleadosActivity.class)));

        ((Button) findViewById(R.id.btnSucursales))
                .setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SucursalesActivity.class)));
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
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
            return true;
        }
        return false;
    }
}
