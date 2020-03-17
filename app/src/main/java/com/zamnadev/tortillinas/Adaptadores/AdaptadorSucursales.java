package com.zamnadev.tortillinas.Adaptadores;

import android.app.AlertDialog;
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
import com.zamnadev.tortillinas.BottomSheets.SucursalesBottomSheet;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sucursales.DesvincularEmpleadosSucursal;

import java.util.ArrayList;

public class AdaptadorSucursales extends RecyclerView.Adapter<AdaptadorSucursales.ViewHolder> {

    private Context context;
    private ArrayList<Sucursal> sucursals;
    private FragmentManager fragmentManager;

    public AdaptadorSucursales(Context context, ArrayList<Sucursal> sucursals, FragmentManager fragmentManager)
    {
        this.context = context;
        this.sucursals = sucursals;
        this.fragmentManager = fragmentManager;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_adaptador_sucursal,parent,false);
        return new AdaptadorSucursales.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sucursal sucursal = sucursals.get(position);

        holder.txtNombre.setText(sucursal.getNombre());
        holder.txtDireccion.setText(sucursal.getDireccion().toRecyclerView());
        holder.txtZona.setText(sucursal.getDireccion().getZona());
        holder.txtCubetas.setText("Cubetas: " + sucursal.getBotes());

        holder.btnOpciones.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context,holder.btnOpciones);
            popupMenu.inflate(R.menu.menu_sucursales_recyclerview);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId())
                {
                    case R.id.menuAddCubetas: {

                    } return true;
                    case R.id.menuEditar: {
                            SucursalesBottomSheet bottomSheet = new SucursalesBottomSheet(sucursal, fragmentManager);
                            bottomSheet.show(fragmentManager, bottomSheet.getTag());
                    } return true;
                    case R.id.menuEliminar: {
                        //Valida que no exista ningun empleado referenciado a la sucursal
                        ArrayList<Empleado> empleados = new ArrayList<>();
                        DatabaseReference refEmpleado = FirebaseDatabase.getInstance().getReference("Empleados");
                        refEmpleado.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                empleados.clear();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Empleado empleado = snapshot.getValue(Empleado.class);
                                    if (!empleado.isEliminado() && empleado.getTipo() != Empleado.TIPO_ADMIN) {
                                        for (int x = 0; x < empleado.getSucursales().size(); x++) {
                                            if (empleado.getSucursales().get("s"+x).equals(sucursal.getIdSucursal())) {
                                                empleados.add(empleado);
                                            }
                                        }
                                    }
                                }
                                if (empleados.size() > 0) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Alerta")
                                            .setMessage("Para poder eliminar la sucursal tiene que desvincular a todos los empleados que pertenecen a ella.")
                                            .setPositiveButton("Desvincular", (dialogInterface, i) -> {
                                                DesvincularEmpleadosSucursal bottomSheet = new DesvincularEmpleadosSucursal(empleados,sucursal.getIdSucursal(),fragmentManager);
                                                bottomSheet.show(fragmentManager,bottomSheet.getTag());
                                            })
                                            .setNegativeButton("Cancelar",null)
                                            .show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Alerta")
                                            .setMessage("¿Esta seguro de quere eliminar esta sucursal?")
                                            .setPositiveButton("Eliminar", (dialogInterface, i) -> {
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales").child(sucursal.getIdSucursal());
                                                reference.child("eliminado")
                                                        .setValue(true)
                                                        .addOnCompleteListener(task -> {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(context, "Sucursal eliminada con exito", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(context, "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            })
                                            .setNegativeButton("Cancelar",null)
                                            .show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return sucursals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private TextView txtDireccion;
        private TextView txtZona;
        private TextView txtCubetas;
        private ImageButton btnOpciones;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = (TextView) itemView.findViewById(R.id.txtNombre);
            txtDireccion = (TextView) itemView.findViewById(R.id.txtDireccion);
            txtZona = (TextView) itemView.findViewById(R.id.txtZona);
            btnOpciones = (ImageButton) itemView.findViewById(R.id.btnOpciones);
            txtCubetas = (TextView) itemView.findViewById(R.id.txtCubetas);
        }
    }
}
