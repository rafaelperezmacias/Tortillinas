package com.zamnadev.tortillinas.Moldes;

import java.util.HashMap;

public class VentaMostrador {

    private String idVenta;
    private long tiempo;
    private String fecha;
    private String idSucursal;
    private String idEmpleado;
    private HashMap<String, String> repartidores;
    private int costales;
    private int botes;
    private int maizNixtamalizado;
    private double masaVendida;
    private double tortillaSobra;
    private double maquinaMasa;
    private double molino;
    private double mermaTortilla;

    public VentaMostrador() {
    }

    public VentaMostrador(String idVenta, long tiempo, String fecha, String idSucursal, String idEmpleado, HashMap<String, String> repartidores, int costales, int botes, int maizNixtamalizado, double masaVendida, double tortillaSobra, double maquinaMasa, double molino, double mermaTortilla) {
        this.idVenta = idVenta;
        this.tiempo = tiempo;
        this.fecha = fecha;
        this.idSucursal = idSucursal;
        this.idEmpleado = idEmpleado;
        this.repartidores = repartidores;
        this.costales = costales;
        this.botes = botes;
        this.maizNixtamalizado = maizNixtamalizado;
        this.masaVendida = masaVendida;
        this.tortillaSobra = tortillaSobra;
        this.maquinaMasa = maquinaMasa;
        this.molino = molino;
        this.mermaTortilla = mermaTortilla;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(String idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public HashMap<String, String> getRepartidores() {
        return repartidores;
    }

    public void setRepartidores(HashMap<String, String> repartidores) {
        this.repartidores = repartidores;
    }

    public int getCostales() {
        return costales;
    }

    public void setCostales(int costales) {
        this.costales = costales;
    }

    public int getBotes() {
        return botes;
    }

    public void setBotes(int botes) {
        this.botes = botes;
    }

    public int getMaizNixtamalizado() {
        return maizNixtamalizado;
    }

    public void setMaizNixtamalizado(int maizNixtamalizado) {
        this.maizNixtamalizado = maizNixtamalizado;
    }

    public double getMasaVendida() {
        return masaVendida;
    }

    public void setMasaVendida(double masaVendida) {
        this.masaVendida = masaVendida;
    }

    public double getTortillaSobra() {
        return tortillaSobra;
    }

    public void setTortillaSobra(double tortillaSobra) {
        this.tortillaSobra = tortillaSobra;
    }

    public double getMaquinaMasa() {
        return maquinaMasa;
    }

    public void setMaquinaMasa(double maquinaMasa) {
        this.maquinaMasa = maquinaMasa;
    }

    public double getMolino() {
        return molino;
    }

    public void setMolino(double molino) {
        this.molino = molino;
    }

    public double getMermaTortilla() {
        return mermaTortilla;
    }

    public void setMermaTortilla(double mermaTortilla) {
        this.mermaTortilla = mermaTortilla;
    }

    @Override
    public String toString() {
        return "VentaMostrador{" +
                "idVenta='" + idVenta + '\'' +
                ", tiempo=" + tiempo +
                ", fecha='" + fecha + '\'' +
                ", idSucursal='" + idSucursal + '\'' +
                ", idEmpleado='" + idEmpleado + '\'' +
                ", repartidores=" + repartidores +
                ", costales=" + costales +
                ", botes=" + botes +
                ", maizNixtamalizado=" + maizNixtamalizado +
                ", masaVendida=" + masaVendida +
                ", tortillaSobra=" + tortillaSobra +
                ", maquinaMasa=" + maquinaMasa +
                ", molino=" + molino +
                ", mermaTortilla=" + mermaTortilla +
                '}';
    }
}
