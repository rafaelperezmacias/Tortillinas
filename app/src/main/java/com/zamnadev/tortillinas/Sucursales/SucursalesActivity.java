package com.zamnadev.tortillinas.Sucursales;

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
import com.zamnadev.tortillinas.Adaptadores.AdaptadorSucursales;
import com.zamnadev.tortillinas.BottomSheets.SucursalesBottomSheet;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class SucursalesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucursales);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sucursales");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

        ArrayList<Sucursal> sucursals = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sucursals.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Sucursal sucursal = snapshot.getValue(Sucursal.class);
                    if (!sucursal.isEliminado()) {
                        sucursals.add(sucursal);
                    }
                }
                AdaptadorSucursales adaptadorSucursales = new AdaptadorSucursales(getApplicationContext(),sucursals);
                recyclerView.setAdapter(adaptadorSucursales);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ((FloatingActionButton) findViewById(R.id.btnAddSucursal))
                .setOnClickListener(view -> {
                    SucursalesBottomSheet sucursalesBottomSheet = new SucursalesBottomSheet();
                    sucursalesBottomSheet.show(getSupportFragmentManager(),sucursalesBottomSheet.getTag());
                });
    }
}
