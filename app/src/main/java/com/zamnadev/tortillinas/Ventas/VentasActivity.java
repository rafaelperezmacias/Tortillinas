package com.zamnadev.tortillinas.Ventas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zamnadev.tortillinas.BottomSheets.VentasBottomSheet;
import com.zamnadev.tortillinas.R;

public class VentasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ventas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        FloatingActionButton fabAgregarVenta = findViewById(R.id.fab_agregar_venta);
        fabAgregarVenta.setOnClickListener((v) -> {
            VentasBottomSheet bottomSheet = new VentasBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
        });
    }
}
