package com.zamnadev.tortillinas.Sucursales;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zamnadev.tortillinas.Dialogos.DialogoClienteDatos;
import com.zamnadev.tortillinas.Dialogos.DialogoSucursalNombre;
import com.zamnadev.tortillinas.R;

public class SucursalesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursales);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        ((FloatingActionButton) findViewById(R.id.btnAddSucursal))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogoSucursalNombre dialogoSucursalNombre = new DialogoSucursalNombre();
                        dialogoSucursalNombre.show(getSupportFragmentManager(),"DialogoSucursalNombre");
                    }
                });
    }
}
