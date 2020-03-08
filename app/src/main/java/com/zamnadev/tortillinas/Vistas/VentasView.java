package com.zamnadev.tortillinas.Vistas;

import com.zamnadev.tortillinas.Presentadores.BasePresenter;

public interface VentasView {
    interface View {
        void showVentas();
    }

    interface Presenter extends BasePresenter {
        void setView(VentasView.View view);
        void getVentas();
    }
}