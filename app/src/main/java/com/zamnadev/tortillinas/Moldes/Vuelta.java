package com.zamnadev.tortillinas.Moldes;

public class Vuelta {

    private boolean confirmado;
    private double masa;
    private double tortillas;
    private double totopos;
    private boolean registrada;
    private long hora;

    public Vuelta() {
    }

    public Vuelta(boolean confirmado, double masa, double tortillas, double totopos, boolean registrada, long hora) {
        this.confirmado = confirmado;
        this.masa = masa;
        this.tortillas = tortillas;
        this.totopos = totopos;
        this.registrada = registrada;
        this.hora = hora;
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

    public double getTotopos() {
        return totopos;
    }

    public void setTotopos(double totopos) {
        this.totopos = totopos;
    }

    public boolean isRegistrada() {
        return registrada;
    }

    public void setRegistrada(boolean registrada) {
        this.registrada = registrada;
    }

    public long getHora() {
        return hora;
    }

    public void setHora(long hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "Vuelta{" +
                "confirmado=" + confirmado +
                ", masa=" + masa +
                ", tortillas=" + tortillas +
                ", totopos=" + totopos +
                ", registrada=" + registrada +
                ", hora=" + hora +
                '}';
    }
}
