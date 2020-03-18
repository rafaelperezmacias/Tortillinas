package com.zamnadev.tortillinas.Dialogos;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorSucursalesDialogo;
import com.zamnadev.tortillinas.BottomSheets.EmpleadosBottomSheet;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class DialogoEmpleadoSucursal extends DialogFragment {
    private AdaptadorSucursalesDialogo adaptador;

    private EmpleadosBottomSheet empleadosBottomSheet;

    public DialogoEmpleadoSucursal(EmpleadosBottomSheet empleadosBottomSheet) {
        this.empleadosBottomSheet = empleadosBottomSheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogo_empleado_sucursal, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final ArrayList<Sucursal> sucursals = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sucursals.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Sucursal sucursal = snapshot.getValue(Sucursal.class);
                    if (!sucursal.isEliminado()) {
                        sucursals.add(sucursal);
                    }
                }
                adaptador = new AdaptadorSucursalesDialogo(getContext(),sucursals);
                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        (view.findViewById(R.id.btnSiguiente)).setOnClickListener(view1 -> {
            if (adaptador.getTmpSucursales().size() == 0) {
                Toast.makeText(getContext(), "Seleccione minimo una sucursal", Toast.LENGTH_SHORT).show();
                return;
            }
            empleadosBottomSheet.setTxtSucursal(adaptador.getTmpSucursales());
            dismiss();
        });
        (view.findViewById(R.id.btnCancelar)).setOnClickListener(view1 -> dismiss());
        return view;
    }
}