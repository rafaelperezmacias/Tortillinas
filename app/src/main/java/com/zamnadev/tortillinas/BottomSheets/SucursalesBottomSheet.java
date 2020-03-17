package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zamnadev.tortillinas.Moldes.Direccion;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

import java.util.HashMap;

public class SucursalesBottomSheet extends BottomSheetDialogFragment {
    private Toolbar toolbar;

    private BottomSheetBehavior bottomSheetBehavior;

    private Sucursal sucursal;

    private Direccion direccion;

    private boolean isEditMode = false;

    public SucursalesBottomSheet() { }

    public SucursalesBottomSheet(Sucursal sucursal) {
        this.sucursal = sucursal;
        isEditMode = true;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_sucursales_bottom_sheet, null);
        bottomSheet.setContentView(view);
        toolbar = view.findViewById(R.id.toolbar);
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

        (view.findViewById(R.id.btn_cerrar)).setOnClickListener((v -> dismiss()));

        TextView lblTitulo = view.findViewById(R.id.lbl_toolbar_title);

        TextInputLayout lytNombre = view.findViewById(R.id.lytNombre);
        TextInputLayout lytCalle = view.findViewById(R.id.lytCalle);
        TextInputLayout lytNumeroExterior = view.findViewById(R.id.lytNumeroExterior);
        TextInputLayout lytNumeroInterior = view.findViewById(R.id.lytNumeroInterior);
        TextInputLayout lytZona = view.findViewById(R.id.lytZona);
        TextInputLayout tilCubetas = view.findViewById(R.id.til_cubetas);

        TextInputEditText txtNombre = view.findViewById(R.id.txtNombre);
        TextInputEditText txtCalle = view.findViewById(R.id.txtCalle);
        TextInputEditText txtNumeroExterior = view.findViewById(R.id.txtNumeroExterior);
        TextInputEditText txtNumeroInterior = view.findViewById(R.id.txtNumeroInterior);
        TextInputEditText txtZona = view.findViewById(R.id.txtZona);
        TextInputEditText txtCubetas = view.findViewById(R.id.txt_cubetas);

        if(isEditMode) {
            lblTitulo.setText("Editar sucursal");
            tilCubetas.setVisibility(View.VISIBLE);
            txtNombre.setText(sucursal.getNombre());
            direccion = sucursal.getDireccion();
            txtCalle.setText(direccion.getCalle());
            txtNombre.setSelection(txtNombre.getText().length());
            txtNumeroExterior.setText(direccion.getNumeroExterior());
            if (direccion.getNumeroInterior() != null) {
                txtNumeroInterior.setText(direccion.getNumeroInterior());
            }
            txtZona.setText(direccion.getZona());
            txtCubetas.setText(String.valueOf(sucursal.getBotes()));
        }

        (view.findViewById(R.id.btnGuardar)).setOnClickListener(view1 -> {
            if (!validaCampo(lytNombre, txtNombre, "Ingrese el nombre")
                    | !validaCampo(lytCalle, txtCalle, "Ingrese la calle")
                    | !validaCampo(lytNumeroExterior, txtNumeroExterior, "Ingrese el numero exterior")
                    | !validaCampo(lytZona, txtZona, "Ingrese la zona")) {
                if(isEditMode) {
                    if (!validaCampo(tilCubetas, txtCubetas, "Ingrese las cubetas")) return;
                }
                return;
            }
            if(isEditMode) {
                if(!validaCampo(tilCubetas, txtCubetas, "Ingrese las cubetas")) {
                    return;
                }
                DatabaseReference referenceNombre = FirebaseDatabase.getInstance()
                        .getReference("Sucursales")
                        .child(sucursal.getIdSucursal()).child("nombre");
                referenceNombre.setValue(txtNombre.getText().toString().trim())
                        .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dismiss();
                    }
                });
                DatabaseReference referenceDireccion;
                String idPadre = sucursal.getIdSucursal();
                int tipo = 1; // Temporal
                if (tipo == 1) {
                    referenceDireccion = FirebaseDatabase.getInstance()
                            .getReference("Sucursales").child(idPadre).child("direccion");
                } else {
                    referenceDireccion = FirebaseDatabase.getInstance()
                            .getReference("Clientes").child(idPadre).child("direccion");
                }
                direccion.setCalle(txtCalle.getText().toString().trim());
                direccion.setNumeroExterior(txtNumeroExterior.getText().toString().trim());
                if (!txtNumeroInterior.getText().toString().isEmpty()) {
                    direccion.setNumeroInterior(txtNumeroInterior.getText().toString().trim());
                } else {
                    direccion.setNumeroInterior(null);
                }
                direccion.setZona(txtZona.getText().toString().trim());
                referenceDireccion.setValue(direccion).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dismiss();
                    }
                });
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales")
                        .child(sucursal.getIdSucursal()).child("botes");
                int tmpCubetas = Integer.parseInt(txtCubetas.getText().toString().trim());
                reference.setValue(tmpCubetas).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dismiss();
                    }
                });
            } else {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales");
                String id = reference.push().getKey();
                HashMap<String, Object> sucursalMap = new HashMap<>();
                Direccion direccion = new Direccion();
                direccion.setCalle(txtCalle.getText().toString().trim());
                if (!txtNumeroInterior.getText().toString().isEmpty()) {
                    direccion.setNumeroInterior(txtNumeroInterior.getText().toString().trim());
                }
                direccion.setNumeroExterior(txtNumeroExterior.getText().toString().trim());
                direccion.setZona(txtZona.getText().toString().trim());
                sucursalMap.put("idSucursal", id);
                sucursalMap.put("nombre", txtNombre.getText().toString().trim());
                sucursalMap.put("direccion", direccion);
                sucursalMap.put("eliminado", false);
                sucursalMap.put("botes", 0);
                reference.child(id).updateChildren(sucursalMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dismiss();
                    }
                });
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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

    private boolean validaCampo(TextInputLayout lyt, TextInputEditText txt, String error) {
        boolean isError;
        if (txt.getText().toString().isEmpty()) {
            isError = true;
            lyt.setError(error);
            txt.requestFocus();
        } else {
            isError = false;
            lyt.setError(null);
        }
        return !isError;
    }
}