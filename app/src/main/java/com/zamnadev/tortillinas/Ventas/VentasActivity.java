package com.zamnadev.tortillinas.Ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zamnadev.tortillinas.BottomSheets.VentasBottomSheet;
import com.zamnadev.tortillinas.R;

public class VentasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas);
        FloatingActionButton fabAgregarVenta = findViewById(R.id.fab_agregar_venta);
        fabAgregarVenta.setOnClickListener((v) -> {
            VentasBottomSheet bottomSheet = new VentasBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
        });
    }
}
