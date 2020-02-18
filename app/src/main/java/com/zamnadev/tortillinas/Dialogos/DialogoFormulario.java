package com.zamnadev.tortillinas.Dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zamnadev.tortillinas.R;

public abstract class DialogoFormulario extends DialogFragment {

    private TextInputLayout lytCampo1;
    private TextInputEditText txtCampo1;
    private TextInputLayout lytCampo2;
    private TextInputEditText txtCampo2;
    private TextInputLayout lytCampo3;
    private TextInputEditText txtCampo3;
    private Button btnOpcion1;
    private Button btnOpcion2;
    private Button btnOpcion3;
    private TextView txtTitulo;
    private ImageView btnCerrar;

    public DialogoFormulario() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_formulario,null);
        builder.setView(view);

        lytCampo1 = view.findViewById(R.id.lytCampo1);
        txtCampo1 = view.findViewById(R.id.txtCampo1);
        lytCampo2 = view.findViewById(R.id.lytCampo2);
        txtCampo2 = view.findViewById(R.id.txtCampo2);
        lytCampo3 = view.findViewById(R.id.lytCampo3);
        txtCampo3 = view.findViewById(R.id.txtCampo3);
        btnOpcion1 = view.findViewById(R.id.btnOpcion1);
        btnOpcion2 = view.findViewById(R.id.btnOpcion2);
        btnOpcion3 = view.findViewById(R.id.btnOpcion3);
        txtTitulo = view.findViewById(R.id.txtTitulo);
        btnCerrar = view.findViewById(R.id.btnCerrar);

        return builder.create();
    }

    private boolean validaCampo(TextInputEditText txt,TextInputLayout lyt, String advertencia) {
        if (txt.getText().toString().isEmpty()) {
            lyt.setError(advertencia);
            return false;
        }
        lyt.setError(null);
        return true;
    }

    public boolean validaCampos(String error1, String error2) {
        if (!validaCampo(txtCampo1,lytCampo1,error1)
                | !validaCampo(txtCampo2,lytCampo2,error2)) {
            return false;
        }
        return true;
    }

    public boolean validaCampos(String error1, String error2, String error3) {
        if (!validaCampo(txtCampo1,lytCampo1,error1)
                | !validaCampo(txtCampo2,lytCampo2,error2)
                | !validaCampo(txtCampo3,lytCampo3,error3)) {
            return false;
        }
        return true;
    }

    public void configuracionTitulo(String titulo) {
        txtTitulo.setText(titulo);
    }

    public void configuracionbtnOpcion1(View.OnClickListener listener, String text) {
        btnOpcion1.setOnClickListener(listener);
        btnOpcion1.setText(text);
    }

    public void configuracionbtnOpcion2(View.OnClickListener listener, String text, boolean visible) {
        btnOpcion2.setOnClickListener(listener);
        btnOpcion2.setText(text);
        if (visible) {
            btnOpcion2.setVisibility(View.VISIBLE);
        } else {
            btnOpcion2.setVisibility(View.GONE);
        }
    }

    public void configuracionbtnOpcion3(View.OnClickListener listener, String text) {
        btnOpcion3.setOnClickListener(listener);
        btnOpcion3.setText(text);
    }

    public void configuracionBtnCerrar(View.OnClickListener listener) {
        btnCerrar.setOnClickListener(listener);
    }

    public void configuracionCampo1(String hint, int tipo) {
        lytCampo1.setHint(hint);
        txtCampo1.setInputType(tipo);
    }

    public void configuracionCampo2(String hint, int tipo) {
        lytCampo2.setHint(hint);
        txtCampo2.setInputType(tipo);
    }

    public void configuracionCampo3(String hint, int tipo) {
        lytCampo3.setHint(hint);
        txtCampo3.setInputType(tipo);
    }

    public String getCampo1() {
        return txtCampo1.getText().toString();
    }

    public String getCampo2() {
        return txtCampo2.getText().toString();
    }

    public String getCampo3() {
        return txtCampo3.getText().toString();
    }
}
