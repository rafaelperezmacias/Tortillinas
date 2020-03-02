package com.zamnadev.tortillinas.Moldes;

public class Sucursal {

    private String idSucursal;
    private String nombre;
    private Direccion direccion;
    private boolean eliminado;

    public Sucursal() {
    }

    public Sucursal(String idSucursal, String nombre, Direccion direccion, boolean eliminado) {
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.direccion = direccion;
        this.eliminado = eliminado;
    }

    public String getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(String idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}
