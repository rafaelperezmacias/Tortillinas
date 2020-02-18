package com.zamnadev.tortillinas.Moldes;

public class Cliente {

    private String idCliente;
    private Nombre nombre;
    private Direccion direccion;
    private String telefono;
    private boolean eliminado;

    public Cliente() {

    }

    public Cliente(String idCliente, Nombre nombre, Direccion direccion, String telefono, boolean eliminado) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.eliminado = eliminado;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Nombre getNombre() {
        return nombre;
    }

    public void setNombre(Nombre nombre) {
        this.nombre = nombre;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente='" + idCliente + '\'' +
                ", nombre=" + nombre +
                ", direccion=" + direccion +
                ", telefono='" + telefono + '\'' +
                ", eliminado=" + eliminado +
                '}';
    }
}
