package com.zamnadev.tortillinas.Sucursales;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zamnadev.tortillinas.BottomSheets.UnCampoBottomSheet;
import com.zamnadev.tortillinas.Moldes.Sucursal;

public class EditarCubetasSucursalBottomSheet extends UnCampoBottomSheet {

    private Sucursal sucursal;

    public EditarCubetasSucursalBottomSheet(Sucursal sucursal)
    {
        this.sucursal = sucursal;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setTitulo("Editar cubetas");
        setCampo("Cubetas", ""+sucursal.getBotes(), InputType.TYPE_CLASS_NUMBER);
        getTxtCampo().setSelection(getTxtCampo().getText().length());
        setBoton("Guardar", view1 -> {
            if (!validacion("Ingrese las cubetas")) {
                return;
            }

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales")
                    .child(sucursal.getIdSucursal()).child("botes");

            int tmpCubetas = Integer.parseInt(getTxtCampo().getText().toString().trim());

            reference.setValue(tmpCubetas)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Acutalización exitosa", Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else {
                            Toast.makeText(getContext(), "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return view;
    }
}
