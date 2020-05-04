package com.zamnadev.tortillinas.Moldes;

public class Venta {

    private String idVenta;
    private long tiempo;
    private String fecha;
    private String idEmpleado;
    private String idSucursal;
    private boolean isMostrador;

    public Venta() {
    }

    public Venta(String idVenta, long tiempo, String fecha, String idEmpleado, String idSucursal, boolean isMostrador) {
        this.idVenta = idVenta;
        this.tiempo = tiempo;
        this.fecha = fecha;
        this.idEmpleado = idEmpleado;
        this.idSucursal = idSucursal;
        this.isMostrador = isMostrador;
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

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(String idSucursal) {
        this.idSucursal = idSucursal;
    }

    public boolean isMostrador() {
        return isMostrador;
    }

    public void setMostrador(boolean mostrador) {
        isMostrador = mostrador;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta='" + idVenta + '\'' +
                ", tiempo=" + tiempo +
                ", fecha='" + fecha + '\'' +
                ", idEmpleado='" + idEmpleado + '\'' +
                ", idSucursal='" + idSucursal + '\'' +
                ", isMostrador=" + isMostrador +
                '}';
    }
}
