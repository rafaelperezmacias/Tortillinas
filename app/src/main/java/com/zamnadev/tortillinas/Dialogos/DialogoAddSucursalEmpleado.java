package com.zamnadev.tortillinas.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.zamnadev.tortillinas.Sucursales.DesvincularEmpleadosSucursal;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogoAddSucursalEmpleado extends DialogFragment {

    private AdaptadorSucursalesDialogo adaptador;
    private DesvincularEmpleadosSucursal padre;
    private String idSucursal;

    public DialogoAddSucursalEmpleado(DesvincularEmpleadosSucursal padre, String idSucursal)
    {
        this.padre = padre;
        this.idSucursal = idSucursal;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_empleado_sucursal,null);
        builder.setView(view);

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final ArrayList<Sucursal> sucursals = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sucursals.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Sucursal sucursal = snapshot.getValue(Sucursal.class);
                    if (sucursal.getIdSucursal().equals(idSucursal) || sucursal.isEliminado()) {
                        continue;
                    }
                    sucursals.add(sucursal);
                }
                adaptador = new AdaptadorSucursalesDialogo(getContext(),sucursals);
                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ((Button) view.findViewById(R.id.btnCancelar))
                .setBackground(null);

        ((Button) view.findViewById(R.id.btnSiguiente))
                .setBackground(null);

        ((ImageView) view.findViewById(R.id.btnCerrar))
                .setOnClickListener(view1 -> dismiss());

        ((Button) view.findViewById(R.id.btnSiguiente))
                .setOnClickListener(view1 -> {
                    if (adaptador.getTmpSucursales().size() == 0) {
                        Toast.makeText(getContext(), "Seleccione minimo una sucursal", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    padre.addSucursal(adaptador.getTmpSucursales());
                    dismiss();
                });

        ((Button) view.findViewById(R.id.btnCancelar))
                .setOnClickListener(view1 -> dismiss());

        return builder.create();
    }
}
