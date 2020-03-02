package com.zamnadev.tortillinas.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.zamnadev.tortillinas.R;

public class DialogoEmpleadoSucursal extends DialogFragment {

    public static DialogoEmpleadoSucursal newInstance() {

        Bundle args = new Bundle();

        DialogoEmpleadoSucursal fragment = new DialogoEmpleadoSucursal();
        fragment.setArguments(args);
        return fragment;
    }

    public DialogoEmpleadoSucursal() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_empleado_sucursal,null);
        builder.setView(view);

        return builder.create();
    }
}
