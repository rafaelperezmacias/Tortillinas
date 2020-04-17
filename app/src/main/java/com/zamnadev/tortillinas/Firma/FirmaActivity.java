package com.zamnadev.tortillinas.Firma;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.util.HashMap;

public class FirmaActivity extends AppCompatActivity {

    private Canvas mCanvas;
    private MaterialButton btnGuardar;

    public static final int FIRMA_CANCELADA = 300;
    public static final int FIRMA_ACEPTADA = 301;

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
        mCanvas.setFirmaActivity(getMe());
        btnGuardar = findViewById(R.id.btn_guardar);
        btnGuardar.setOnClickListener((v) -> {
            setResult(FIRMA_ACEPTADA);
            finish();
        });
        MaterialButton btnBorrar = findViewById(R.id.btn_borrar);
        btnBorrar.setOnClickListener((v) -> {
            btnGuardar.setVisibility(View.INVISIBLE);
            mCanvas.clear();
        });
        MaterialButton btnCancelar = findViewById(R.id.btn_cancelar);
        btnCancelar.setOnClickListener((v) -> {
            setResult(FIRMA_CANCELADA);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Empleados")
                .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()));
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("time", ServerValue.TIMESTAMP);
        hashMap.put("conexion", true);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Empleados")
                .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()));
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("time", ServerValue.TIMESTAMP);
        hashMap.put("conexion", false);
        reference.updateChildren(hashMap);
    }

    public void showBtnGuardar() {
        btnGuardar.setVisibility(View.VISIBLE);
    }

    private FirmaActivity getMe() {
        return this;
    }
}