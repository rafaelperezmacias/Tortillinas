package com.zamnadev.tortillinas.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
import com.zamnadev.tortillinas.Adaptadores.AdaptadorProductos;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorSucursales;
import com.zamnadev.tortillinas.BottomSheets.ProductosBottomSheet;
import com.zamnadev.tortillinas.BottomSheets.SucursalesBottomSheet;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class ProductosFragment extends Fragment {

    private Activity activity;

    private DatabaseReference refProductos;
    private ValueEventListener listenerProductos;

    public ProductosFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        ArrayList<Producto> productos  = new ArrayList<>();
        refProductos = FirebaseDatabase.getInstance().getReference("Productos");
        listenerProductos = refProductos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Producto producto = snapshot.getValue(Producto.class);
                    if (!producto.isEliminado()) {
                        productos.add(producto);
                    }
                }
                AdaptadorProductos adaptador = new AdaptadorProductos(getContext(),productos,true,getChildFragmentManager());
                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FloatingActionButton fab =  view.findViewById(R.id.fab_producto);
        fab.setOnClickListener(v -> {
            ProductosBottomSheet productosBottomSheet = new ProductosBottomSheet();
            if (getFragmentManager() != null) {
                productosBottomSheet.show(getFragmentManager(), productosBottomSheet.getTag());
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refProductos.removeEventListener(listenerProductos);
    }
}