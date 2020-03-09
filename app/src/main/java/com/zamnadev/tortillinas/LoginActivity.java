package com.zamnadev.tortillinas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsuario = findViewById(R.id.txtUsuario);
        lytUsuario = findViewById(R.id.lytUsuario);
        txtPassword = findViewById(R.id.txtPassword);
        lytPassword = findViewById(R.id.lytPassword);

        //TODO Usuario y contraseña para el acceso admin , admin

        ((Button) findViewById(R.id.btnIngresar))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!validaUsuario() | !validaPassword()) {
                            return;
                        }

                        Query reference = FirebaseDatabase.getInstance().getReference("Cuentas")
                                .orderByChild("usuario")
                                .equalTo(txtUsuario.getText().toString());

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Query reference = FirebaseDatabase.getInstance().getReference("Cuentas")
                                            .orderByChild("password")
                                            .equalTo(txtPassword.getText().toString());
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot data) {
                                            if (data.exists()) {
                                                Cuenta cuenta = null;
                                                for (DataSnapshot snapshot : data.getChildren()) {
                                                    cuenta = snapshot.getValue(Cuenta.class);
                                                }
                                                ControlSesiones.IngreasarUsuario(getApplicationContext(),cuenta.getIdCuenta());
                                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(LoginActivity.this, "Usuario no registrado", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
    }

    private boolean validaUsuario() {
        if (txtUsuario.getText().toString().isEmpty()) {
            lytUsuario.setError("Rellene el campo");
            return false;
        }

        lytUsuario.setError(null);
        return true;
    }

    private boolean validaPassword() {
        if (txtPassword.getText().toString().isEmpty()) {
            lytPassword.setError("Rellene el campo");
            return false;
        }

        lytPassword.setError(null);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ControlSesiones.ValidaUsuarioActivo(getApplicationContext())) {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
    }
}
