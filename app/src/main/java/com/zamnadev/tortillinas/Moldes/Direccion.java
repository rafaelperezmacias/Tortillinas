package com.zamnadev.tortillinas.Moldes;

import java.io.Serializable;

public class Direccion implements Serializable {

    private String calle;
    private String numeroExterior;
    private String numeroInterior;
    private String zona;

    public Direccion() {

    }

    public Direccion(String calle, String numeroExterior, String zona) {
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.zona = zona;
    }

    public Direccion(String calle, String numeroExterior, String numeroInterior, String zona) {
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.numeroInterior = numeroInterior;
        this.zona = zona;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroExterior() {
        return numeroExterior;
    }

    public void setNumeroExterior(String numeroExterior) {
        this.numeroExterior = numeroExterior;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String toRecyclerView() {
        String msg = calle + " #" + numeroExterior;
        if (numeroInterior != null) {
            msg += ", Int. #" + numeroInterior;
        }
        return msg;
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "calle='" + calle + '\'' +
                ", numeroExterior='" + numeroExterior + '\'' +
                ", numeroInterior='" + numeroInterior + '\'' +
                ", zona='" + zona + '\'' +
                '}';
    }
}
