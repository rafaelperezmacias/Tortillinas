package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Moldes.Cuenta;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

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

        ((ImageButton) view.findViewById(R.id.btn_cerrar))
                .setOnClickListener(view1 -> dismiss());

        ((Button) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    if (!isValidPassword() | !isValidPasswordNew() | !isValidPasswordRepeat()) {
                        return;
                    }

                    if (!txtPasswordNew.getText().toString().equals(txtPasswordRepeat.getText().toString())) {
                        lytPasswordRepeat.setError("Las contraseñas no coinciden");
                        return;
                    }

                    lytPasswordRepeat.setError(null);

                    DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("Cuentas")
                            .child(ControlSesiones.ObtenerUsuarioActivo(getContext()));
                    refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Cuenta c = dataSnapshot.getValue(Cuenta.class);
                            if (!c.getPassword().equals(txtPassword.getText().toString())) {
                                lytPassword.setError("La contraseña anterior no coincide");
                                return;
                            }
                            lytPassword.setError(null);
                            refUser.child("password").setValue(txtPasswordNew.getText().toString())
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "La contraseña se ha cambiado", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                        } else {
                                            Toast.makeText(getContext(), "Error, intentelo más tarde", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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

