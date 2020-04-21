package com.zamnadev.tortillinas.Moldes;

public class VueltaRepartidor {

    private double masa;
    private double tortillas;
    private double totopos;
    private double tortillaVenta;
    private double masaVenta;
    private double totoposVenta;

    public VueltaRepartidor() {
    }

    public VueltaRepartidor(double masa, double tortillas, double totopos, double tortillaVenta, double masaVenta, double totoposVenta) {
        this.masa = masa;
        this.tortillas = tortillas;
        this.totopos = totopos;
        this.tortillaVenta = tortillaVenta;
        this.masaVenta = masaVenta;
        this.totoposVenta = totoposVenta;
    }

    public double getMasa() {
        return masa;
    }

    public void setMasa(double masa) {
        this.masa = masa;
    }

    public double getTortillas() {
        return tortillas;
    }

    public void setTortillas(double tortillas) {
        this.tortillas = tortillas;
    }

    public double getTotopos() {
        return totopos;
    }

    public void setTotopos(double totopos) {
        this.totopos = totopos;
    }

    public double getTortillaVenta() {
        return tortillaVenta;
    }

    public void setTortillaVenta(double tortillaVenta) {
        this.tortillaVenta = tortillaVenta;
    }

    public double getMasaVenta() {
        return masaVenta;
    }

    public void setMasaVenta(double masaVenta) {
        this.masaVenta = masaVenta;
    }

    public double getTotoposVenta() {
        return totoposVenta;
    }

    public void setTotoposVenta(double totoposVenta) {
        this.totoposVenta = totoposVenta;
    }
}
