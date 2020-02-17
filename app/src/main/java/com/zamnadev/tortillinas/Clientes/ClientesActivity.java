package com.zamnadev.tortillinas.Clientes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zamnadev.tortillinas.Dialogos.DialogoClienteDatos;
import com.zamnadev.tortillinas.R;

public class ClientesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        ((FloatingActionButton) findViewById(R.id.btnAddCliente))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO Valida si existe una sucursal antes de aparecer
                        DialogoClienteDatos dialogoClienteDatos = new DialogoClienteDatos();
                        dialogoClienteDatos.show(getSupportFragmentManager(),"DialogoClienteDatos");
                    }
                });

    }
}
