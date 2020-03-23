package com.zamnadev.tortillinas.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorVenta;
import com.zamnadev.tortillinas.BottomSheets.VentasMostradorBottomSheet;
import com.zamnadev.tortillinas.BottomSheets.VentasRepartidorBottomSheet;
import com.zamnadev.tortillinas.Dialogos.DialogoEmpleadoSucursal;
import com.zamnadev.tortillinas.Dialogos.DialogoVentaSucursal;
import com.zamnadev.tortillinas.MainActivity;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Venta;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class VentasFragment extends Fragment {

    private Empleado empleado;
    private ArrayList<Venta> ventas;
    private ArrayList<Venta> ventasDelDia;
    private String fecha;

    public VentasFragment()
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
        View view = inflater.inflate(R.layout.fragment_ventas, container, false);

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
                    Query refVentas = FirebaseDatabase.getInstance().getReference("Ventas")
                            .child(empleado.getIdEmpleado())
                            .orderByChild("fecha");

                    refVentas.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ventasDelDia.clear();
                            ventas.clear();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                //Vamos a mostrar todas las ventas, solo podremos verlas
                                Venta venta = snapshot.getValue(Venta.class);
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
                            AdaptadorVenta adaptador = new AdaptadorVenta(getContext(),ventas,fecha,empleado,getChildFragmentManager(),empleado.getTipo());
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
                if (empleado.getSucursales().size() == 1) {
                    altaVentaMostrador(empleado.getSucursales().get("s"+0));
                } else {
                    //TODO Proceso para poder seleccionar la sucursal
                    //Despues de seleccionarla se procedera en el metodo alta venta
                    DialogoVentaSucursal dialog = new DialogoVentaSucursal(getMe(),empleado,ventasDelDia,true);
                    dialog.show(getChildFragmentManager(),dialog.getTag());
                }
            } //TODO REPARTIDOR
            else if (empleado.getTipo() == Empleado.TIPO_REPARTIDOR) {
                if (empleado.getSucursales().size() == 1) {
                    altaVentaRepartidor(empleado.getSucursales().get("s"+0));
                } else {
                    //TODO Proceso para poder seleccionar la sucursal
                    //Despues de seleccionarla se procedera en el metodo alta venta
                    DialogoVentaSucursal dialog = new DialogoVentaSucursal(getMe(),empleado,ventasDelDia,false);
                    dialog.show(getChildFragmentManager(),dialog.getTag());
                }
            } else {
                //TODO ADMIN
                fabAgregarVenta.hide();
            }

        });
        return view;
    }

    //TODO Continuacion de alta venta
    public void altaVentaMostrador(String idSucursal) {
        DatabaseReference refVenta = FirebaseDatabase.getInstance().getReference("Ventas")
                .child(empleado.getIdEmpleado());
        HashMap<String, Object> hashMap = new HashMap<>();
        String id = refVenta.push().getKey();
        hashMap.put("idVenta",id);
        hashMap.put("tiempo", ServerValue.TIMESTAMP);
        hashMap.put("fecha",fecha);
        hashMap.put("idSucursal",idSucursal);
        hashMap.put("idEmpleado",empleado.getIdEmpleado());
        refVenta.child(id).updateChildren(hashMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Venta venta = new Venta();
                        venta.setIdVenta(id);
                        VentasMostradorBottomSheet bottomSheet = new VentasMostradorBottomSheet(id,empleado,idSucursal);
                        bottomSheet.show(getFragmentManager(), bottomSheet.getTag());
                    }
                });
    }

    public void altaVentaRepartidor(String idSucursal) {
        DatabaseReference refVenta = FirebaseDatabase.getInstance().getReference("Ventas")
                .child(empleado.getIdEmpleado());
        HashMap<String, Object> hashMap = new HashMap<>();
        String id = refVenta.push().getKey();
        hashMap.put("idVenta",id);
        hashMap.put("tiempo", ServerValue.TIMESTAMP);
        hashMap.put("fecha",fecha);
        hashMap.put("idSucursal",idSucursal);
        hashMap.put("idEmpleado",empleado.getIdEmpleado());
        refVenta.child(id).updateChildren(hashMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Venta venta = new Venta();
                        venta.setIdVenta(id);
                        VentasRepartidorBottomSheet bottomSheet = new VentasRepartidorBottomSheet(id,empleado,idSucursal);
                        bottomSheet.show(getFragmentManager(), bottomSheet.getTag());
                    }
                });
    }

    private VentasFragment getMe() {
        return this;
    }
}