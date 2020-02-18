package com.zamnadev.tortillinas.Dialogos;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zamnadev.tortillinas.Moldes.Direccion;
import com.zamnadev.tortillinas.Moldes.Nombre;

public class DialogoClienteDomicioCalle extends DialogoFormulario {

    public static DialogoClienteDomicioCalle newInstance(Nombre nombre, String telefono) {
        Bundle args = new Bundle();
        args.putSerializable("nombre",nombre);
        args.putString("telefono",telefono);
        DialogoClienteDomicioCalle fragment = new DialogoClienteDomicioCalle();
        fragment.setArguments(args);
        return fragment;
    }

    public DialogoClienteDomicioCalle() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        final Nombre nombre = (Nombre) getArguments().getSerializable("nombre");
        final String telefono = getArguments().getString("telefono");

        configuracionCampo1("Ingrese la calle", InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        configuracionCampo2("Ingrese el numero exterior",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_CLASS_TEXT);
        configuracionCampo3("Ingrese el numero interior (Opcional)",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_CLASS_TEXT);

        configuracionTitulo("Agregar cliente (2 de 3)");

        configuracionbtnOpcion1(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        },"Cancelar");

        configuracionbtnOpcion2(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        },"Anterior",true);

        configuracionbtnOpcion3(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validaCampos("Rellene el campo calle","Rellene el campo numero exterior")) {
                    Direccion direccion = new Direccion(getCampo1(),getCampo2(),getCampo3());
                    DialogoClienteDomicioOtros dialogoClienteDomicioOtros = DialogoClienteDomicioOtros.newInstance(nombre,telefono,direccion);
                    dialogoClienteDomicioOtros.show(getActivity().getSupportFragmentManager(),"DialogoClienteDomicioOtros");
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
