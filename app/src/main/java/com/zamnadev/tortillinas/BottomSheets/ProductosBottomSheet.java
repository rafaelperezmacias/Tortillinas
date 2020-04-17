package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Moldes.CambioPrecios;
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.Moldes.Direccion;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.ProductoModificado;
import com.zamnadev.tortillinas.R;

import java.util.HashMap;

public class ProductosBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private boolean isError;

    private Producto producto;
    private boolean isEditable;

    public ProductosBottomSheet()
    {
        isEditable = false;
    }

    public ProductosBottomSheet(Producto producto)
    {
        this.producto = producto;
        isEditable = true;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_productos_bottom_sheet, null);
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
        TextInputLayout lytPrecio = view.findViewById(R.id.lytPrecio);

        TextInputEditText txtNombre = view.findViewById(R.id.txtNombre);
        TextInputEditText txtPrecio = view.findViewById(R.id.txtPrecio);

        if (isEditable) {
            txtNombre.setText(producto.getNombre());
            txtNombre.setSelection(txtNombre.getText().length());
            txtPrecio.setText(""+producto.getPrecio());
        }

        ((Button) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    isError = false;
                    if (!validaCampo(lytNombre,txtNombre,"Ingrese el nombre")
                        | !validaCampo(lytPrecio,txtPrecio,"Ingrese el precio")) {
                        return;
                    }

                    HashMap<String,Object> productoMap = new HashMap<>();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Productos");

                    if (isEditable) {
                        productoMap.put("idProducto",producto.getIdProducto());
                        productoMap.put("modificado",true);
                    } else {
                        String id = reference.push().getKey();
                        productoMap.put("idProducto",id);
                        productoMap.put("modificado",false);
                        productoMap.put("alta",ServerValue.TIMESTAMP);
                        productoMap.put("formulario",false);
                    }

                    productoMap.put("nombre",txtNombre.getText().toString().trim());
                    productoMap.put("precio",Double.parseDouble(txtPrecio.getText().toString().trim()));
                    productoMap.put("eliminado",false);

                    reference.child(productoMap.get("idProducto").toString()).updateChildren(productoMap)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    //Esto lo que hace es actualizar los precios preferenciales por defecto en caso de agregar un nuevo
                                    //producto a la base de datos, en caso de no ser preferencial lo toma por defecto
                                    if (!isEditable) {
                                        DatabaseReference refCliente = FirebaseDatabase.getInstance().getReference("Clientes");
                                        refCliente.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                                                {
                                                    Cliente c = snapshot.getValue(Cliente.class);
                                                    if (c.isPreferencial()) {
                                                        c.getPrecios().put("p"+c.getPrecios().size(),Double.parseDouble(txtPrecio.getText().toString().trim()) + "?" + productoMap.get("idProducto").toString());
                                                        snapshot.getRef().child("precios").setValue(c.getPrecios());
                                                    }
                                                }
                                                Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                                                dismiss();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(getContext(), "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        if (producto.getPrecio() != Double.parseDouble(txtPrecio.getText().toString().trim())) {
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("tipo", CambioPrecios.PRODUCTO_MODIFICACION);
                                            hashMap.put("fecha", ServerValue.TIMESTAMP);
                                            hashMap.put("idProducto", productoMap.get("idProducto").toString());
                                            hashMap.put("precio", producto.getPrecio());
                                            DatabaseReference refCambios = FirebaseDatabase.getInstance().getReference("CambioPrecios");
                                            refCambios.push().updateChildren(hashMap);
                                        }
                                        Toast.makeText(getContext(), "Actualización exitosa", Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    }
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