package com.zamnadev.tortillinas.Moldes;

public class Confirmacion {

    private String fecha;
    private String idVenta;
    private String idEmpleado;
    private Vuelta vuelta1;
    private Vuelta vuelta2;

    public Confirmacion() {
    }

    public Confirmacion(String fecha, String idVenta, String idEmpleado, Vuelta vuelta1, Vuelta vuelta2) {
        this.fecha = fecha;
        this.idVenta = idVenta;
        this.idEmpleado = idEmpleado;
        this.vuelta1 = vuelta1;
        this.vuelta2 = vuelta2;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Vuelta getVuelta1() {
        return vuelta1;
    }

    public void setVuelta1(Vuelta vuelta1) {
        this.vuelta1 = vuelta1;
    }

    public Vuelta getVuelta2() {
        return vuelta2;
    }

    public void setVuelta2(Vuelta vuelta2) {
        this.vuelta2 = vuelta2;
    }

    @Override
    public String toString() {
        return "Confirmacion{" +
                "fecha='" + fecha + '\'' +
                ", idVenta='" + idVenta + '\'' +
                ", idEmpleado='" + idEmpleado + '\'' +
                ", vuelta1=" + vuelta1.toString() +
                ", vuelta2=" + vuelta2.toString() +
                '}';
    }
}
