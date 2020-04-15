package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.zamnadev.tortillinas.Moldes.AuxVenta;
import com.zamnadev.tortillinas.Moldes.CambioPrecios;
import com.zamnadev.tortillinas.Moldes.Cliente;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.Producto;
import com.zamnadev.tortillinas.Moldes.Vuelta;
import com.zamnadev.tortillinas.Notificaciones.Client;
import com.zamnadev.tortillinas.Notificaciones.Data;
import com.zamnadev.tortillinas.Notificaciones.FCMServiceAPI;
import com.zamnadev.tortillinas.Notificaciones.MyResponse;
import com.zamnadev.tortillinas.Notificaciones.Sender;
import com.zamnadev.tortillinas.Notificaciones.Token;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VueltaBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private boolean primero;
    private String idVenta;
    private Empleado empleado;
    private FCMServiceAPI api;
    private Context context;

    public VueltaBottomSheet(boolean primero, Empleado empleado, String idVenta, Context context)
    {
        this.primero = primero;
        this.empleado = empleado;
        this.idVenta = idVenta;
        api = Client.getClient("https://fcm.googleapis.com/").create(FCMServiceAPI.class);
        this.context = context;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_vuelta_bottom_sheet, null);
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
        TextInputLayout lytTortilla = view.findViewById(R.id.lytTortilla);
        TextInputEditText txtMasa = view.findViewById(R.id.txtMasa);
        TextInputLayout lytMasa = view.findViewById(R.id.lytMasa);

        String titulo = "";
        if (primero) {
            titulo += "Primer ";
        } else {
            titulo += "Segunda ";
        }

        titulo += "vuelta";

        ((TextView) view.findViewById(R.id.txtTitulo))
                .setText(titulo);

        ((TextView) view.findViewById(R.id.txtSubTitulo))
                .setText(empleado.getNombre().getNombres() + " " + empleado.getNombre().getApellidos());

        ((MaterialButton) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    if (!validaCampo(lytTortilla,txtTortilla,"Ingrese la cantidad de tortillas (kg)")
                    | !validaCampo(lytMasa,txtMasa,"Ingrese la cantidad de masa (kg)")) {
                        return;
                    }
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("masa",Double.parseDouble(txtMasa.getText().toString()));
                    hashMap.put("tortillas",Double.parseDouble(txtTortilla.getText().toString()));
                    hashMap.put("confirmado",false);
                    hashMap.put("registrada",true);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                            .child(idVenta)
                            .child(empleado.getIdEmpleado());
                    if (primero) {
                        reference.child("vuelta1").updateChildren(hashMap)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        enviarNotificacion(empleado.getIdEmpleado());
                                        dismiss();
                                    }
                                });
                    } else {
                        reference.child("vuelta2").updateChildren(hashMap)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        enviarNotificacion(empleado.getIdEmpleado());
                                        dismiss();
                                    }
                                });
                    }
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


    //TODO inicia el proceso de comunicacion con el repartidor
    private void enviarNotificacion(String idRepartidor) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("idVenta",idVenta);
        hashMap.put("idRepartidor",idRepartidor);
        hashMap.put("primero",primero);
        hashMap.put("visto",false);
        hashMap.put("confirmado",false);
        hashMap.put("hora", ServerValue.TIMESTAMP);
        if (primero) {
            FirebaseDatabase.getInstance().getReference("Confirmaciones")
                    .child(idRepartidor)
                    .child(idVenta)
                    .child("vuelta1")
                    .updateChildren(hashMap)
                    .addOnCompleteListener(task -> {
                        enviarMensaje(idRepartidor);
                    });
        } else {
            FirebaseDatabase.getInstance().getReference("Confirmaciones")
                    .child(idRepartidor)
                    .child(idVenta)
                    .child("vuelta2")
                    .updateChildren(hashMap)
                    .addOnCompleteListener(task -> {
                        enviarMensaje(idRepartidor);
                    });
        }
    }

    //Para enviar un mensaje ocupasmos el id del receptor
    private void enviarMensaje(final String receptor) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens")
                .child(receptor);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Token token = dataSnapshot.getValue(Token.class);
                Data data = new Data();
                data.setEmisor(ControlSesiones.ObtenerUsuarioActivo(context));
                data.setIdVenta(idVenta);
                data.setReceptor(receptor);
                if (primero) {
                    data.setTipo(Data.TIPO_CONFIRMACION_REPARTIDOR_PRIMER_VUELTA);
                } else {
                    data.setTipo(Data.TIPO_CONFIRMACION_REPARTIDOR_SEGUNDA_VUELTA);
                }
                Sender sender  = new Sender(data,token.getToken());

                api.enviarNotificacion(sender)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if (response.code() == 200) {
                                    assert response.body() != null;
                                    if (response.body().success != 1) {
                                        Log.e("NOTIFICACION","Error con la notificacion");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private boolean validaCampo(TextInputLayout lyt, TextInputEditText txt, String error) {
        boolean isError;
        if (txt.getText().toString().isEmpty()) {
            isError = true;
            lyt.setError(error);
            txt.requestFocus();
        } else {
            isError = false;
            lyt.setError(null);
        }
        return !isError;
    }
}