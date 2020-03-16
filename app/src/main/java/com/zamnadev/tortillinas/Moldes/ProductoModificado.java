package com.zamnadev.tortillinas.Moldes;

public class ProductoModificado {

    private String idProducto;
    private double precio;

    public ProductoModificado()
    {

    }

    public ProductoModificado(String idProducto, double precio)
    {
        this.idProducto = idProducto;
        this.precio = precio;
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

    @Override
    public String toString() {
        return "ProductoModificado{" +
                "idProducto='" + idProducto + '\'' +
                ", precio=" + precio +
                '}';
    }
}
