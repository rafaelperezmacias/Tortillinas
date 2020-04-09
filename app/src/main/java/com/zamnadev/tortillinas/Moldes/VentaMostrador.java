package com.zamnadev.tortillinas.Moldes;

import java.util.HashMap;

public class VentaMostrador {

    private String idVenta;
    private long tiempo;
    private String fecha;
    private String idSucursal;
    private String idEmpleado;
    private HashMap<String, String> repartidores;

    public VentaMostrador() {
    }

    public VentaMostrador(String idVenta, long tiempo, String fecha, String idSucursal, String idEmpleado, HashMap<String, String> repartidores) {
        this.idVenta = idVenta;
        this.tiempo = tiempo;
        this.fecha = fecha;
        this.idSucursal = idSucursal;
        this.idEmpleado = idEmpleado;
        this.repartidores = repartidores;
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

    public HashMap<String, String> getRepartidores() {
        return repartidores;
    }

    public void setRepartidores(HashMap<String, String> repartidores) {
        this.repartidores = repartidores;
    }

    @Override
    public String toString() {
        return "VentaMostrador{" +
                "idVenta='" + idVenta + '\'' +
                ", tiempo=" + tiempo +
                ", fecha='" + fecha + '\'' +
                ", idSucursal='" + idSucursal + '\'' +
                ", idEmpleado='" + idEmpleado + '\'' +
                ", repartidores=" + repartidores +
                '}';
    }
}
