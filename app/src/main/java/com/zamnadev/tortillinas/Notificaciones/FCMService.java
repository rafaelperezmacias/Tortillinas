package com.zamnadev.tortillinas.Notificaciones;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zamnadev.tortillinas.Dialogos.DialogoAddCampoVentas;
import com.zamnadev.tortillinas.MainActivity;
import com.zamnadev.tortillinas.Moldes.Empleado;
import com.zamnadev.tortillinas.Moldes.VentaMostrador;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

public class FCMService extends FirebaseMessagingService {

    private static onConfirmacion LISTENER;

    //En caso de que el token de acceso cambie
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        if (ControlSesiones.ValidaUsuarioActivo(getApplicationContext()))
        {
            FirebaseDatabase.getInstance().getReference("Tokens")
                    .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()))
                    .child("token")
                    .setValue(s);
        }

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (ControlSesiones.ValidaUsuarioActivo(getApplicationContext()))
        {
            String receptor = remoteMessage.getData().get("receptor");
            if (receptor.equals(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()))) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Empleados")
                        .child(receptor);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Empleado empleado = dataSnapshot.getValue(Empleado.class);
                        if (!empleado.isConexion()) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                enviarOreoNotificacion(remoteMessage);
                            } else {
                                enviarNotificacion(remoteMessage);
                            }
                        } else {
                            if (LISTENER != null) {
                                String emisor = remoteMessage.getData().get("emisor");
                                String idVenta = remoteMessage.getData().get("idVenta");
                                int tipo = Integer.parseInt(remoteMessage.getData().getOrDefault("tipo", "-1"));
                                LISTENER.enviarMensaje(idVenta,tipo,emisor);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    public interface onConfirmacion {
        void enviarMensaje(String idVenta, int tipo, String emisor);
    }

    public static void setLISTENER(onConfirmacion listener) {
        LISTENER = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void enviarOreoNotificacion(RemoteMessage remoteMessage) {
        //Por mientras solo validamos que los mensajes lleguen con exito
        String receptor = remoteMessage.getData().get("receptor");
        String emisor = remoteMessage.getData().get("emisor");
        String idVenta = remoteMessage.getData().get("idVenta");
        String texto = remoteMessage.getData().get("texto");
        int tipo = Integer.parseInt(remoteMessage.getData().getOrDefault("tipo", "-1"));
        //Expresion regular que elimina todas las letras del campo que le asignamos
        int j = Integer.parseInt(emisor.replaceAll("[\\D]",""));
        Bundle bundle = new Bundle();
        bundle.putBoolean("notificacion",true);
        bundle.putString("idVenta",idVenta);
        bundle.putString("emisor",emisor);
        bundle.putInt("tipo",tipo);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"com.zamnadev.tortillinas")
                .setContentTitle("Nueva entrega disponible")
                .setContentText(texto.substring(0,texto.indexOf("|")))
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(texto.replace("|","")))
                .setAutoCancel(true)
                .setSound(sonido)
                .setVibrate(new long[] {100,250,100,200})
                .setShowWhen(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        int randomNo = (int) ((Math.random() * 10000) + 1);

        NotificationChannel channel = new NotificationChannel("com.zamnadev.tortillinas","Tortillinas",NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        channel.enableLights(false);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(randomNo,builder.build());
    }

    private void enviarNotificacion(RemoteMessage remoteMessage) {

        String receptor = remoteMessage.getData().get("receptor");
        String emisor = remoteMessage.getData().get("emisor");

        Log.e("receptor",receptor);
        Log.e("emisor",emisor);

        //Expresion regular que elimina todas las letras del campo que le asignamos
        int j = Integer.parseInt(emisor.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"com.zamnadev.tortillinas")
                .setContentTitle("Prueba de notificaciones")
                .setContentText("cuerpo")
                .setAutoCancel(true)
                .setSound(sonido)
                .setVibrate(new long[] {100,250,100,200})
                .setShowWhen(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        int randomNo = (int) ((Math.random() * 10000) + 1);

        notificationManager.notify(randomNo,builder.build());
    }
}