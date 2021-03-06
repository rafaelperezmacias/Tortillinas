package com.zamnadev.tortillinas.BottomSheets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Dialogos.DialogoEmpleadoSucursal;
import com.zamnadev.tortillinas.Fragments.EmpleadosFragment;
import com.zamnadev.tortillinas.Moldes.Cuenta;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Nombre;
import com.zamnadev.tortillinas.Moldes.Sucursal;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.Encriptar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

public class EmpleadosBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private TextInputEditText txtSucursal;

    private boolean isError;

    private ArrayList<Sucursal> sucursals;

    private EmpleadosFragment empleadosFragment;

    private String nombre;
    private String apellidos;
    private String telefono;
    private Empleado empleado;
    private boolean isEditMode;

    private TextInputLayout lytPassword;
    private TextInputEditText txtPassword;

    private String msg = "";
    private Cuenta cuenta;

    public EmpleadosBottomSheet(EmpleadosFragment empleadosFragment)
    {
        this.empleadosFragment = empleadosFragment;
        isEditMode = false;
    }

    public EmpleadosBottomSheet(Empleado empleado, EmpleadosFragment empleadosFragment)
    {
        this.empleado = empleado;
        this.empleadosFragment = empleadosFragment;
        isEditMode = true;
        sucursals = new ArrayList<>();
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

        nombre = "";
        apellidos = "";
        telefono = "";

        TextView txtTitulo = view.findViewById(R.id.txtTitulo);

        TextInputLayout lytNombre = view.findViewById(R.id.lytNombre);
        TextInputLayout lytApellidos = view.findViewById(R.id.lytApellidos);
        TextInputLayout lytTelefono = view.findViewById(R.id.lytTelefono);
        TextInputLayout lytSucursal = view.findViewById(R.id.lytSucursal);
        TextInputLayout lytUsuario = view.findViewById(R.id.lytUsuario);
        lytPassword = view.findViewById(R.id.lytPassword);

        TextInputEditText txtNombre = view.findViewById(R.id.txtNombre);
        TextInputEditText txtApellidos = view.findViewById(R.id.txtApellidos);
        TextInputEditText txtTelefono = view.findViewById(R.id.txtTelefono);
        txtSucursal = view.findViewById(R.id.txtSucursal);
        TextInputEditText txtUsuario = view.findViewById(R.id.txtUsuario);
        txtPassword = view.findViewById(R.id.txtPassword);

        RadioButton rdbMostrador = view.findViewById(R.id.rdbMostrador);
        RadioButton rdbRepartidor = view.findViewById(R.id.rdbRepartidor);

        ((ImageButton) view.findViewById(R.id.btn_cerrar))
                .setOnClickListener((v -> dismiss()));

        txtSucursal.setOnClickListener(view12 -> {
            DialogoEmpleadoSucursal dialogoEmpleadoSucursal = new DialogoEmpleadoSucursal(getMe());
            dialogoEmpleadoSucursal.show(getFragmentManager(),dialogoEmpleadoSucursal.getTag());
        });

        //Eventos que escuchan los txt para generar un usario aleatoriamente atravez de ellos
        //para ello tomaremos un un nombre, _ , primera 2 letras del apellido (mayusculas) en caso de tener 2 se integraran, 2 ultimos numeros de telefono
        if (isEditMode) {
            txtTitulo.setText("Editar empleado");
            txtNombre.setText(empleado.getNombre().getNombres());
            txtNombre.setSelection(txtNombre.getText().length());
            txtApellidos.setText(empleado.getNombre().getApellidos());
            txtTelefono.setText(empleado.getTelefono());

            if (empleado.getTipo() == Empleado.TIPO_MOSTRADOR) {
                rdbMostrador.setChecked(true);
                rdbRepartidor.setChecked(false);
            } else {
                rdbMostrador.setChecked(false);
                rdbRepartidor.setChecked(true);
            }

            DatabaseReference refSucursal = FirebaseDatabase.getInstance().getReference("Sucursales");
            refSucursal.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Sucursal sucursal = snapshot.getValue(Sucursal.class);
                        for (int x = 0; x < empleado.getSucursales().size(); x++) {
                            if (sucursal.getIdSucursal().equals(empleado.getSucursales().get("s"+x))) {
                                if (msg.equals("")) {
                                    msg += sucursal.getNombre();
                                } else {
                                    msg += ", " + sucursal.getNombre();
                                }
                            }
                        }
                    }
                    txtSucursal.setText(msg);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //Consulta para obtener el usuario
            DatabaseReference refCuenta = FirebaseDatabase.getInstance().getReference("Cuentas")
                    .child(empleado.getIdEmpleado());
            refCuenta.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    cuenta = dataSnapshot.getValue(Cuenta.class);
                    txtUsuario.setText(cuenta.getUsuario());
                    String pass = null;
                    try {
                        pass = Encriptar.desencriptar(cuenta.getPassword());
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Ha ocurrido un error, inténtelo más tarde", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    txtPassword.setText(""+pass);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            txtTitulo.setText("Agregar empleado");
            txtNombre.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String tmPnombre = txtNombre.getText().toString().trim().toLowerCase();
                    //Quita los espacios de toda la cadena. el \\s es una expresion regular
                    nombre = tmPnombre.replaceAll("\\s","");
                    txtUsuario.setText(nombre +"_" + apellidos + telefono);
                    txtPassword.setText(txtUsuario.getText().toString().trim());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            txtApellidos.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    StringTokenizer token = new StringTokenizer(txtApellidos.getText().toString().trim()," ");
                    apellidos = "";
                    while (token.hasMoreTokens())
                    {
                        String tmpToken = token.nextToken();
                        if (tmpToken.length() > 1) {
                            apellidos += tmpToken.substring(0,2).toUpperCase();
                        } else {
                            apellidos += tmpToken.toUpperCase();
                        }
                    }
                    txtUsuario.setText(nombre +"_" + apellidos + telefono);
                    txtPassword.setText(txtUsuario.getText().toString().trim());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            txtTelefono.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String tmpTelefono = txtTelefono.getText().toString().trim();
                    if (tmpTelefono.length() < 3) {
                        telefono = tmpTelefono;
                    } else {
                        telefono = txtTelefono.getText().toString().trim().substring(txtTelefono.length()-2,txtTelefono.length());
                    }
                    txtUsuario.setText(nombre +"_" + apellidos + telefono);
                    txtPassword.setText(txtUsuario.getText().toString().trim());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        ((Button) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    isError = false;
                    //Valida si los campos estan vacios
                    if (!validaCampo(lytNombre,txtNombre,"Ingrese el nombre")
                        | !validaCampo(lytApellidos,txtApellidos,"Ingrese el apellido(s)")
                        | !validaCampo(lytTelefono,txtTelefono,"Ingrese el telefono")
                        | !validaCampo(lytSucursal, txtSucursal, "Ingrese la sucursal")
                        | !validaCampo(lytUsuario, txtUsuario,"Ingrese el usuario")
                        | !isValidPassword()) {
                        return;
                    }

                    //Valida en caso de hacer un cambio de usuario, si esto se cumple se edita la informacion
                    if (isEditMode && txtUsuario.getText().toString().equals(cuenta.getUsuario())) {
                        //Proceso de insercion de datos
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Empleados")
                                .child(empleado.getIdEmpleado());
                        HashMap<String,Object> empleadoMap = new HashMap<>();
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
                        if (sucursals.size() > 0) {
                            HashMap<String, String> sucursalesMap = new HashMap<>();
                            for (int x = 0; x < sucursals.size(); x++) {
                                sucursalesMap.put("s" + x,sucursals.get(x).getIdSucursal());
                            }
                            empleadoMap.put("sucursales", sucursalesMap);
                        }
                        empleadoMap.put("eliminado",false);

                        reference.updateChildren(empleadoMap)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful())
                                    {
                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Cuentas")
                                                .child(empleado.getIdEmpleado());
                                        HashMap<String, Object> cuentaMap = new HashMap<>();
                                        cuentaMap.put("usuario",txtUsuario.getText().toString().trim());
                                        String pass;
                                        try {
                                            pass = Encriptar.encriptar(txtPassword.getText().toString().trim());
                                        } catch (Exception ignored) {
                                            pass = "12345678a";
                                        }
                                        cuentaMap.put("password",pass);

                                        reference1.updateChildren(cuentaMap)
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        Toast.makeText(getContext(), "Actualización exitosa", Toast.LENGTH_SHORT).show();
                                                        dismiss();
                                                    } else {
                                                        Toast.makeText(getContext(), "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                    } else if (isEditMode) {
                        //Valida que solo exista un nombre de usuario
                        if (txtUsuario.getText().toString().contains(" ")) {
                            lytUsuario.setError("El nombre de usuario no puede contener espacios");
                            txtUsuario.requestFocus();
                            return;
                        }

                        Query reference = FirebaseDatabase.getInstance().getReference("Cuentas")
                                .orderByChild("usuario")
                                .equalTo(txtUsuario.getText().toString().trim());

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    lytUsuario.setError("El nombre de usuario ya existe");
                                    txtUsuario.requestFocus();
                                    return;
                                }

                                lytUsuario.setError(null);

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Empleados")
                                        .child(empleado.getIdEmpleado());
                                HashMap<String,Object> empleadoMap = new HashMap<>();
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
                                if (sucursals.size() > 0) {
                                    HashMap<String, String> sucursalesMap = new HashMap<>();
                                    for (int x = 0; x < sucursals.size(); x++) {
                                        sucursalesMap.put("s" + x,sucursals.get(x).getIdSucursal());
                                    }
                                    empleadoMap.put("sucursales", sucursalesMap);
                                }
                                empleadoMap.put("eliminado",false);

                                reference.updateChildren(empleadoMap)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful())
                                            {
                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Cuentas")
                                                        .child(empleado.getIdEmpleado());
                                                HashMap<String, Object> cuentaMap = new HashMap<>();
                                                cuentaMap.put("usuario",txtUsuario.getText().toString().trim());
                                                String pass;
                                                try {
                                                    pass = Encriptar.encriptar(txtPassword.getText().toString().trim());
                                                } catch (Exception ignored) {
                                                    pass = "12345678a";
                                                }
                                                cuentaMap.put("password",pass);

                                                reference1.updateChildren(cuentaMap)
                                                        .addOnCompleteListener(task1 -> {
                                                            if (task1.isSuccessful()) {
                                                                Toast.makeText(getContext(), "Actualización exitosa", Toast.LENGTH_SHORT).show();
                                                                empleadosFragment.notificarCambios();
                                                                dismiss();
                                                            } else {
                                                                Toast.makeText(getContext(), "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        //Proceso normal
                        if (txtUsuario.getText().toString().contains(" ")) {
                            lytUsuario.setError("El nombre de usuario no puede contener espacios");
                            txtUsuario.requestFocus();
                            return;
                        }

                        Query reference = FirebaseDatabase.getInstance().getReference("Cuentas")
                                .orderByChild("usuario")
                                .equalTo(txtUsuario.getText().toString().trim());

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    lytUsuario.setError("El nombre de usuario ya existe");
                                    txtUsuario.requestFocus();
                                    return;
                                }

                                lytUsuario.setError(null);

                                //Proceso de insercion de datos
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
                                HashMap<String, String> sucursalesMap = new HashMap<>();
                                for (int x = 0; x < sucursals.size(); x++) {
                                    sucursalesMap.put("s" + x,sucursals.get(x).getIdSucursal());
                                }
                                empleadoMap.put("sucursales", sucursalesMap);
                                empleadoMap.put("eliminado",false);

                                reference.child(id).updateChildren(empleadoMap)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful())
                                            {
                                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Cuentas");
                                                HashMap<String, Object> cuentaMap = new HashMap<>();
                                                cuentaMap.put("idCuenta",id);
                                                cuentaMap.put("usuario",txtUsuario.getText().toString().trim());
                                                String pass;
                                                try {
                                                    pass = Encriptar.encriptar(txtPassword.getText().toString().trim());
                                                } catch (Exception ignored) {
                                                    pass = "12345678a";
                                                }
                                                cuentaMap.put("password",pass);

                                                reference1.child(id).updateChildren(cuentaMap)
                                                        .addOnCompleteListener(task1 -> {
                                                            if (task1.isSuccessful()) {
                                                                Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                                                                empleadosFragment.notificarCambios();
                                                                dismiss();
                                                            } else {
                                                                Toast.makeText(getContext(), "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
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

    public void setTxtSucursal(ArrayList<Sucursal> sucursals) {
        this.sucursals = sucursals;
        if (sucursals.size() == 1) {
            txtSucursal.setText(sucursals.get(0).getNombre());
            return;
        }
        String msg = "";
        for (int x = 0; x < sucursals.size(); x++) {
            if (x == 0) {
                msg += sucursals.get(x).getNombre();
                continue;
            }
            msg += ", " + sucursals.get(x).getNombre();
        }
        txtSucursal.setText(msg);
    }

    private boolean isValidPassword() {
        if (txtPassword.getText().toString().isEmpty()) {
            lytPassword.setError("Ingrese la contraseña");
            isError = true;
            return false;
        }

        Log.e("dada","" + txtPassword.getText().toString().length());

        if (txtPassword.getText().toString().length() <= 7 || txtPassword.getText().toString().length() > 16) {
            lytPassword.setError("La contraseña debe de medir de 8 a 16 carácteres");
            isError = true;
            return false;
        }

        isError = false;
        lytPassword.setError(null);
        return true;
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
