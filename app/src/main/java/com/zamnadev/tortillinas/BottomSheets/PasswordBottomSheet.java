package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zamnadev.tortillinas.R;

public class PasswordBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private TextInputLayout lytPassword;
    private TextInputLayout lytPasswordNew;
    private TextInputLayout lytPasswordRepeat;

    private TextInputEditText txtPassword;
    private TextInputEditText txtPasswordNew;
    private TextInputEditText txtPasswordRepeat;

    public PasswordBottomSheet()
    {
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_password_bottom_sheet, null);
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

        lytPassword = view.findViewById(R.id.lytPassword);
        lytPasswordNew = view.findViewById(R.id.lytPasswordNew);
        lytPasswordRepeat = view.findViewById(R.id.lytPasswordRepeat);

        txtPassword = view.findViewById(R.id.txtPassword);
        txtPasswordNew = view.findViewById(R.id.txtPasswordNew);
        txtPasswordRepeat = view.findViewById(R.id.txtPasswordRepeat);

        ((Button) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    if (!isValidPassword() | !isValidPasswordNew() | !isValidPasswordRepeat()) {
                        return;
                    }

                    ProgressDialog dialog = new ProgressDialog(getContext());
                    dialog.setMessage("Actualizando contraseña");
                    dialog.show();
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

    private boolean isValidPassword() {
        if (txtPassword.getText().toString().isEmpty()) {
            lytPassword.setError("Ingrese la contraseña");
            return false;
        }

        lytPassword.setError(null);
        return true;
    }

    private boolean isValidPasswordNew() {
        if (txtPasswordNew.getText().toString().isEmpty()) {
            lytPasswordNew.setError("Ingrese la nueva contraseña");
            return false;
        }

        if (txtPasswordNew.getText().toString().length() <= 7 || txtPasswordNew.getText().toString().length() > 16) {
            lytPasswordNew.setError("La contraseña debe de medir de 8 a 16 carácteres");
            return false;
        }

        lytPasswordNew.setError(null);
        return true;
    }

    private boolean isValidPasswordRepeat() {
        if (txtPasswordRepeat.getText().toString().isEmpty()) {
            lytPasswordRepeat.setError("Ingrese la nueva contraseña");
            return false;
        }

        if (txtPasswordRepeat.getText().toString().length() <= 7 || txtPasswordRepeat.getText().toString().length() > 16) {
            lytPasswordRepeat.setError("La contraseña debe de medir de 8 a 16 carácteres");
            return false;
        }

        lytPasswordRepeat.setError(null);
        return true;
    }
}

