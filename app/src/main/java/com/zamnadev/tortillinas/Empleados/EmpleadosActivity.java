package com.zamnadev.tortillinas.Empleados;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zamnadev.tortillinas.Dialogos.DialogoEmpleadoDatos;
import com.zamnadev.tortillinas.R;

public class EmpleadosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleados);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((FloatingActionButton) findViewById(R.id.btnAddEmpleado))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogoEmpleadoDatos dialogoEmpleadosDatos = new DialogoEmpleadoDatos();
                        dialogoEmpleadosDatos.show(getSupportFragmentManager(),"DialogoEmpleadosDatos");
                    }
                });
    }
}
