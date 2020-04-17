package com.zamnadev.tortillinas.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorConfirmaciones;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorVenta;
import com.zamnadev.tortillinas.Firma.FirmaActivity;
import com.zamnadev.tortillinas.Moldes.Confirmacion;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.Moldes.Vuelta;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ConfirmacionesFragment extends Fragment {

    private static final int CODE_INTENT = 100;

    private String fecha;
    private Empleado empleado;
    private int contador;

    private VentasFragment ventasFragment;

    private Confirmacion confirmacion;
    private boolean primero;

    public ConfirmacionesFragment(VentasFragment ventasFragment)
    {
        this.ventasFragment = ventasFragment;
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fecha = sdf.format(calendar.getTime());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmaciones, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);

        DatabaseReference refEmpleado = FirebaseDatabase.getInstance().getReference("Empleados")
                .child(ControlSesiones.ObtenerUsuarioActivo(getContext()));
        refEmpleado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                empleado = dataSnapshot.getValue(Empleado.class);
                if (empleado.getTipo() != Empleado.TIPO_ADMIN) {
                    Query refConfirmaciones = FirebaseDatabase.getInstance().getReference("Confirmaciones")
                                .child(empleado.getIdEmpleado())
                                .orderByChild("fecha");
                    refConfirmaciones.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<Confirmacion> confirmaciones  = new ArrayList<>();
                            contador = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                Confirmacion confirmacion = snapshot.getValue(Confirmacion.class);
                                if (confirmacion.getFecha().equals(fecha)) {
                                    if (confirmacion.getVuelta1() != null) {
                                        if (!confirmacion.getVuelta1().isConfirmado()) {
                                            confirmaciones.add(confirmacion);
                                            contador++;
                                        }
                                    }
                                    if (confirmacion.getVuelta2() != null) {
                                        if (!confirmacion.getVuelta2().isConfirmado()) {
                                            confirmaciones.add(confirmacion);
                                            contador ++;
                                        }
                                    }
                                }
                            }
                            AdaptadorConfirmaciones adaptador = new AdaptadorConfirmaciones(getContext(),confirmaciones,getMe());
                            recyclerView.setAdapter(adaptador);
                            if (contador == 0) {
                                ventasFragment.setTextToTab("Confirmaciones");
                            } else {
                                ventasFragment.setTextToTab("Confirmaciones (" + contador + ")");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private ConfirmacionesFragment getMe() {
        return this;
    }

    public void addIntent(Confirmacion confirmacion, boolean primero) {
        this.confirmacion = confirmacion;
        this.primero = primero;
        startActivityForResult(new Intent(getContext(), FirmaActivity.class), CODE_INTENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_INTENT)  {
            if (resultCode == FirmaActivity.FIRMA_ACEPTADA) {
                alta();
            } else {
                Toast.makeText(getContext(), "Error al firmar la entrega", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void alta() {
        if (confirmacion != null) {
            if (primero) {
                FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                        .child(confirmacion.getIdVenta())
                        .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                        .child("vuelta1")
                        .child("confirmado")
                        .setValue(true);
                FirebaseDatabase.getInstance().getReference("Confirmaciones")
                        .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                        .child(confirmacion.getIdVenta())
                        .child("vuelta1")
                        .child("confirmado")
                        .setValue(true);
            } else {
                FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                        .child(confirmacion.getIdVenta())
                        .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                        .child("vuelta2")
                        .child("confirmado")
                        .setValue(true);
                FirebaseDatabase.getInstance().getReference("Confirmaciones")
                        .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                        .child(confirmacion.getIdVenta())
                        .child("vuelta2")
                        .child("confirmado")
                        .setValue(true);
            }

        }
    }
}
