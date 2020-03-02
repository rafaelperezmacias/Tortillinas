package com.zamnadev.tortillinas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

}
