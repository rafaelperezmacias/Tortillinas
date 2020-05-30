package com.zamnadev.tortillinas.Moldes;

import java.util.HashMap;

public class Cliente {

    private String idCliente;
    private Nombre nombre;
    private Direccion direccion;
    private String telefono;
    private String pseudonimo;
    private HashMap<String, String> precios;
    private boolean eliminado;
    private boolean preferencial;
    private long timeDelete;
    private long altaPreferencial;
    private boolean altaPref;

    public Cliente() {

    }

    public Cliente(String idCliente, Nombre nombre, Direccion direccion, String telefono, String pseudonimo, HashMap<String, String> precios, boolean eliminado, boolean preferencial, long timeDelete, long altaPreferencial, boolean altaPref) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.pseudonimo = pseudonimo;
        this.precios = precios;
        this.eliminado = eliminado;
        this.preferencial = preferencial;
        this.timeDelete = timeDelete;
        this.altaPreferencial = altaPreferencial;
        this.altaPref = altaPref;
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

    public String getPseudonimo() {
        return pseudonimo;
    }

    public void setPseudonimo(String pseudonimo) {
        this.pseudonimo = pseudonimo;
    }

    public HashMap<String, String> getPrecios() {
        return precios;
    }

    public void setPrecios(HashMap<String, String> precios) {
        this.precios = precios;
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

    public long getTimeDelete() {
        return timeDelete;
    }

    public void setTimeDelete(long timeDelete) {
        this.timeDelete = timeDelete;
    }

    public long getAltaPreferencial() {
        return altaPreferencial;
    }

    public void setAltaPreferencial(long altaPreferencial) {
        this.altaPreferencial = altaPreferencial;
    }

    public boolean isAltaPref() {
        return altaPref;
    }

    public void setAltaPref(boolean altaPref) {
        this.altaPref = altaPref;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente='" + idCliente + '\'' +
                ", nombre=" + nombre +
                ", direccion=" + direccion +
                ", telefono='" + telefono + '\'' +
                ", pseudonimo='" + pseudonimo + '\'' +
                ", precios=" + precios +
                ", eliminado=" + eliminado +
                ", preferencial=" + preferencial +
                ", timeDelete=" + timeDelete +
                ", altaPreferencial=" + altaPreferencial +
                ", altaPref=" + altaPref +
                '}';
    }
}
