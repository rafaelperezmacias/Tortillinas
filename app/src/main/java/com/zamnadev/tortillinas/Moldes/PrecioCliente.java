package com.zamnadev.tortillinas.Moldes;

import java.util.HashMap;

public class PrecioCliente {

    private long fecha;
    private HashMap<String, String> precios;

    public PrecioCliente() {
    }

    public PrecioCliente(long fecha, HashMap<String, String> precios) {
        this.fecha = fecha;
        this.precios = precios;
    }

    public PrecioCliente(PrecioCliente pc) {
        fecha = pc.fecha;
        precios = pc.precios;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public HashMap<String, String> getPrecios() {
        return precios;
    }

    public void setPrecios(HashMap<String, String> precios) {
        this.precios = precios;
    }

    @Override
    public String toString() {
        return "PrecioCliente{" +
                "fecha=" + fecha +
                ", precios=" + precios +
                '}';
    }
}
