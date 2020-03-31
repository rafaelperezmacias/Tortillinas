package com.zamnadev.tortillinas.Notificaciones;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

//Peticion web para la conexion con el servidor, este envia el packete con la informacion
public interface FCMServiceAPI {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAj2raFAg:APA91bGK7wPFln6jAM1PCWPXZNLTp1U5rEVmREroPKAh6hCyidsA0FEwSaObdA-cvpRDj-T9H4k5iDGA9iuNWuzEkuedOfvKRTPiBrvyJnJuSSpKSH05cg5WcGT79ZZnN34KEay8J7Fx"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> enviarNotificacion (@Body Sender body);

}
