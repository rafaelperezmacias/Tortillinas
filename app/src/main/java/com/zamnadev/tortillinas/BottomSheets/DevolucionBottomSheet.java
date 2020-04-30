package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Dialogs.MessageDialog;
import com.zamnadev.tortillinas.Dialogs.MessageDialogBuilder;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Vuelta;
import com.zamnadev.tortillinas.Notificaciones.Client;
import com.zamnadev.tortillinas.Notificaciones.Data;
import com.zamnadev.tortillinas.Notificaciones.FCMServiceAPI;
import com.zamnadev.tortillinas.Notificaciones.MyResponse;
import com.zamnadev.tortillinas.Notificaciones.Sender;
import com.zamnadev.tortillinas.Notificaciones.Token;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DevolucionBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private double tortilla;
    private double masa;
    private double totopos;

    public DevolucionBottomSheet(double tortilla, double masa, double totopos)
    {
        this.tortilla = tortilla;
        this.masa = masa;
        this.totopos = totopos;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_ventas_cliente_bottom_sheet, null);
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (BottomSheetBehavior.STATE_HIDDEN == i) dismiss();
                if (BottomSheetBehavior.STATE_DRAGGING == i) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });

        TextInputEditText txtTortilla = view.findViewById(R.id.txtTortilla);
        TextInputEditText txtMasa = view.findViewById(R.id.txtMasa);
        TextInputEditText txtTotopos = view.findViewById(R.id.txtTotopos);

        ImageView icon1 = view.findViewById(R.id.icon1);
        ImageView icon2 = view.findViewById(R.id.icon2);
        ImageView icon3 = view.findViewById(R.id.icon3);

        RelativeLayout layout1 = view.findViewById(R.id.layout1);
        RelativeLayout layout2 = view.findViewById(R.id.layout2);
        RelativeLayout layout3 = view.findViewById(R.id.layout3);

        hideTxt(txtMasa);
        hideTxt(txtTortilla);
        hideTxt(txtTotopos);

        if (tortilla > 0.0) {
            txtTortilla.setText("" + tortilla + " kgs");
        } else {
            layout1.setVisibility(View.GONE);
        }

        if (masa > 0.0) {
            txtMasa.setText("" + masa + " kgs");
            if (layout1.getVisibility() == View.GONE) {
                icon2.setVisibility(View.VISIBLE);
            }
        } else {
            layout2.setVisibility(View.GONE);
        }

        if (totopos > 0.0) {
            txtTotopos.setText("" + totopos + " kgs");
            if (layout2.getVisibility() == View.GONE) {
                icon3.setVisibility(View.VISIBLE);
            }
        } else {
            layout3.setVisibility(View.GONE);
        }

        ((TextView) view.findViewById(R.id.txtTitulo))
                .setText("Devoluciones");

        ((TextView) view.findViewById(R.id.txtSubTitulo))
                .setVisibility(View.GONE);

        ((MaterialButton) view.findViewById(R.id.btnGuardar))
                .setText("CERRAR");

        ((MaterialButton) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                   dismiss();
                });

        ((ImageButton) view.findViewById(R.id.btn_cerrar))
                .setOnClickListener(view1 -> {
                    dismiss();
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

    private void hideTxt(TextInputEditText txt) {
        txt.setClickable(false);
        txt.setFocusable(false);
        txt.setLongClickable(false);
        txt.setCursorVisible(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
