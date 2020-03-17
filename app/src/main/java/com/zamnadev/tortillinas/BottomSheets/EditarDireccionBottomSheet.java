package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zamnadev.tortillinas.Moldes.Direccion;
import com.zamnadev.tortillinas.R;

import java.util.HashMap;

public class EditarDireccionBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private int tipo;
    private String idPadre;
    private Direccion direccion;

    public EditarDireccionBottomSheet(int tipo, String idPadre, Direccion direccion)
    {
        this.tipo = tipo;
        this.idPadre = idPadre;
        this.direccion = direccion;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_editar_direccion_bottom_sheet, null);
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

        TextInputEditText txtCalle = view.findViewById(R.id.txtCalle);
        TextInputEditText txtNumeroExterior = view.findViewById(R.id.txtNumeroExterior);
        TextInputEditText txtNumeroInterior = view.findViewById(R.id.txtNumeroInterior);
        TextInputEditText txtZona = view.findViewById(R.id.txtZona);

        txtCalle.setText(direccion.getCalle());
        ponerCursor(txtCalle);
        txtNumeroExterior.setText(direccion.getNumeroExterior());
        ponerCursor(txtNumeroExterior);
        if (direccion.getNumeroInterior() != null) {
            txtNumeroInterior.setText(direccion.getNumeroInterior());
            ponerCursor(txtNumeroInterior);
        }
        txtZona.setText(direccion.getZona());
        ponerCursor(txtZona);

        ((Button) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {

                    DatabaseReference reference;

                    if (tipo == 1) {
                        reference = FirebaseDatabase.getInstance().getReference("Sucursales").child(idPadre).child("direccion");
                    } else {
                        reference = FirebaseDatabase.getInstance().getReference("Clientes").child(idPadre).child("direccion");
                    }

                    direccion.setCalle(txtCalle.getText().toString().trim());
                    direccion.setNumeroExterior(txtNumeroExterior.getText().toString().trim());
                    direccion.setNumeroInterior(txtNumeroInterior.getText().toString().trim());
                    direccion.setZona(txtZona.getText().toString().trim());

                    reference.setValue(direccion)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Acutalización exitosa", Toast.LENGTH_SHORT).show();
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

    private void ponerCursor(TextInputEditText txt) {
        txt.setSelection(txt.getText().length());
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}