package com.zamnadev.tortillinas.Dialogos;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.util.HashMap;

public class DialogoAddCampoVentas extends DialogFragment {

    public static final int TIPO_GASTOS = 1000;
    public static final int TIPO_VENTAS_MOSTRADOR = 1001;

    private String idVenta;
    private int tipo;

    public DialogoAddCampoVentas(int tipo, String idVenta)
    {
        this.tipo = tipo;
        this.idVenta = idVenta;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialogo_add_campo_ventas, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputEditText txtNombre = view.findViewById(R.id.txt_nombre);
        TextInputLayout lytNombre = view.findViewById(R.id.til_nombre);
        TextInputEditText txtPrecio = view.findViewById(R.id.txt_precio);
        TextInputLayout lytPrecio = view.findViewById(R.id.til_precio);

        ((MaterialButton) view.findViewById(R.id.btn_dialog_secondary))
                .setOnClickListener(view1 -> dismiss());

        ((MaterialButton) view.findViewById(R.id.btn_dialog_primary))
                .setOnClickListener(view1 -> {
                    if (!validaCampo(lytNombre,txtNombre,"Ingrese el nombre")
                        | !validaCampo(lytPrecio,txtPrecio,"Ingrese el precio")) {
                        return;
                    }
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("nombre",txtNombre.getText().toString().trim());
                    hashMap.put("precio",Double.parseDouble(txtPrecio.getText().toString()));
                    if (tipo == TIPO_GASTOS) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Gastos")
                                .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                                .child(idVenta);
                        String id = reference.push().getKey();
                        hashMap.put("id",id);
                        reference.child(id)
                                .updateChildren(hashMap)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        dismiss();
                                    }
                                });
                    } else if (tipo == TIPO_VENTAS_MOSTRADOR) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mostrador")
                                .child(ControlSesiones.ObtenerUsuarioActivo(getContext()))
                                .child(idVenta);
                        String id = reference.push().getKey();
                        hashMap.put("id",id);
                        reference.child(id)
                                .updateChildren(hashMap)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        dismiss();
                                    }
                                });
                    }
        });

        return view;
    }

    private boolean validaCampo(TextInputLayout lyt,TextInputEditText txt, String error) {
        if (txt.getText().toString().isEmpty()) {
            lyt.setError(error);
            return false;
        }

        lyt.setError(null);
        return true;
    }
}