package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zamnadev.tortillinas.R;

public abstract class UnCampoBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;
    private boolean isError;

    private TextInputLayout lytCampo;
    private TextInputEditText txtCampo;
    private Button btnAccion;
    private TextView txtTitulo;

    public UnCampoBottomSheet()
    {

    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.un_campo_bottom_sheet, null);
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

        lytCampo = view.findViewById(R.id.lytCampo);
        txtCampo = view.findViewById(R.id.txtCampo);
        txtTitulo = view.findViewById(R.id.txtTitulo);
        btnAccion = view.findViewById(R.id.btnGuardar);


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

    public void setTitulo(String titulo) {
        txtTitulo.setText(titulo);
    }

    public void setCampo(String hint, String text, int tipo) {
        lytCampo.setHint(hint);
        txtCampo.setText(text);
        txtCampo.setInputType(tipo);
    }

    public void setBoton(String text, View.OnClickListener listener) {
        btnAccion.setText(text);
        btnAccion.setOnClickListener(listener);
    }

    public boolean validacion(String error) {
        return validaCampo(lytCampo, txtCampo, error);
    }

    public TextInputEditText getTxtCampo() {
        return txtCampo;
    }

    private boolean validaCampo(TextInputLayout lyt, TextInputEditText txt, String error) {
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