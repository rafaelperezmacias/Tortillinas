package com.zamnadev.tortillinas.Moldes;

import java.util.HashMap;

public class Cliente {

    private String idCliente;
    private Nombre nombre;
    private Direccion direccion;
    private String telefono;
    private HashMap<String, String> productos;
    private boolean eliminado;
    private boolean preferencial;

    public Cliente() {

    }

    public Cliente(String idCliente, Nombre nombre, Direccion direccion, String telefono, HashMap<String, String> productos, boolean eliminado, boolean preferencial)
    {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.productos = productos;
        this.eliminado = eliminado;
        this.preferencial = preferencial;
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

    public HashMap<String, String> getProductos() {
        return productos;
    }

    public void setProductos(HashMap<String, String> productos) {
        this.productos = productos;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public boolean isPreferencial() {
        return preferencial;
    }

    public void setPreferencial(boolean preferencial) {
        this.preferencial = preferencial;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente='" + idCliente + '\'' +
                ", nombre=" + nombre +
                ", direccion=" + direccion.toString() +
                ", telefono='" + telefono + '\'' +
                ", productos=" +
                ", eliminado=" + eliminado +
                ", preferencial=" + preferencial +
                '}';
    }
}
