package com.zamnadev.tortillinas.Moldes;

public class Concepto {

    private String nombre;
    private double precio;
    private String id;

    public Concepto() {
    }

    public Concepto(String nombre, double precio, String id) {
        this.nombre = nombre;
        this.precio = precio;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Concepto{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", id='" + id + '\'' +
                '}';
    }
}
