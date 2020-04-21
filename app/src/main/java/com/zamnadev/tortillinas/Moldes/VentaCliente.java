package com.zamnadev.tortillinas.Moldes;

public class VentaCliente {

    private String idCliente;
    private VueltaRepartidor vuelta1;
    private VueltaRepartidor vuelta2;
    private VueltaRepartidor devolucion;
    private double pago;

    public VentaCliente() {
    }

    public VentaCliente(String idCliente, VueltaRepartidor vuelta1, VueltaRepartidor vuelta2, VueltaRepartidor devolucion, double pago) {
        this.idCliente = idCliente;
        this.vuelta1 = vuelta1;
        this.vuelta2 = vuelta2;
        this.devolucion = devolucion;
        this.pago = pago;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public VueltaRepartidor getVuelta1() {
        return vuelta1;
    }

    public void setVuelta1(VueltaRepartidor vuelta1) {
        this.vuelta1 = vuelta1;
    }

    public VueltaRepartidor getVuelta2() {
        return vuelta2;
    }

    public void setVuelta2(VueltaRepartidor vuelta2) {
        this.vuelta2 = vuelta2;
    }

    public VueltaRepartidor getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(VueltaRepartidor devolucion) {
        this.devolucion = devolucion;
    }

    public double getPago() {
        return pago;
    }

    public void setPago(double pago) {
        this.pago = pago;
    }

    @Override
    public String toString() {
        return "VentaCliente{" +
                "idCliente='" + idCliente + '\'' +
                ", vuelta1=" + vuelta1 +
                ", vuelta2=" + vuelta2 +
                ", devolucion=" + devolucion +
                ", pago=" + pago +
                '}';
    }
}
