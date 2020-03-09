package com.zamnadev.tortillinas.Presentadores;

import android.app.Activity;

import com.zamnadev.tortillinas.Vistas.VentasView;

public class VentasPresenter implements VentasView.Presenter {

    private Activity activity;

    @Override
    public void setView(VentasView.View view) {

    }

    @Override
    public void getVentas() {
        // TODO: En esta clase se hace todo lo relacionado con la base de datos para después
        //  mostrarlo en la activity.
    }

    @Override
    public void init(Activity activity) {
        this.activity = activity;
    }
}