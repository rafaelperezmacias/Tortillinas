package com.zamnadev.tortillinas.Sucursales;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zamnadev.tortillinas.BottomSheets.UnCampoBottomSheet;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;

public class AddCubetasSucursalBottomSheet extends UnCampoBottomSheet {

    private Sucursal sucursal;

    public AddCubetasSucursalBottomSheet(Sucursal sucursal)
    {
        this.sucursal = sucursal;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);

        setTitulo("Añadir cubetas");
        setCampo("Cubetas",null,InputType.TYPE_CLASS_NUMBER);
        setBoton("Añadir", view1 -> {
            if (!validacion("Ingrese las cubetas")) {
                return;
            }
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales")
                    .child(sucursal.getIdSucursal());

            int tmpBotes = Integer.parseInt(getTxtCampo().getText().toString());

            sucursal.setBotes(sucursal.getBotes() + tmpBotes);
            reference.child("botes").setValue(sucursal.getBotes())
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