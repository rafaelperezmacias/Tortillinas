package com.zamnadev.tortillinas.Moldes;

public class Modificado {

    private String idProducto;
    private long fecha;
    private double precio;
    private int tipo;

    public Modificado() {
    }

    public Modificado(String idProducto, long fecha, double precio, int tipo) {
        this.idProducto = idProducto;
        this.fecha = fecha;
        this.precio = precio;
        this.tipo = tipo;
    }

    public Modificado(Modificado m) {
        idProducto = m.idProducto;
        fecha = m.fecha;
        precio = m.precio;
        tipo = m.tipo;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Modificado{" +
                "idProducto='" + idProducto + '\'' +
                ", fecha=" + fecha +
                ", precio=" + precio +
                ", tipo=" + tipo +
                '}';
    }
}
