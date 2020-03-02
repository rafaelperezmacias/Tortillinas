package com.zamnadev.tortillinas.Dialogos;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zamnadev.tortillinas.Moldes.Direccion;
import com.zamnadev.tortillinas.Moldes.Nombre;

import java.util.HashMap;

public class DialogoSucursalDomicilioOtros extends DialogoFormulario {

    public static DialogoSucursalDomicilioOtros newInstance(String nombre, Direccion direccion) {
        Bundle args = new Bundle();
        args.putString("nombre",nombre);
        args.putSerializable("direccion",direccion);
        DialogoSucursalDomicilioOtros fragment = new DialogoSucursalDomicilioOtros();
        fragment.setArguments(args);
        return fragment;
    }

    public DialogoSucursalDomicilioOtros() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        final String nombre = getArguments().getString("nombre");
        final Direccion direccion = (Direccion) getArguments().getSerializable("direccion");

        configuracionCampo1("Ingrese el codigo postal", InputType.TYPE_CLASS_NUMBER);
        configuracionCampo2("Ingrese la colonia",InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        configuracionCampo3("Ingrese el municipio",InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        configuracionTitulo("Agregar sucursal (3 de 3)");

        configuracionbtnOpcion1(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        },"Cancelar");

        configuracionbtnOpcion2(null,"Anterior",true);

        configuracionbtnOpcion3(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validaCampos("Rellene el campo codigo postal","Rellene el campo colonia","Rellene el campo municipio")) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales");

                    String id = reference.push().getKey();
                    HashMap<String,Object> clienteMap = new HashMap<>();
                    direccion.setCp(getCampo1());
                    direccion.setColonia(getCampo2());
                    direccion.setMunicipio(getCampo3());
                    clienteMap.put("idSucursal",id);
                    clienteMap.put("nombre",nombre);
                    clienteMap.put("direccion",direccion);
                    clienteMap.put("eliminado",false);

                    reference.child(id).updateChildren(clienteMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //Toast.makeText(getContext(), "Cliente añadido con exito", Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    } else {
                                        //Toast.makeText(getContext(), "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    dismiss();
                }
            }
        },"Finalizar");

        configuracionBtnCerrar(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return dialog;
    }

}
