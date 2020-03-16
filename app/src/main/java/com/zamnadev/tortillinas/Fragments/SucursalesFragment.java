package com.zamnadev.tortillinas.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SucursalesFragment extends Fragment {

    private Activity activity;

    private DatabaseReference refSucursal;
    private ValueEventListener listenerSucursal;

    public SucursalesFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sucursales, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        ArrayList<Sucursal> sucursals = new ArrayList<>();
        refSucursal = FirebaseDatabase.getInstance().getReference("Sucursales");
        listenerSucursal = refSucursal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sucursals.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Sucursal sucursal = snapshot.getValue(Sucursal.class);
                    if (!(sucursal != null && sucursal.isEliminado())) {
                        sucursals.add(sucursal);
                    }
                }
                AdaptadorSucursales adaptadorSucursales = new AdaptadorSucursales(activity, sucursals);
                recyclerView.setAdapter(adaptadorSucursales);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        FloatingActionButton fab =  view.findViewById(R.id.fab_sucursal);
        fab.setOnClickListener(v -> {
            SucursalesBottomSheet sucursalesBottomSheet = new SucursalesBottomSheet();
            if (getFragmentManager() != null) {
                sucursalesBottomSheet.show(getFragmentManager(), sucursalesBottomSheet.getTag());
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refSucursal.removeEventListener(listenerSucursal);
    }
}