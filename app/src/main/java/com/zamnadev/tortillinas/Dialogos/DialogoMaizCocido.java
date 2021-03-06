package com.zamnadev.tortillinas.Dialogos;

import android.content.Context;
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
import java.util.Objects;

public class DialogoMaizCocido extends DialogFragment {

    private int botesAnteriores;
    private int costalesAnteriores;
    private String idVenta;
    private boolean isEditable;
    private String idEmpleado;

    public DialogoMaizCocido(String idVenta,int costalesAnteriores, int botesAnteriores, boolean isEditable, String idEmpleado)
    {
        this.costalesAnteriores = costalesAnteriores;
        this.botesAnteriores = botesAnteriores;
        this.idVenta = idVenta;
        this.isEditable = isEditable;
        this.idEmpleado = idEmpleado;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialgo_maiz_cocido, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputEditText txtCostales = view.findViewById(R.id.txtCostales);
        TextInputEditText txtBotes = view.findViewById(R.id.txtBotes);

        if (botesAnteriores < 0) {
            txtBotes.setText("");
        } else {
            txtBotes.setText("" + botesAnteriores);
        }

        if (costalesAnteriores < 0) {
            txtCostales.setText("");
        } else {
            txtCostales.setText("" + costalesAnteriores);
        }

        ((MaterialButton) view.findViewById(R.id.btn_dialog_secondary))
                .setOnClickListener(view1 -> dismiss());

        if (!isEditable) {
            ((MaterialButton) view.findViewById(R.id.btn_dialog_secondary))
                    .setVisibility(View.GONE);
            ((MaterialButton) view.findViewById(R.id.btn_dialog_primary))
                    .setText("CERRAR");
            ((TextView) view.findViewById(R.id.tv_dialog_title))
                    .setText("Maíz agregado");
            hideTxt(txtBotes);
            hideTxt(txtCostales);
        }

        ((MaterialButton) view.findViewById(R.id.btn_dialog_primary))
                .setOnClickListener(view1 -> {
                    if (!isEditable) {
                        dismiss();
                        return;
                    }
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("costales",-1);
                    hashMap.put("botes",-1);
                    if (!txtCostales.getText().toString().isEmpty()) {
                        hashMap.put("costales",Integer.parseInt(txtCostales.getText().toString()));
                    }
                    if (!txtBotes.getText().toString().isEmpty()) {
                        hashMap.put("botes",Integer.parseInt(txtBotes.getText().toString()));
                    }
                    FirebaseDatabase.getInstance().getReference("VentasMostrador")
                            .child(idEmpleado)
                            .child(idVenta)
                            .updateChildren(hashMap)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    dismiss();
                                }
                            });
                });

        return view;
    }

    private void hideTxt(TextInputEditText txt) {
        txt.setClickable(false);
        txt.setLongClickable(false);
        txt.setFocusable(false);
        txt.setCursorVisible(false);
    }
}
