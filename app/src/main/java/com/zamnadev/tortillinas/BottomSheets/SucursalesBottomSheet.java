package com.zamnadev.tortillinas.BottomSheets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.zamnadev.tortillinas.Moldes.Direccion;
import com.zamnadev.tortillinas.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SucursalesBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private boolean isError;

    public SucursalesBottomSheet()
    {

    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_sucursales_bottom_sheet, null);
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

        ((ImageButton) view.findViewById(R.id.btn_cerrar))
                .setOnClickListener((v -> dismiss()));

        TextInputLayout lytNombre = view.findViewById(R.id.lytNombre);
        TextInputLayout lytCalle = view.findViewById(R.id.lytCalle);
        TextInputLayout lytNumeroExterior = view.findViewById(R.id.lytNumeroExterior);
        TextInputLayout lytNumeroInterior = view.findViewById(R.id.lytNumeroInterior);
        TextInputLayout lytCodigoPostal = view.findViewById(R.id.lytCodigoPostal);
        TextInputLayout lytColonia = view.findViewById(R.id.lytColonia);
        TextInputLayout lytMunicipio = view.findViewById(R.id.lytMunicipio);

        TextInputEditText txtNombre = view.findViewById(R.id.txtNombre);
        TextInputEditText txtCalle = view.findViewById(R.id.txtCalle);
        TextInputEditText txtNumeroExterior = view.findViewById(R.id.txtNumeroExterior);
        TextInputEditText txtNumeroInterior = view.findViewById(R.id.txtNumeroInterior);
        TextInputEditText txtCodigoPostal = view.findViewById(R.id.txtCodigoPostal);
        TextInputEditText txtColonia = view.findViewById(R.id.txtColonia);
        TextInputEditText txtMunicipio = view.findViewById(R.id.txtMunicipio);

        ((Button) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    isError = false;
                    if (!validaCampo(lytNombre,txtNombre,"Ingrese el nombre")
                        | !validaCampo(lytCalle,txtCalle,"Ingrese la calle")
                        | !validaCampo(lytNumeroExterior,txtNumeroExterior,"Ingrese el numero exterior")
                        | !validaCampo(lytCodigoPostal,txtCodigoPostal,"Ingrese el código postal")
                        | !validaCampo(lytColonia,txtColonia,"Ingrese la colonia")
                        | !validaCampo(lytMunicipio,txtMunicipio,"Ingrese el municipio"))  {
                        return;
                    }

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Sucursales");

                    String id = reference.push().getKey();
                    HashMap<String,Object> clienteMap = new HashMap<>();
                    Direccion direccion = new Direccion();
                    direccion.setCalle(txtCalle.getText().toString().trim());
                    if (!txtNumeroInterior.getText().toString().isEmpty()) {
                        direccion.setNumeroInterior(txtNumeroInterior.getText().toString().trim());
                    }
                    direccion.setNumeroExterior(txtNumeroExterior.getText().toString().trim());
                    direccion.setCp(txtCodigoPostal.getText().toString().trim());
                    direccion.setColonia(txtColonia.getText().toString().trim());
                    direccion.setMunicipio(txtMunicipio.getText().toString().trim());
                    clienteMap.put("idSucursal",id);
                    clienteMap.put("nombre",txtNombre.getText().toString().trim());
                    clienteMap.put("direccion",direccion);
                    clienteMap.put("eliminado",false);

                    reference.child(id).updateChildren(clienteMap)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                } else {
                                    Toast.makeText(getContext(), "Error, intentelo mas tarde", Toast.LENGTH_SHORT).show();
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

