package com.zamnadev.tortillinas.Moldes;

public class VentaDelDia {

    private String idProducto;
    private int cantidad;

    public VentaDelDia() {

    }

    public VentaDelDia(String idProducto, int cantidad) {
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "VentaDelDia{" +
                "idProducto='" + idProducto + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }
}
