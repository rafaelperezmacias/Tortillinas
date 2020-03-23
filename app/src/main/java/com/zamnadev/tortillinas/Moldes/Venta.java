package com.zamnadev.tortillinas.Moldes;

public class  Venta {

    private String idVenta;
    private long tiempo;
    private String fecha;
    private String idSucursal;
    private String idEmpleado;

    public Venta() {
    }

    public Venta(String idVenta, long tiempo, String fecha, String idSucursal, String idEmpleado) {
        this.idVenta = idVenta;
        this.tiempo = tiempo;
        this.fecha = fecha;
        this.idSucursal = idSucursal;
        this.idEmpleado = idEmpleado;
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

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta='" + idVenta + '\'' +
                ", tiempo=" + tiempo +
                ", fecha='" + fecha + '\'' +
                ", idSucursal='" + idSucursal + '\'' +
                ", idEmpleado='" + idEmpleado + '\'' +
                '}';
    }
}
