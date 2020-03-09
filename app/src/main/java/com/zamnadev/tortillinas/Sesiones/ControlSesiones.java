package com.zamnadev.tortillinas.Sesiones;

import android.content.Context;
import android.content.SharedPreferences;

public class ControlSesiones {

    public static void IngreasarUsuario(Context context, String cuenta) {
        context.getSharedPreferences("cuentas", Context.MODE_PRIVATE).edit().putString("idCuenta",cuenta).apply();
    }

    public static boolean ValidaUsuarioActivo(Context context) {
        return context.getSharedPreferences("cuentas", Context.MODE_PRIVATE).getString("idCuenta",null) != null;
    }

    public static void EliminaUsuario(Context context) {
        context.getSharedPreferences("cuentas",Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static String ObtenerUsuarioActivo(Context context) {
        return context.getSharedPreferences("cuentas",Context.MODE_PRIVATE).getString("idCuenta",null);
    }
}
