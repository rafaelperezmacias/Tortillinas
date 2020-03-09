package com.zamnadev.tortillinas.BottomSheets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.zamnadev.tortillinas.Dialogos.DialogoEmpleadoSucursal;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Nombre;
import com.zamnadev.tortillinas.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class EmpleadosBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private TextInputEditText txtSucursal;

    private boolean isError;

    private String idSucursal;

    public EmpleadosBottomSheet()
    {

    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_empleados_bottom_sheet, null);
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

        TextInputLayout lytNombre = view.findViewById(R.id.lytNombre);
        TextInputLayout lytApellidos = view.findViewById(R.id.lytApellidos);
        TextInputLayout lytTelefono = view.findViewById(R.id.lytTelefono);
        TextInputLayout lytSucursal = view.findViewById(R.id.lytSucursal);
        TextInputLayout lytUsuario = view.findViewById(R.id.lytUsuario);
        TextInputLayout lytPassword = view.findViewById(R.id.lytPassword);

        TextInputEditText txtNombre = view.findViewById(R.id.txtNombre);
        TextInputEditText txtApellidos = view.findViewById(R.id.txtApellidos);
        TextInputEditText txtTelefono = view.findViewById(R.id.txtTelefono);
        txtSucursal = view.findViewById(R.id.txtSucursal);
        TextInputEditText txtUsuario = view.findViewById(R.id.txtUsuario);
        TextInputEditText txtPassword = view.findViewById(R.id.txtPassword);

        RadioButton rdbMostrador = view.findViewById(R.id.rdbMostrador);
        RadioButton rdbRepartidor = view.findViewById(R.id.rdbRepartidor);

        ((ImageButton) view.findViewById(R.id.btn_cerrar))
                .setOnClickListener((v -> dismiss()));

        txtSucursal.setOnClickListener(view12 -> {
            DialogoEmpleadoSucursal dialogoEmpleadoSucursal = new DialogoEmpleadoSucursal(getMe());
            dialogoEmpleadoSucursal.show(getFragmentManager(),dialogoEmpleadoSucursal.getTag());
        });

        ((Button) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    isError = false;
                    if (!validaCampo(lytNombre,txtNombre,"Ingrese el nombre")
                        | !validaCampo(lytApellidos,txtApellidos,"Ingrese el apellido(s)")
                        | !validaCampo(lytTelefono,txtTelefono,"Ingrese el telefono")
                        | !validaCampo(lytSucursal, txtSucursal, "Ingrese la sucursal")
                        | !validaCampo(lytUsuario, txtUsuario,"Ingrese el usuario")
                        | !validaCampo(lytPassword,txtPassword,"Ingrese la contraseña")) {
                        return;
                    }

                    //TODO Falta valicadion de usuario repetido, encriptacion de datos

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Empleados");

                    final String id = reference.push().getKey();
                    HashMap<String,Object> empleadoMap = new HashMap<>();
                    empleadoMap.put("idEmpleado",id);
                    Nombre nombre = new Nombre(txtNombre.getText().toString().trim(),txtApellidos.getText().toString().trim());
                    empleadoMap.put("nombre",nombre);
                    empleadoMap.put("telefono",txtTelefono.getText().toString().trim());
                    int tipo = 1;
                    if (rdbMostrador.isChecked())
                    {
                        tipo = Empleado.TIPO_MOSTRADOR;
                    } else if (rdbRepartidor.isChecked()) {
                        tipo = Empleado.TIPO_REPARTIDOR;
                    }
                    empleadoMap.put("tipo",tipo);
                    empleadoMap.put("idSucursal",idSucursal);
                    empleadoMap.put("eliminado",false);

                    reference.child(id).updateChildren(empleadoMap)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Cuentas");
                                    HashMap<String, Object> cuentaMap = new HashMap<>();
                                    cuentaMap.put("idCuenta",id);
                                    cuentaMap.put("usuario",txtUsuario.getText().toString().trim());
                                    cuentaMap.put("password",txtPassword.getText().toString().trim());

                                    reference1.child(id).updateChildren(cuentaMap)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                                                    dismiss();
                                                } else {
                                                    Toast.makeText(getContext(), "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                                                }
                                            });
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

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private EmpleadosBottomSheet getMe()
    {
        return this;
    }

    public void setTxtSucursal(String idSucursal, String nombre) {
        this.idSucursal = idSucursal;
        txtSucursal.setText(nombre);
    }

    private boolean validaCampo(TextInputLayout lyt,TextInputEditText txt, String error) {
        if (txt.getText().toString().isEmpty()) {
            lyt.setError(error);
            if (!isError) {
                txt.requestFocus();
                isError = true;
            }
            return false;
        }

        isError = false;
        lyt.setError(null);
        return true;
    }
}
