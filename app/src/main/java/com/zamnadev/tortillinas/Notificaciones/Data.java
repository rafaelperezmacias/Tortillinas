package com.zamnadev.tortillinas.Notificaciones;

public class Data {

    public static final int TIPO_CONFIRMACION_REPARTIDOR_PRIMER_VUELTA = 10;
    public static final int TIPO_CONFIRMACION_REPARTIDOR_SEGUNDA_VUELTA = 11;

    //Caracteristicas que deseamos pasar entre mensajes, por mientras esto
    private int tipo;
    private String receptor;
    private String emisor;
    private String idVenta;
    private String texto;

    public Data() {
    }

    public Data(int tipo, String receptor, String emisor, String idVenta, String texto) {
        this.tipo = tipo;
        this.receptor = receptor;
        this.emisor = emisor;
        this.idVenta = idVenta;
        this.texto = texto;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
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

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return "Data{" +
                "tipo=" + tipo +
                ", receptor='" + receptor + '\'' +
                ", emisor='" + emisor + '\'' +
                ", idVenta='" + idVenta + '\'' +
                ", texto='" + texto + '\'' +
                '}';
    }
}
