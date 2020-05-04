package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.BottomSheets.VentasMostradorBottomSheet;
import com.zamnadev.tortillinas.BottomSheets.VentasRepartidorBottomSheet;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.Moldes.Venta;
import com.zamnadev.tortillinas.Moldes.VentaRepartidor;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.util.ArrayList;

public class AdaptadorVenta extends RecyclerView.Adapter<AdaptadorVenta.ViewHolder> {

    private Context context;
    private ArrayList<Venta> ventas;
    private String fecha;
    private FragmentManager fragmentManager;
    private boolean isAdmin;

    public AdaptadorVenta(Context context, ArrayList<Venta> ventas, String fecha, FragmentManager fragmentManager, boolean isAdmin)
    {
        this.context = context;
        this.ventas = ventas;
        this.fecha = fecha;
        this.fragmentManager = fragmentManager;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_venta,parent,false);
        return new AdaptadorVenta.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Venta venta = ventas.get(position);

        if (isAdmin) {
            FirebaseDatabase.getInstance().getReference("Empleados")
                    .child(venta.getIdEmpleado())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Empleado empleado = dataSnapshot.getValue(Empleado.class);
                            holder.txtEmpleado.setText(empleado.getNombre().getNombres() + " " + empleado.getNombre().getApellidos());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else {
            holder.txtEmpleado.setVisibility(View.GONE);
        }

        holder.txtFecha.setText(venta.getFecha());
        DatabaseReference refSucursal = FirebaseDatabase.getInstance().getReference("Sucursales")
                .child(venta.getIdSucursal());
        refSucursal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Sucursal sucursal = dataSnapshot.getValue(Sucursal.class);
                holder.txtSucursal.setText(sucursal.getNombre());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (fecha.equals(venta.getFecha())) {
            holder.lytMain.setBackgroundColor(Color.GREEN);
            holder.itemView.setOnClickListener(view -> {
                if (venta.isMostrador()) {
                    VentasMostradorBottomSheet bottomSheet;
                    if (isAdmin) {
                        bottomSheet = new VentasMostradorBottomSheet(venta.getIdVenta(),venta.getIdSucursal(), true, venta.getIdEmpleado());
                    } else {
                        bottomSheet = new VentasMostradorBottomSheet(venta.getIdVenta(), venta.getIdSucursal(), true, venta.getIdEmpleado());
                    }
                    bottomSheet.show(fragmentManager, bottomSheet.getTag());
                } else {
                    VentasRepartidorBottomSheet bottomSheet;
                    if (isAdmin) {
                        bottomSheet = new VentasRepartidorBottomSheet(venta.getIdVenta(),venta.getIdSucursal(), true, venta.getIdEmpleado());
                    } else {
                        bottomSheet = new VentasRepartidorBottomSheet(venta.getIdVenta(),venta.getIdSucursal(), true, venta.getIdEmpleado());
                    }
                    bottomSheet.show(fragmentManager, bottomSheet.getTag());
                }
            });
        } else {
            holder.itemView.setOnClickListener(view -> {
                if (venta.isMostrador()) {
                    VentasMostradorBottomSheet bottomSheet;
                    if (isAdmin) {
                        bottomSheet = new VentasMostradorBottomSheet(venta.getIdVenta(),venta.getIdSucursal(), true,venta.getIdEmpleado());
                    } else {
                        bottomSheet = new VentasMostradorBottomSheet(venta.getIdVenta(),venta.getIdSucursal(), false,venta.getIdEmpleado());
                    }
                    bottomSheet.show(fragmentManager, bottomSheet.getTag());
                } else {
                    VentasRepartidorBottomSheet bottomSheet;
                    if (isAdmin) {
                        bottomSheet = new VentasRepartidorBottomSheet(venta.getIdVenta(),venta.getIdSucursal(), true, venta.getIdEmpleado());
                    } else {
                        bottomSheet = new VentasRepartidorBottomSheet(venta.getIdVenta(),venta.getIdSucursal(), false, venta.getIdEmpleado());
                    }
                    bottomSheet.show(fragmentManager, bottomSheet.getTag());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ventas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtFecha;
        private TextView txtEmpleado;
        private TextView txtSucursal;
        private LinearLayout lytMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFecha = (TextView) itemView.findViewById(R.id.txtFecha);
            txtSucursal = (TextView) itemView.findViewById(R.id.txtSucursal);
            lytMain = (LinearLayout) itemView.findViewById(R.id.lytMain);
            txtEmpleado = (TextView) itemView.findViewById(R.id.txtEmpleado);
        }
    }
}
