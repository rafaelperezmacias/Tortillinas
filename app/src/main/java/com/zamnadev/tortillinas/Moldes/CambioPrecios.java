package com.zamnadev.tortillinas.Moldes;

public class CambioPrecios {

    public static final int PRODUCTO_MODIFICACION = 2;

    private String idProducto;
    private double precio;
    private int tipo;
    private Long fecha;

    public CambioPrecios() {
    }

    public CambioPrecios(String idProducto, double precio, int tipo, Long fecha) {
        this.idProducto = idProducto;
        this.precio = precio;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
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

    public Long getFecha() {
        return fecha;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "CambioPrecios{" +
                "idProducto='" + idProducto + '\'' +
                ", precio=" + precio +
                ", tipo=" + tipo +
                ", fecha=" + fecha +
                '}';
    }
}
