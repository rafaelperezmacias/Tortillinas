package com.zamnadev.tortillinas.Moldes;

public class VueltaRepartidor {

    private double masa;
    private double tortillas;
    private double totopos;
    private Long time;

    public VueltaRepartidor() {
    }

    public VueltaRepartidor(double masa, double tortillas, double totopos, Long time) {
        this.masa = masa;
        this.tortillas = tortillas;
        this.totopos = totopos;
        this.time = time;
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "VueltaRepartidor{" +
                "masa=" + masa +
                ", tortillas=" + tortillas +
                ", totopos=" + totopos +
                ", time=" + time +
                '}';
    }
}
