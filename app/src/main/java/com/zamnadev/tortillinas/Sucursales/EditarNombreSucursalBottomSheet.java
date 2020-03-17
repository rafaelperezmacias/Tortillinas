package com.zamnadev.tortillinas.Sucursales;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zamnadev.tortillinas.BottomSheets.UnCampoBottomSheet;
import com.zamnadev.tortillinas.Moldes.Sucursal;

public class EditarNombreSucursalBottomSheet extends UnCampoBottomSheet {

    private Sucursal sucursal;

    public EditarNombreSucursalBottomSheet(Sucursal sucursal)
    {
        this.sucursal = sucursal;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setTitulo("Editar nombre");
        setCampo("Nombre", "" + sucursal.getNombre(), InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        getTxtCampo().setSelection(getTxtCampo().getText().length());
        setBoton("Guardar", view1 -> {
            if (!validacion("Ingrese el nuevo nombre")) {
                return;
            }

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales")
                    .child(sucursal.getIdSucursal()).child("nombre");

            reference.setValue(getTxtCampo().getText().toString().trim())
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
