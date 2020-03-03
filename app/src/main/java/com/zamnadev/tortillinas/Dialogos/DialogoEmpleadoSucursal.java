package com.zamnadev.tortillinas.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorSucursalesDialogo;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Nombre;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class DialogoEmpleadoSucursal extends DialogFragment {

    private AdaptadorSucursalesDialogo adaptador;

    public static DialogoEmpleadoSucursal newInstance(Nombre nombre, String telefono) {
        Bundle args = new Bundle();
        args.putString("telefono",telefono);
        args.putSerializable("nombre",nombre);
        DialogoEmpleadoSucursal fragment = new DialogoEmpleadoSucursal();
        fragment.setArguments(args);
        return fragment;
    }

    public DialogoEmpleadoSucursal() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        final Nombre nombre = (Nombre) getArguments().getSerializable("nombre");
        final String telefono = getArguments().getString("telefono");

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_empleado_sucursal,null);
        builder.setView(view);

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final RadioButton rdbMostrador = view.findViewById(R.id.rdbMostrador);
        final RadioButton rdbRepartidor = view.findViewById(R.id.rdbRepartidor);

        final ArrayList<Sucursal> sucursals = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sucursals.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Sucursal sucursal = snapshot.getValue(Sucursal.class);
                    sucursals.add(sucursal);
                }
                adaptador = new AdaptadorSucursalesDialogo(getContext(),sucursals);
                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ((ImageView) view.findViewById(R.id.btnCerrar))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

        ((Button) view.findViewById(R.id.btnSiguiente))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (adaptador.getPositionCheckBox() < 0) {
                            Toast.makeText(getContext(), "Seleccione una sucursal", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int tipo = 0;

                        if (rdbMostrador.isChecked()) {
                            tipo = Empleado.TIPO_MOSTRADOR;
                        } else if (rdbRepartidor.isChecked()) {
                            tipo = Empleado.TIPO_REPARTIDOR;
                        }

                        DialogoEmpleadoCuenta dialogoEmpleadoCuenta = DialogoEmpleadoCuenta.newInstance(nombre,telefono,tipo,adaptador.getIdSucursalActiva());
                        dialogoEmpleadoCuenta.show(getFragmentManager(),"DialogoEmpleadoCuenta");
                        dialogoEmpleadoCuenta.setCancelable(false);
                        dismiss();
                    }
                });

        ((Button) view.findViewById(R.id.btnCancelar))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

        ((Button) view.findViewById(R.id.btnAnterior))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

        return builder.create();
    }
}
