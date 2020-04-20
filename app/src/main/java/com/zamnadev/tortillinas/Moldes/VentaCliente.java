package com.zamnadev.tortillinas.Moldes;

public class VentaCliente {

    private String idCliente;
    private Vuelta vuelta1;
    private Vuelta vuelta2;
    private Vuelta devolucion;

    public VentaCliente() {

    }

    public VentaCliente(String idCliente, Vuelta vuelta1, Vuelta vuelta2, Vuelta devolucion) {
        this.idCliente = idCliente;
        this.vuelta1 = vuelta1;
        this.vuelta2 = vuelta2;
        this.devolucion = devolucion;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public Vuelta getVuelta1() {
        return vuelta1;
    }

    public Vuelta getVuelta2() {
        return vuelta2;
    }

    public Vuelta getDevolucion() {
        return devolucion;
    }

    @Override
    public String toString() {
        return "VentaCliente{" +
                "idCliente='" + idCliente + '\'' +
                ", vuelta1=" + vuelta1 +
                ", vuelta2=" + vuelta2 +
                ", devolucion=" + devolucion +
                '}';
    }
}
