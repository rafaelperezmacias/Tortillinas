package com.zamnadev.tortillinas.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zamnadev.tortillinas.Moldes.Nombre;
import com.zamnadev.tortillinas.R;

import java.util.HashMap;

public class DialogoEmpleadoCuenta extends DialogFragment {

    public static DialogoEmpleadoCuenta newInstance(Nombre nombre, String telefono, int tipo, String idSucursal) {
        Bundle args = new Bundle();
        args.putString("telefono",telefono);
        args.putSerializable("nombre",nombre);
        args.putInt("tipo",tipo);
        args.putString("idSucursal",idSucursal);
        DialogoEmpleadoCuenta fragment = new DialogoEmpleadoCuenta();
        fragment.setArguments(args);
        return fragment;
    }

    public DialogoEmpleadoCuenta() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        final Nombre nombre = (Nombre) getArguments().getSerializable("nombre");
        final String telefono = getArguments().getString("telefono");
        final int tipo = getArguments().getInt("tipo");
        final String idSucursal = getArguments().getString("idSucursal");

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_empleado_cuenta,null);
        builder.setView(view);

        final TextInputLayout lytUsuario = view.findViewById(R.id.lytUsuario);
        final TextInputEditText txtUsuario = view.findViewById(R.id.txtUsuario);
        final TextInputLayout lytPassword = view.findViewById(R.id.lytPassword);
        final TextInputEditText txtPassword = view.findViewById(R.id.txtPassword);

        ((ImageView) view.findViewById(R.id.btnCerrar))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

        ((Button) view.findViewById(R.id.btnFinalizar))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!validarCamposVacio(txtUsuario,lytUsuario,"Rellene el campo") | !validarCamposVacio(txtPassword,lytPassword,"Rellene el campo")) {
                            return;
                        }

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Empleados");

                        final String id = reference.push().getKey();
                        HashMap<String,Object> empleadoMap = new HashMap<>();
                        empleadoMap.put("idEmpleado",id);
                        empleadoMap.put("nombre",nombre);
                        empleadoMap.put("telefono",telefono);
                        empleadoMap.put("tipo",tipo);
                        empleadoMap.put("idSucursal",idSucursal);
                        empleadoMap.put("eliminado",false);

                        reference.child(id).updateChildren(empleadoMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cuentas");
                                            HashMap<String, Object> cuentaMap = new HashMap<>();
                                            cuentaMap.put("idCuenta",id);
                                            cuentaMap.put("usuario",txtUsuario.getText().toString().trim());
                                            cuentaMap.put("password",txtPassword.getText().toString().trim());

                                            reference.child(id).updateChildren(cuentaMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                dismiss();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });

        ((Button) view.findViewById(R.id.btnCancelar))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

        ((Button) view.findViewById(R.id.btnAnterior))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

        return builder.create();
    }

    private boolean validarCamposVacio(TextInputEditText txt, TextInputLayout lyt, String error) {
        if (txt.getText().toString().isEmpty()) {
            lyt.setError(error);
            return false;
        }

        lyt.setError(null);
        return true;
    }
}
