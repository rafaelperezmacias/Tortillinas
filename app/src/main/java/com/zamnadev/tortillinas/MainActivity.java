package com.zamnadev.tortillinas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.zamnadev.tortillinas.Clientes.ClientesActivity;
import com.zamnadev.tortillinas.Empleados.EmpleadosActivity;
import com.zamnadev.tortillinas.Sucursales.SucursalesActivity;
import com.zamnadev.tortillinas.Ventas.VentasActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ((Button) findViewById(R.id.btnClientes))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, ClientesActivity.class));
                    }
                });

        ((Button) findViewById(R.id.btnVentas))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, VentasActivity.class));
                    }
                });

        ((Button) findViewById(R.id.btnEmpleados))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, EmpleadosActivity.class));
                    }
                });

        ((Button) findViewById(R.id.btnSucursales))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, SucursalesActivity.class));
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuCerrarSesion) {
            getSharedPreferences("cuentas",MODE_PRIVATE).edit().clear().apply();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
            return true;
        }
        return false;
    }
}
