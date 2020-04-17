package com.zamnadev.tortillinas.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorVenta;
import com.zamnadev.tortillinas.Adaptadores.ViewPagerAdapter;
import com.zamnadev.tortillinas.BottomSheets.VentasMostradorBottomSheet;
import com.zamnadev.tortillinas.BottomSheets.VentasRepartidorBottomSheet;
import com.zamnadev.tortillinas.Dialogos.DialogoVentaSucursal;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ListadoVentasFragment extends Fragment {
    
    private Empleado empleado;
    private ArrayList<VentaRepartidor> ventas;
    private ArrayList<VentaRepartidor> ventasDelDia;
    private String fecha;

    public ListadoVentasFragment()
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        fecha = sdf.format(calendar.getTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listado_ventas, container, false);
        
        ventas = new ArrayList<>();
        ventasDelDia = new ArrayList<>();
        FloatingActionButton fabAgregarVenta = view.findViewById(R.id.fab_ventas);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);

        //Se agrega el evento por la demaro del puto firebase, esta mierda no jala chido con mi internet de mierda
        //Luego validamos la puta espera de respuesta del servidor
        DatabaseReference refEmpleado = FirebaseDatabase.getInstance().getReference("Empleados")
                .child(ControlSesiones.ObtenerUsuarioActivo(getContext()));
        refEmpleado.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                empleado = dataSnapshot.getValue(Empleado.class);
                if (empleado.getTipo() != Empleado.TIPO_ADMIN) {
                    Query refVentas = null;
                    if (empleado.getTipo() == Empleado.TIPO_MOSTRADOR) {
                         refVentas = FirebaseDatabase.getInstance().getReference("VentasMostrador")
                                .child(empleado.getIdEmpleado())
                                .orderByChild("fecha");
                    } else {
                         refVentas = FirebaseDatabase.getInstance().getReference("VentasRepartidor")
                                .child(empleado.getIdEmpleado())
                                .orderByChild("fecha");
                    }

                    refVentas.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ventasDelDia.clear();
                            ventas.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                //Vamos a mostrar todas las ventas, solo podremos verlas
                                VentaRepartidor venta = snapshot.getValue(VentaRepartidor.class);
                                ventas.add(venta);
                                //Pero solo las del dia son las que podremos editar y/o agregar
                                if (venta.getFecha().equals(fecha)) {
                                    ventasDelDia.add(venta);
                                }
                            }
                            if (ventasDelDia.size() == empleado.getSucursales().size()) {
                                fabAgregarVenta.hide();
                            } else {
                                fabAgregarVenta.show();
                            }
                            AdaptadorVenta adaptador = new AdaptadorVenta(getContext(),ventas,fecha,empleado,getFragmentManager(),empleado.getTipo());
                            recyclerView.setAdapter(adaptador);
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

        //Una venta por dia por cada sucursal, si el usuario tiene 2 sucursales a su nombre, este puede generar une venta
        //por cada sucursal al dia
        fabAgregarVenta.setOnClickListener((v) -> {
            //TODO MOSTRADOR
            if (empleado.getTipo() == Empleado.TIPO_MOSTRADOR) {
                DialogoVentaSucursal dialog = new DialogoVentaSucursal(getMe(),empleado,ventasDelDia,true);
                dialog.show(getChildFragmentManager(),dialog.getTag());
            } //TODO REPARTIDOR
            else if (empleado.getTipo() == Empleado.TIPO_REPARTIDOR) {
                //TODO EL proceso se hace de manera diferente
                fabAgregarVenta.hide();
            } else {
                //TODO ADMIN
                fabAgregarVenta.hide();
            }

        });
        return view;
    }


    //TODO Continuacion de alta venta
    public void altaVentaMostrador(String idSucursal) {
        DatabaseReference refVenta = FirebaseDatabase.getInstance().getReference("VentasMostrador")
                .child(empleado.getIdEmpleado());
        HashMap<String, Object> hashMap = new HashMap<>();
        String id = refVenta.push().getKey();
        hashMap.put("idVenta",id);
        hashMap.put("tiempo", ServerValue.TIMESTAMP);
        hashMap.put("fecha",fecha);
        hashMap.put("idSucursal",idSucursal);
        hashMap.put("idEmpleado",empleado.getIdEmpleado());
        HashMap<String, String> repartidores = new HashMap<>();
        repartidores.put("repartidor0","null");
        hashMap.put("repartidores",repartidores);
        refVenta.child(id).updateChildren(hashMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        VentaRepartidor venta = new VentaRepartidor();
                        venta.setIdVenta(id);
                        VentasMostradorBottomSheet bottomSheet = new VentasMostradorBottomSheet(id,empleado,idSucursal);
                        bottomSheet.show(getFragmentManager(), bottomSheet.getTag());
                    }
                });
    }

    private ListadoVentasFragment getMe() {
        return this;
    }
}