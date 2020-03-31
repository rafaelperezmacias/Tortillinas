package com.zamnadev.tortillinas.Notificaciones;

public class Data {

    //Caracteristicas que deseamos pasar entre mensajes, por mientras esto
    private String receptor;
    private String emisor;

    public Data() {
    }

    public Data(String receptor, String emisor) {
        this.receptor = receptor;
        this.emisor = emisor;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    @Override
    public String toString() {
        return "Data{" +
                "receptor='" + receptor + '\'' +
                ", emisor='" + emisor + '\'' +
                '}';
    }
}
