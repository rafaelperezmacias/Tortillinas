package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Moldes.Cuenta;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;

public class AdaptadorEmpleado extends RecyclerView.Adapter<AdaptadorEmpleado.ViewHolder> {

    private Context context;
    private ArrayList<Empleado> empleados;

    public AdaptadorEmpleado(Context context, ArrayList<Empleado> empleados)
    {
        this.context = context;
        this.empleados = empleados;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_empleado,parent,false);
        return new AdaptadorEmpleado.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Empleado empleado = empleados.get(position);

        holder.txtNombre.setText(empleado.getNombre().getNombres() + " " + empleado.getNombre().getApellidos());
        holder.txtTelefono.setText(empleado.getTelefono());
        holder.txtTipo.setText(Empleado.NIVELES_DE_USUARIO[empleado.getTipo()]);

        holder.txtSucursal.setText("Sucursale(s): ");

        /// Una sola sucursal
        DatabaseReference refSucursal = FirebaseDatabase.getInstance().getReference("Sucursales")
                .child(empleado.getSucursales().get("s0"));
        refSucursal.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Sucursal sucursal = dataSnapshot.getValue(Sucursal.class);
                holder.txtSucursal.append(sucursal.getNombre());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Agrega las demas en caso de ser mas (ojo, es importante hacerlo de esta forma)
        for (int x = 1; x < empleado.getSucursales().size(); x++) {
            refSucursal = FirebaseDatabase.getInstance().getReference("Sucursales")
                    .child(empleado.getSucursales().get("s"+x));
            refSucursal.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Sucursal sucursal = dataSnapshot.getValue(Sucursal.class);
                    holder.txtSucursal.append(", " + sucursal.getNombre());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        DatabaseReference refCuentas = FirebaseDatabase.getInstance().getReference("Cuentas")
                .child(empleado.getIdEmpleado());
        refCuentas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Cuenta cuenta = dataSnapshot.getValue(Cuenta.class);
                    holder.txtCuenta.setText(cuenta.getUsuario() + ", " + cuenta.getPassword());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return empleados.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private TextView txtTelefono;
        private TextView txtSucursal;
        private TextView txtTipo;
        private TextView txtCuenta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtTelefono = (TextView) itemView.findViewById(R.id.txtTelefono);
            txtSucursal = (TextView) itemView.findViewById(R.id.txtSucursal);
            txtTipo = (TextView) itemView.findViewById(R.id.txtTipo);
            txtCuenta = (TextView) itemView.findViewById(R.id.txtCuenta);
        }
    }
}
