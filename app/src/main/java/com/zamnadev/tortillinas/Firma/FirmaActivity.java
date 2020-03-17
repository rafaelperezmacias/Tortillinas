package com.zamnadev.tortillinas.Firma;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.zamnadev.tortillinas.R;

public class FirmaActivity extends AppCompatActivity {
    private Canvas mCanvas;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.bringToFront();
        mCanvas = findViewById(R.id.canvas);
        MaterialButton btnBorrar = findViewById(R.id.btn_borrar);
        btnBorrar.setOnClickListener((v) -> mCanvas.clear());
        MaterialButton btnCancelar = findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener((v) -> finish());
    }
}