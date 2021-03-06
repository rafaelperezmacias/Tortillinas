package com.zamnadev.tortillinas.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.BottomSheets.EmpleadosBottomSheet;
import com.zamnadev.tortillinas.Dialogs.MessageDialog;
import com.zamnadev.tortillinas.Dialogs.MessageDialogBuilder;
import com.zamnadev.tortillinas.Fragments.EmpleadosFragment;
import com.zamnadev.tortillinas.Moldes.Cuenta;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.Encriptar;

import java.util.ArrayList;

public class AdaptadorEmpleado extends RecyclerView.Adapter<AdaptadorEmpleado.ViewHolder> {
    private Context context;

    private ArrayList<Empleado> empleados;

    private ArrayList<Boolean> showPassword;

    private FragmentManager fragmentManager;

    private EmpleadosFragment empleadosFragment;

    public AdaptadorEmpleado(Context context, ArrayList<Empleado> empleados,
                             FragmentManager fragmentManager, EmpleadosFragment empleadosFragment) {
        this.context = context;
        this.empleados = empleados;
        this.fragmentManager = fragmentManager;
        this.empleadosFragment = empleadosFragment;
        showPassword = new ArrayList<>();
        for (Empleado e : empleados) {
            showPassword.add(false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_adaptador_empleado, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Empleado empleado = empleados.get(position);
        holder.txtNombre.setText(empleado.getNombre().getNombres() + " " +
                empleado.getNombre().getApellidos());
        holder.txtTelefono.setText(empleado.getTelefono());
        holder.txtTipo.setText(Empleado.NIVELES_DE_USUARIO[empleado.getTipo()]);
        holder.txtSucursal.setText("Sucursale(s): ");
        // Una sola sucursal
        if (empleado.getTipo() != Empleado.TIPO_ADMIN) {
            DatabaseReference refSucursal = FirebaseDatabase.getInstance()
                    .getReference("Sucursales")
                    .child(empleado.getSucursales().get("s0"));
            refSucursal.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Sucursal sucursal = dataSnapshot.getValue(Sucursal.class);
                    holder.txtSucursal.append(sucursal.getNombre());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
            //Agrega las demas en caso de ser mas (ojo, es importante hacerlo de esta forma)
            for (int x = 1; x < empleado.getSucursales().size(); x++) {
                refSucursal = FirebaseDatabase.getInstance().getReference("Sucursales")
                        .child(empleado.getSucursales().get("s" + x));
                refSucursal.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Sucursal sucursal = dataSnapshot.getValue(Sucursal.class);
                        holder.txtSucursal.append(", " + sucursal.getNombre());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
        }
        PopupMenu popupMenu = new PopupMenu(context,holder.btnOpciones);
        popupMenu.inflate(R.menu.menu_empleados_recyclerview);
        if (showPassword.get(position)) {
            popupMenu.getMenu().getItem(0).setTitle("Ocultar contraseña");
        } else {
            popupMenu.getMenu().getItem(0).setTitle("Mostrar contraseña");
        }
        holder.btnOpciones.setOnClickListener(view -> {
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.menuPassword: {
                        if (showPassword.get(position)) {
                            showPassword.set(position, false);
                        } else {
                            showPassword.set(position, true);
                        }
                        notifyDataSetChanged();
                        return true;
                    }
                    case R.id.menuEditar: {
                        EmpleadosBottomSheet bottomSheet = new EmpleadosBottomSheet(empleado, empleadosFragment);
                        bottomSheet.show(fragmentManager, bottomSheet.getTag());
                        return true;
                    }
                    case R.id.menuEliminar: {
                        MessageDialog dialog = new MessageDialog(context, new MessageDialogBuilder()
                                .setTitle("Alerta")
                                .setMessage("¿Estás seguro de que quieres eliminar a este empleado?")
                                .setPositiveButtonText("Eliminar")
                                .setNegativeButtonText("Cancelar")
                        );
                        dialog.show();
                        dialog.setPositiveButtonListener(v -> {
                            DatabaseReference refEmpleado = FirebaseDatabase.getInstance()
                                    .getReference("Empleados")
                                    .child(empleado.getIdEmpleado())
                                    .child("eliminado");
                            refEmpleado.setValue(true).addOnCompleteListener(task -> {
                                DatabaseReference refCuenta = FirebaseDatabase.getInstance()
                                        .getReference("Cuentas")
                                        .child(empleado.getIdEmpleado());
                                refCuenta.removeValue().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(context, "Empleado eliminado con exito",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Error, intentelo mas tarde",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            });
                            dialog.dismiss();
                        });
                        dialog.setNegativeButtonListener(v -> dialog.dismiss());
                        return true;
                    }
                }
                return false;
            });
            popupMenu.show();
        });

        DatabaseReference refCuentas = FirebaseDatabase.getInstance().getReference("Cuentas")
                .child(empleado.getIdEmpleado());
        refCuentas.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Cuenta cuenta = dataSnapshot.getValue(Cuenta.class);
                    if (showPassword.get(position)) {
                        try {
                            String pass = Encriptar.desencriptar(cuenta.getPassword());
                            holder.txtCuenta.setText(cuenta.getUsuario() + ", " + pass);
                        } catch (Exception e) {
                            holder.txtCuenta.setText(cuenta.getUsuario() + ", " + cuenta.getPassword());
                        }
                    } else {
                        try {
                            String pass = Encriptar.desencriptar(cuenta.getPassword());
                            holder.txtCuenta.setText(cuenta.getUsuario() + ", " +
                                    pass.replaceAll(".","*"));
                        } catch (Exception e) {
                            holder.txtCuenta.setText(cuenta.getUsuario() + ", " +
                                    cuenta.getPassword().replaceAll(".","*"));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public int getItemCount() { return empleados.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNombre;
        private TextView txtTelefono;
        private TextView txtSucursal;
        private TextView txtTipo;
        private TextView txtCuenta;

        private ImageButton btnOpciones;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTelefono = itemView.findViewById(R.id.txtTelefono);
            txtSucursal = itemView.findViewById(R.id.txtSucursal);
            txtTipo = itemView.findViewById(R.id.txtTipo);
            txtCuenta = itemView.findViewById(R.id.txtCuenta);
            btnOpciones = itemView.findViewById(R.id.btnOpciones);
        }
    }
}