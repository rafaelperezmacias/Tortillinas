package com.zamnadev.tortillinas.Fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ClientesFragment extends Fragment {
    private Activity activity;

    private View elevation;

    public ClientesFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);
        elevation = view.findViewById(R.id.elevation);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(recyclerView.canScrollVertically(-1)) {
                    elevation.setVisibility(View.VISIBLE);
                } else {
                    elevation.setVisibility(View.GONE);
                }
            }
        });
        final ArrayList<Cliente> clientes = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Clientes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clientes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cliente cliente = snapshot.getValue(Cliente.class);
                    if (!(cliente != null && cliente.isEliminado())) {
                        clientes.add(cliente);
                    }
                }
                AdaptadorClientes adaptadorClientes = new AdaptadorClientes(activity, clientes,getFragmentManager());
                recyclerView.setAdapter(adaptadorClientes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        FloatingActionButton fab = view.findViewById(R.id.fab_clientes);
        fab.setOnClickListener((v) -> {
            ClientesBottomSheet clientesBottomSheet = new ClientesBottomSheet();
            if (getFragmentManager() != null) {
                clientesBottomSheet.show(getFragmentManager(), clientesBottomSheet.getTag());
            }
        });
        return view;
    }
}