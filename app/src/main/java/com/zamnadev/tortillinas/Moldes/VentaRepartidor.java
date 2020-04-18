package com.zamnadev.tortillinas.Moldes;

public class VentaRepartidor {

    private String idVenta;
    private long tiempo;
    private String fecha;
    private String idSucursal;
    private String idEmpleado;
    private VueltaRepartidor vuelta1;
    private VueltaRepartidor vuelta2;

    public VentaRepartidor() {
    }

    public VentaRepartidor(String idVenta, long tiempo, String fecha, String idSucursal, String idEmpleado, VueltaRepartidor vuelta1, VueltaRepartidor vuelta2) {
        this.idVenta = idVenta;
        this.tiempo = tiempo;
        this.fecha = fecha;
        this.idSucursal = idSucursal;
        this.idEmpleado = idEmpleado;
        this.vuelta1 = vuelta1;
        this.vuelta2 = vuelta2;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(String idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public VueltaRepartidor getVuelta1() {
        return vuelta1;
    }

    public void setVuelta1(VueltaRepartidor vuelta1) {
        this.vuelta1 = vuelta1;
    }

    public VueltaRepartidor getVuelta2() {
        return vuelta2;
    }

    public void setVuelta2(VueltaRepartidor vuelta2) {
        this.vuelta2 = vuelta2;
    }

    @Override
    public String toString() {
        return "VentaRepartidor{" +
                "idVenta='" + idVenta + '\'' +
                ", tiempo=" + tiempo +
                ", fecha='" + fecha + '\'' +
                ", idSucursal='" + idSucursal + '\'' +
                ", idEmpleado='" + idEmpleado + '\'' +
                ", vuelta1=" + vuelta1.toString() +
                ", vuelta2=" + vuelta2.toString() +
                '}';
    }
}
