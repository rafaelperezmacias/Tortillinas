package com.zamnadev.tortillinas.BottomSheets;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import com.zamnadev.tortillinas.MainActivity;
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

public class VueltaBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private boolean primero;
    private String idVenta;
    private Empleado empleado;
    private FCMServiceAPI api;
    private Context context;
    private String nombreSucursal;
    private boolean isEdit;
    private Vuelta vuelta;
    private String idEmpleado;

    public VueltaBottomSheet(boolean primero, Empleado empleado, String idVenta, Context context, String nombreSucursal, String idEmpleado)
    {
        this.primero = primero;
        this.empleado = empleado;
        this.idVenta = idVenta;
        api = Client.getClient("https://fcm.googleapis.com/").create(FCMServiceAPI.class);
        this.context = context;
        this.nombreSucursal = nombreSucursal;
        isEdit = false;
        this.idEmpleado = idEmpleado;
    }

    public VueltaBottomSheet(boolean primero, Empleado empleado, String idVenta, Context context, String nombreSucursal, boolean isEdit, Vuelta vuelta, String idEmpleado)
    {
        this.primero = primero;
        this.empleado = empleado;
        this.idVenta = idVenta;
        api = Client.getClient("https://fcm.googleapis.com/").create(FCMServiceAPI.class);
        this.context = context;
        this.nombreSucursal = nombreSucursal;
        this.isEdit = isEdit;
        this.vuelta = vuelta;
        this.idEmpleado = idEmpleado;
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
        TextInputEditText txtMasa = view.findViewById(R.id.txtMasa);
        TextInputEditText txtTotopos = view.findViewById(R.id.txtTotopos);

        CardView cardError = view.findViewById(R.id.cardError);

        String titulo = "";
        if (primero) {
            titulo += "Primer ";
        } else {
            titulo += "Segunda ";
        }

        titulo += "vuelta";

        if (isEdit) {
            if (vuelta != null) {
                if (vuelta.getMasa() > 0.0) {
                    txtMasa.setText("" + vuelta.getMasa());
                }
                if (vuelta.getTotopos() > 0.0) {
                    txtTotopos.setText("" + vuelta.getTotopos());
                }
                if (vuelta.getTortillas() > 0.0) {
                    txtTortilla.setText("" + vuelta.getTortillas());
                }
            }
        }

        ((TextView) view.findViewById(R.id.txtTitulo))
                .setText(titulo);

        ((TextView) view.findViewById(R.id.txtSubTitulo))
                .setText(empleado.getNombre().getNombres() + " " + empleado.getNombre().getApellidos());

        ((MaterialButton) view.findViewById(R.id.btnGuardar))
                .setOnClickListener(view1 -> {
                    if (txtMasa.getText().toString().isEmpty() && txtTortilla.getText().toString().isEmpty()
                        && txtTotopos.getText().toString().isEmpty()) {
                        cardError.setVisibility(View.VISIBLE);
                        return;
                    }

                    cardError.setVisibility(View.GONE);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    String text = "El repartidor se llevó:";
                    if (!txtTortilla.getText().toString().isEmpty()) {
                        hashMap.put("tortillas",Double.parseDouble(txtTortilla.getText().toString()));
                        text += "\n\t" + txtTortilla.getText().toString() + " kgs de tortilla";
                    }
                    if (!txtMasa.getText().toString().isEmpty()) {
                        hashMap.put("masa",Double.parseDouble(txtMasa.getText().toString()));
                        text += "\n\t" + txtMasa.getText().toString() + " kgs de masa";
                    }
                    if (!txtTotopos.getText().toString().isEmpty()) {
                        hashMap.put("totopos",Double.parseDouble(txtTotopos.getText().toString()));
                        text += "\n\t" + txtTotopos.getText().toString() + " kgs de totopos";
                    }
                    hashMap.put("hora", ServerValue.TIMESTAMP);
                    hashMap.put("confirmado",false);
                    hashMap.put("registrada",true);

                    MessageDialog dialog = new MessageDialog(context, new MessageDialogBuilder()
                            .setTitle("Alerta")
                            .setMessage("Confirme para continuar\n" + text)
                            .setPositiveButtonText("Confirmar")
                            .setNegativeButtonText("Cancelar")
                    );
                    dialog.show();
                    dialog.setPositiveButtonListener(v -> {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                                .child(idVenta)
                                .child(empleado.getIdEmpleado());
                        if (primero) {
                            reference.child("vuelta1").updateChildren(hashMap)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            enviarNotificacion(empleado.getIdEmpleado(), txtMasa.getText().toString() , txtTortilla.getText().toString(), txtTotopos.getText().toString(), nombreSucursal);
                                            dialog.dismiss();
                                            dismiss();
                                        }
                                    });
                        } else {
                            reference.child("vuelta2").updateChildren(hashMap)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            enviarNotificacion(empleado.getIdEmpleado(), txtMasa.getText().toString() , txtTortilla.getText().toString(), txtTotopos.getText().toString(), nombreSucursal);
                                            dialog.dismiss();
                                            dismiss();
                                        }
                                    });
                        }
                    });
                    dialog.setNegativeButtonListener(v -> dialog.dismiss());
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
    private void enviarNotificacion(String idRepartidor, String masa, String tortilla, String totopos, String sucursal) {
        String fecha = MainActivity.getFecha();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fecha",fecha);
        hashMap.put("idVenta", idVenta);
        hashMap.put("idEmpleado",idEmpleado);

        HashMap<String, Object> vueltaMap = new HashMap<>();
        vueltaMap.put("confirmado",false);
        if (!tortilla.isEmpty()) {
            vueltaMap.put("tortillas",Double.parseDouble(tortilla));
        } else {
            vueltaMap.put("tortillas",-1);
        }
        if (!masa.isEmpty()) {
            vueltaMap.put("masa",Double.parseDouble(masa));
        } else {
            vueltaMap.put("masa",-1);
        }
        if (!totopos.isEmpty()) {
            vueltaMap.put("totopos",Double.parseDouble(totopos));
        } else {
            vueltaMap.put("totopos",-1);
        }
        vueltaMap.put("registrada",true);
        vueltaMap.put("hora", ServerValue.TIMESTAMP);

        if (primero) {
            hashMap.put("vuelta1",vueltaMap);
        } else {
            hashMap.put("vuelta2",vueltaMap);
        }

        if (isEdit) {
            if (primero) {
                FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                        .child(idVenta)
                        .child(idRepartidor)
                        .child("vuelta1")
                        .updateChildren(vueltaMap);
            } else {
                FirebaseDatabase.getInstance().getReference("AuxVentaMostrador")
                        .child(idVenta)
                        .child(idRepartidor)
                        .child("vuelta2")
                        .updateChildren(vueltaMap);
            }
        }

        FirebaseDatabase.getInstance().getReference("Confirmaciones")
                .child(idRepartidor)
                .child(idVenta)
                .updateChildren(hashMap)
                .addOnCompleteListener(task -> enviarMensaje(idRepartidor, masa, tortilla, totopos, sucursal));
    }

    //Para enviar un mensaje ocupasmos el id del receptor
    private void enviarMensaje(final String receptor, String masa, String tortilla, String totopos, String sucursal) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens")
                .child(receptor);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Token token = dataSnapshot.getValue(Token.class);
                Data data = new Data();
                String text = sucursal + "|\n";
                if (primero) {
                    text += "Primer vuelta\n";
                } else {
                    text += "Segunda vuelta\n";
                }
                text += "Se ha agendando una nueva entrega de: \n";
                if (!tortilla.isEmpty()) {
                    text += "\t\t\tTortillas: " + tortilla + " kgs.\n";
                }
                if (!masa.isEmpty()) {
                    text += "\t\t\tMasa: " + masa + " kgs.\n";
                }
                if (!totopos.isEmpty()) {
                    text += "\t\t\tTotopos: " + totopos + " kgs.\n";
                }
                text += "Confirme para poder continuar";
                data.setTexto(text);
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
}