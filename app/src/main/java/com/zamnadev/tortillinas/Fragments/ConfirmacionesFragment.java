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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorConfirmaciones;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorVenta;
import com.zamnadev.tortillinas.BottomSheets.VentasRepartidorBottomSheet;
import com.zamnadev.tortillinas.Firma.FirmaActivity;
import com.zamnadev.tortillinas.MainActivity;
import com.zamnadev.tortillinas.Moldes.Confirmacion;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
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
        fecha = MainActivity.getFecha();
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

    public void addIntent(String idVenta, boolean primero) {
        FirebaseDatabase.getInstance().getReference("Confirmaciones")
                .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                .child(idVenta)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        getMe().confirmacion = dataSnapshot.getValue(Confirmacion.class);
                        getMe().primero = primero;
                        startActivityForResult(new Intent(getContext(), FirmaActivity.class), CODE_INTENT);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
                FirebaseDatabase.getInstance().getReference("VentasMostrador")
                        .child(confirmacion.getIdEmpleado())
                        .child(confirmacion.getIdVenta())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                VentaMostrador ventaMostrador = dataSnapshot.getValue(VentaMostrador.class);
                                altaVentaRepartidor(ventaMostrador.getIdSucursal(),confirmacion.getIdVenta(),confirmacion.getVuelta1(),true);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
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
                FirebaseDatabase.getInstance().getReference("VentasMostrador")
                        .child(confirmacion.getIdEmpleado())
                        .child(confirmacion.getIdVenta())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                VentaMostrador ventaMostrador = dataSnapshot.getValue(VentaMostrador.class);
                                altaVentaRepartidor(ventaMostrador.getIdSucursal(),confirmacion.getIdVenta(),confirmacion.getVuelta2(),false);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        }
    }

    public void altaVentaRepartidor(String idSucursal, String idVenta, Vuelta vuelta, boolean primero) {
        DatabaseReference refVenta = FirebaseDatabase.getInstance().getReference("VentasRepartidor")
                .child(empleado.getIdEmpleado());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idVenta",idVenta);
        HashMap<String, Object> vueltaMap = new HashMap<>();
        vueltaMap.put("masa",vuelta.getMasa());
        vueltaMap.put("tortillas",vuelta.getTortillas());
        vueltaMap.put("totopos",vuelta.getTotopos());
        vueltaMap.put("time",ServerValue.TIMESTAMP);
        vueltaMap.put("registrada",true);
        vueltaMap.put("confirmado",true);
        if (primero) {
            hashMap.put("vuelta1",vueltaMap);
        } else {
            hashMap.put("vuelta2",vueltaMap);
        }
        hashMap.put("tiempo", ServerValue.TIMESTAMP);
        hashMap.put("fecha",fecha);
        hashMap.put("idSucursal",idSucursal);
        hashMap.put("idEmpleado",empleado.getIdEmpleado());
        refVenta.child(idVenta).updateChildren(hashMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getContext(), "Venta agrega con éxito", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
