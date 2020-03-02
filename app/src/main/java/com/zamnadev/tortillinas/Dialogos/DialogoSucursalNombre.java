package com.zamnadev.tortillinas.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zamnadev.tortillinas.R;

public class DialogoSucursalNombre extends DialogFragment {

    public DialogoSucursalNombre() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_sucursal_nombre,null);
        builder.setView(view);

        final TextInputLayout lytNombre = view.findViewById(R.id.lytNombre);
        final TextInputEditText txtNombre = view.findViewById(R.id.txtNombre);

        ((Button) view.findViewById(R.id.btnCancelar))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

        ((Button) view.findViewById(R.id.btnSiguiente))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (txtNombre.getText().toString().isEmpty()) {
                            lytNombre.setError("Rellene el campo");
                            return;
                        }

                        lytNombre.setError(null);
                        DialogoSucursalDomicilioCalle dialogoSucursalDomicilioCalle = DialogoSucursalDomicilioCalle.newInstance(txtNombre.getText().toString().trim());
                        dialogoSucursalDomicilioCalle.show(getFragmentManager(),"DialogoSucursalDomicilioCalle");
                        dismiss();
                    }
                });


        return builder.create();
    }
}
