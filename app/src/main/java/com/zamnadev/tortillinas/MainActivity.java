package com.zamnadev.tortillinas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Clientes.ClientesActivity;
import com.zamnadev.tortillinas.Moldes.Cliente;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.btnClientes))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, ClientesActivity.class));
                    }
                });
    }

}
