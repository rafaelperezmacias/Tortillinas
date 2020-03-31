package com.zamnadev.tortillinas.Notificaciones;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zamnadev.tortillinas.MainActivity;
import com.zamnadev.tortillinas.R;
import com.zamnadev.tortillinas.Sesiones.ControlSesiones;

public class FCMService extends FirebaseMessagingService {

    //En caso de que el token de acceso cambie
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        if (ControlSesiones.ValidaUsuarioActivo(getApplicationContext()))
        {
            String token = s;

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens")
                    .child(ControlSesiones.ObtenerUsuarioActivo(getApplicationContext()))
                    .child("token");

            reference.setValue(token);
        }

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (ControlSesiones.ValidaUsuarioActivo(getApplicationContext()))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                enviarOreoNotificacion(remoteMessage);
            } else {
                enviarNotificacion(remoteMessage);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void enviarOreoNotificacion(RemoteMessage remoteMessage) {
        //Por mientras solo validamos que los mensajes lleguen con exito
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
                .setVibrate(new long[] {100,250,100,2})
                .setShowWhen(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        int randomNo = 0;
        if (j > 0){
            randomNo = j;
        }

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
                .setVibrate(new long[] {100,250,100,2})
                .setShowWhen(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        int randomNo = 0;
        if (j > 0){
            randomNo = j;
        }

        notificationManager.notify(randomNo,builder.build());
    }
}