package com.zamnadev.tortillinas.Dialogos;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zamnadev.tortillinas.Moldes.Nombre;

public class DialogoEmpleadoDatos extends DialogoFormulario {

    public DialogoEmpleadoDatos() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        configuracionCampo1("Ingrese el nombre(s)", InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        configuracionCampo2("Ingrese el apellido(s)",InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        configuracionCampo3("Ingrese el numero telefonico",InputType.TYPE_CLASS_NUMBER);

        configuracionTitulo("Agregar empleado (1 de 3)");

        configuracionbtnOpcion1(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        },"Cancelar");

        configuracionbtnOpcion2(null,"Anterior",false);

        configuracionbtnOpcion3(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validaCampos("Rellene el campo nombre","Rellene el campo apellido","Rellene el campo telefono")) {
                    Nombre nombre = new Nombre(getCampo1(),getCampo2());
                    DialogoEmpleadoSucursal dialogoEmpleadoSucursal = DialogoEmpleadoSucursal.newInstance(nombre,getCampo3());
                    dialogoEmpleadoSucursal.show(getFragmentManager(),"DialogoEmpleadoSucursal");
                    dialogoEmpleadoSucursal.setCancelable(false);
                    dismiss();
                }
            }
        },"Siguiente");

        configuracionBtnCerrar(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return dialog;
    }
}
