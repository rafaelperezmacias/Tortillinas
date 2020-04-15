package com.zamnadev.tortillinas.Moldes;

public class AuxVenta {

    private Vuelta vuelta1;
    private Vuelta vuelta2;

    public AuxVenta() {
    }

    public AuxVenta(Vuelta vuelta1, Vuelta vuelta2) {
        this.vuelta1 = vuelta1;
        this.vuelta2 = vuelta2;
    }

    public Vuelta getVuelta1() {
        return vuelta1;
    }

    public void setVuelta1(Vuelta vuelta1) {
        this.vuelta1 = vuelta1;
    }

    public Vuelta getVuelta2() {
        return vuelta2;
    }

    public void setVuelta2(Vuelta vuelta2) {
        this.vuelta2 = vuelta2;
    }

    @Override
    public String toString() {
        return "AuxVenta{" +
                ", vuelta1=" + vuelta1.toString() +
                ", vuelta2=" + vuelta2.toString() +
                '}';
    }
}
