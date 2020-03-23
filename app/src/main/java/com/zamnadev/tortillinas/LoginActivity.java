package com.zamnadev.tortillinas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Moldes.Cuenta;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText txtUsuario;
    private TextInputLayout lytUsuario;
    private TextInputEditText txtPassword;
    private TextInputLayout lytPassword;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsuario = findViewById(R.id.txtUsuario);
        lytUsuario = findViewById(R.id.lytUsuario);
        txtPassword = findViewById(R.id.txtPassword);
        lytPassword = findViewById(R.id.lytPassword);
        progressBar = findViewById(R.id.progressbar);

        Button btnIngresar = findViewById(R.id.btnIngresar);

        MaterialCardView mcvError = findViewById(R.id.card_error);

        //TODO Usuario y contraseña para el acceso admin , admin modo admin
        //TODO modo repartidor: repartidor,repartidor
        //TODO modo mostrador: mostrador, mostrador

        btnIngresar.setOnClickListener(view -> {
            if (!validaUsuario() | !validaPassword()) return;
            btnIngresar.setText("INICIANDO SESIÓN...");
            btnIngresar.setEnabled(false);
            lytUsuario.setEnabled(false);
            lytPassword.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            Query reference = FirebaseDatabase.getInstance().getReference("Cuentas")
                    .orderByChild("usuario")
                    .equalTo(txtUsuario.getText().toString().trim());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    Log.e("data", dataSnapshot.toString());
                    if (dataSnapshot.exists()) {
                        Query reference = FirebaseDatabase.getInstance().getReference("Cuentas")
                                .orderByChild("password")
                                .equalTo(txtPassword.getText().toString().trim());
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot data) {
                                if (data.exists()) {
                                    Cuenta cuenta = null;
                                    for (DataSnapshot snapshot : data.getChildren()) {
                                        cuenta = snapshot.getValue(Cuenta.class);
                                    }
                                    ControlSesiones.IngreasarUsuario(getApplicationContext(), cuenta.getIdCuenta());
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    btnIngresar.setText("INGRESAR");
                                    btnIngresar.setEnabled(true);
                                    lytUsuario.setEnabled(true);
                                    lytPassword.setEnabled(true);
                                    progressBar.setVisibility(View.GONE);
                                    mcvError.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });
                    } else {
                        btnIngresar.setText("INGRESAR");
                        btnIngresar.setEnabled(true);
                        lytUsuario.setEnabled(true);
                        lytPassword.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        mcvError.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        });
    }

    private boolean validaUsuario() {
        if (txtUsuario.getText().toString().isEmpty()) {
            lytUsuario.setError("Por favor, ingresa tu usuario");
            return false;
        }
        lytUsuario.setError(null);
        return true;
    }

    private boolean validaPassword() {
        if (txtPassword.getText().toString().isEmpty()) {
            lytPassword.setError("Por favor, ingresa tu contraseña");
            return false;
        }
        lytPassword.setError(null);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ControlSesiones.ValidaUsuarioActivo(getApplicationContext())) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}