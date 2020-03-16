package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorClientesProductos;
import com.zamnadev.tortillinas.Adaptadores.AdaptadorProductos;
import com.zamnadev.tortillinas.Moldes.Direccion;
import com.zamnadev.tortillinas.Moldes.Nombre;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.ProductoModificado;
import com.zamnadev.tortillinas.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientesBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private boolean isError;

    private AdaptadorClientesProductos adaptador;

    public ClientesBottomSheet()
    {

    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_clientes_bottom_sheet, null);
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if(BottomSheetBehavior.STATE_HIDDEN == i) dismiss();
                if(BottomSheetBehavior.STATE_DRAGGING == i) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override public void onSlide(@NonNull View view, float v) { }
        });

        ((ImageButton) view.findViewById(R.id.btn_cerrar))
                .setOnClickListener((v -> dismiss()));

        TextInputLayout lytNombre = view.findViewById(R.id.lytNombre);
        TextInputLayout lytApellidos = view.findViewById(R.id.lytApellidos);
        TextInputLayout lytTelefono = view.findViewById(R.id.lytTelefono);
        TextInputLayout lytCalle = view.findViewById(R.id.lytCalle);
        TextInputLayout lytNumeroExterior = view.findViewById(R.id.lytNumeroExterior);
        TextInputLayout lytNumeroInterior = view.findViewById(R.id.lytNumeroInterior);
        TextInputLayout lytZona = view.findViewById(R.id.lytZona);
        TextInputLayout lytPseudonimo = view.findViewById(R.id.lytPseudonimo);

        TextInputEditText txtNombre = view.findViewById(R.id.txtNombre);
        TextInputEditText txtApellidos = view.findViewById(R.id.txtApellidos);
        TextInputEditText txtTelefono = view.findViewById(R.id.txtTelefono);
        TextInputEditText txtCalle = view.findViewById(R.id.txtCalle);
        TextInputEditText txtNumeroExterior = view.findViewById(R.id.txtNumeroExterior);
        TextInputEditText txtNumeroInterior = view.findViewById(R.id.txtNumeroInterior);
        TextInputEditText txtZona = view.findViewById(R.id.txtZona);
        TextInputEditText txtPseudonimo = view.findViewById(R.id.txtPseudonimo);

        Switch sPrecio = view.findViewById(R.id.sPrecios);
        LinearLayout lytProductos = view.findViewById(R.id.lytProductos);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        ArrayList<Producto> productos = new ArrayList<>();
        DatabaseReference refProductos = FirebaseDatabase.getInstance().getReference("Productos");
        refProductos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Producto producto = snapshot.getValue(Producto.class);
                    if (!producto.isEliminado()) {
                        productos.add(producto);
                    }
                }
                adaptador = new AdaptadorClientesProductos(getContext(),productos,productos);
                recyclerView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sPrecio.setOnCheckedChangeListener((compoundButton, b) -> {
            if (sPrecio.isChecked()) {
                lytProductos.setVisibility(View.VISIBLE);
            } else {
                lytProductos.setVisibility(View.GONE);
            }
        });

        ((Button) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    isError = false;
                    if (!validaCampo(lytNombre,txtNombre,"Ingrese el nombre")
                        | !validaCampo(lytApellidos,txtApellidos,"Ingrese el apellido(s)")
                        | !validaCampo(lytTelefono,txtTelefono,"Ingrese el teléfono")
                        | !validaCampo(lytCalle,txtCalle,"Ingrese la calle")
                        | !validaCampo(lytNumeroExterior,txtNumeroExterior,"Ingrese el numero exterior")
                        | !validaCampo(lytZona,txtZona,"Ingrese la zona")
                        | !validaCampo(lytPseudonimo,txtPseudonimo,"Ingrese el pseudónimo")) {
                        return;
                    }

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Clientes");

                    String id = reference.push().getKey();
                    HashMap<String, Object> clienteMap = new HashMap<>();
                    Direccion direccion = new Direccion();
                    direccion.setCalle(txtCalle.getText().toString().trim());
                    if (!txtNumeroInterior.getText().toString().isEmpty()) {
                        direccion.setNumeroInterior(txtNumeroInterior.getText().toString().trim());
                    }
                    direccion.setNumeroExterior(txtNumeroExterior.getText().toString().trim());
                    direccion.setZona(txtZona.getText().toString().trim());
                    Nombre nombre = new Nombre(txtNombre.getText().toString().trim(),txtApellidos.getText().toString().trim());
                    clienteMap.put("idCliente",id);
                    clienteMap.put("nombre",nombre);
                    clienteMap.put("direccion",direccion);
                    clienteMap.put("telefono",txtTelefono.getText().toString().trim());
                    clienteMap.put("eliminado",false);

                    if (sPrecio.isChecked()) {
                        HashMap<String, String> mapaProductos = new HashMap<>();
                        for (int x = 0; x < adaptador.getNuevosPrecios().size(); x++) {
                            String tmpProducto = adaptador.getNuevosPrecios().get(x).getPrecio() + "?" + adaptador.getNuevosPrecios().get(x).getIdProducto();
                            mapaProductos.put("p"+x,tmpProducto);
                        }
                        clienteMap.put("preferencial",true);
                        clienteMap.put("precios",mapaProductos);
                    } else {
                        clienteMap.put("preferencial",false);
                    }

                    reference.child(id).updateChildren(clienteMap)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                } else {
                                    Toast.makeText(getContext(), "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                                }
                            });
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toolbar toolbar = view.findViewById(R.id.toolbar);
            NestedScrollView nestedScrollView = view.findViewById(R.id.nested_scroll_ventas);
            nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener)
                    (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                        if(scrollY == 0) {
                            toolbar.setElevation(0);
                        } else {
                            toolbar.setElevation(8);
                        }
                    });
        }
        setCancelable(false);
        return bottomSheet;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private boolean validaCampo(TextInputLayout lyt,TextInputEditText txt, String error) {
        if (txt.getText().toString().isEmpty()) {
            lyt.setError(error);
            if (!isError) {
                txt.requestFocus();
                isError = true;
            }
            return false;
        }

        isError = false;
        lyt.setError(null);
        return true;
    }

}
