package com.zamnadev.tortillinas.Clientes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorClientes;
import com.zamnadev.tortillinas.BottomSheets.ClientesBottomSheet;
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class ClientesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Clientes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        final ArrayList<Cliente> clientes = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Clientes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clientes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Cliente cliente = snapshot.getValue(Cliente.class);
                    if (!cliente.isEliminado())
                    {
                        clientes.add(cliente);
                    }
                }
                AdaptadorClientes adaptadorClientes = new AdaptadorClientes(getApplicationContext(),clientes);
                recyclerView.setAdapter(adaptadorClientes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ((FloatingActionButton) findViewById(R.id.btnAddCliente))
                .setOnClickListener(view -> {
                    ClientesBottomSheet clientesBottomSheet = new ClientesBottomSheet();
                    clientesBottomSheet.show(getSupportFragmentManager(),clientesBottomSheet.getTag());
                });

    }
}
