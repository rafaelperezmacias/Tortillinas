package com.zamnadev.tortillinas.BottomSheets;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.zamnadev.tortillinas.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class VentasRepartidorBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private TextInputEditText txtFecha;

    private boolean isEditable;

    public VentasRepartidorBottomSheet()
    {

    }

    public VentasRepartidorBottomSheet(boolean isEditable)
    {
        this.isEditable = isEditable;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_ventas_repartidor_bottom_sheet, null);
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
        ImageButton btnCerrar = view.findViewById(R.id.btn_cerrar);
        btnCerrar.setOnClickListener((v -> dismiss()));
        txtFecha = view.findViewById(R.id.txt_fecha);
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view1, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            txtFecha.setText(sdf.format(calendar.getTime()));
        };
        txtFecha.setOnClickListener((v) -> new DatePickerDialog(getContext(), date,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());
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
}