package com.zamnadev.tortillinas.Moldes;

public class Sucursal {

    private String idSucursal;
    private String nombre;
    private Direccion direccion;
    private int botes;
    private boolean eliminado;

    public Sucursal() {
    }

    public Sucursal(String idSucursal, String nombre, Direccion direccion, int botes, boolean eliminado) {
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.direccion = direccion;
        this.botes = botes;
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

    public int getBotes() {
        return botes;
    }

    public void setBotes(int botes) {
        this.botes = botes;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "Sucursal{" +
                "idSucursal='" + idSucursal + '\'' +
                ", nombre='" + nombre + '\'' +
                ", direccion=" + direccion.toString() +
                ", botes=" + botes +
                ", eliminado=" + eliminado +
                '}';
    }
}
