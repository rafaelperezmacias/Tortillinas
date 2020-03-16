package com.zamnadev.tortillinas.Moldes;

import android.util.Log;

import java.util.StringTokenizer;

public class ProductoModificado {

    private String idProducto;
    private double precio;

    public ProductoModificado(String cadena)
    {
        char tmp[] = cadena.toCharArray();
        int x;
        for (x = 0; x < cadena.length(); x++)
        {
            if (tmp[x] == '?') {
                break;
            }
        }
        precio = Double.parseDouble(cadena.substring(0,x));
        idProducto = cadena.substring(++x);
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
