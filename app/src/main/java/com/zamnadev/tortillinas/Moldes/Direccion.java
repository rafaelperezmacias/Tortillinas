package com.zamnadev.tortillinas.Moldes;

import java.io.Serializable;

public class Direccion implements Serializable {

    private String calle;
    private String numeroExterior;
    private String numeroInterior;
    private String cp;
    private String colonia;
    private String municipio;

    public Direccion() {

    }

    public Direccion(String calle, String numeroExterior) {
        this.calle = calle;
        this.numeroExterior = numeroExterior;
    }

    public Direccion(String calle, String numeroExterior, String numeroInterior) {
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.numeroInterior = numeroInterior;
    }

    public Direccion(String calle, String numeroExterior, String cp, String colonia, String municipio) {
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.cp = cp;
        this.colonia = colonia;
        this.municipio = municipio;
    }

    public Direccion(String calle, String numeroExterior, String numeroInterior, String cp, String colonia, String municipio) {
        this.calle = calle;
        this.numeroExterior = numeroExterior;
        this.numeroInterior = numeroInterior;
        this.cp = cp;
        this.colonia = colonia;
        this.municipio = municipio;
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

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String toStringRecyclerView() {
        String tmp  = "";

        tmp += calle + "#" + numeroExterior;

        if (numeroInterior != null) {
            tmp += ", int: " + numeroInterior;
        }

        tmp += ", C.P. " + cp + ", " + colonia + ", " + municipio + ".";
        return tmp;
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "calle='" + calle + '\'' +
                ", numeroExterior='" + numeroExterior + '\'' +
                ", numeroInterior='" + numeroInterior + '\'' +
                ", cp='" + cp + '\'' +
                ", colonia='" + colonia + '\'' +
                ", municipio='" + municipio + '\'' +
                '}';
    }
}
