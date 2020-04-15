package com.zamnadev.tortillinas.Moldes;

public class Vuelta {

    private boolean confirmado;
    private double masa;
    private double tortillas;
    private boolean registrada;

    public Vuelta() {
    }

    public Vuelta(boolean confirmado, double masa, double tortillas, boolean registrada) {
        this.confirmado = confirmado;
        this.masa = masa;
        this.tortillas = tortillas;
        this.registrada = registrada;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
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

    public boolean isRegistrada() {
        return registrada;
    }

    public void setRegistrada(boolean registrada) {
        this.registrada = registrada;
    }

    @Override
    public String toString() {
        return "Vuelta{" +
                "confirmado=" + confirmado +
                ", masa=" + masa +
                ", tortillas=" + tortillas +
                ", registrada=" + registrada +
                '}';
    }
}
