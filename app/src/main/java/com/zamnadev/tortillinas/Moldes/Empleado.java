package com.zamnadev.tortillinas.Moldes;

import java.util.HashMap;

public class Empleado {

    public static String[] NIVELES_DE_USUARIO = {
            "Mostrador",
            "Repartidor",
            "Administrador"
    };

    public static final int TIPO_MOSTRADOR = 0;
    public static final int TIPO_REPARTIDOR = 1;
    public static final int TIPO_ADMIN = 2;

    private String idEmpleado;
    private Nombre nombre;
    private String telefono;
    private int tipo;
    private HashMap<String, String> sucursales;
    private boolean eliminado;
    private long time;
    private boolean conexion;

    public Empleado() {

    }

    public Empleado(String idEmpleado, Nombre nombre, String telefono, int tipo, HashMap<String, String> sucursales, boolean eliminado, long time, boolean conexion) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.telefono = telefono;
        this.tipo = tipo;
        this.sucursales = sucursales;
        this.eliminado = eliminado;
        this.time = time;
        this.conexion = conexion;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Nombre getNombre() {
        return nombre;
    }

    public void setNombre(Nombre nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public HashMap<String, String> getSucursales() {
        return sucursales;
    }

    public void setSucursales(HashMap<String, String> sucursales) {
        this.sucursales = sucursales;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isConexion() {
        return conexion;
    }

    public void setConexion(boolean conexion) {
        this.conexion = conexion;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "idEmpleado='" + idEmpleado + '\'' +
                ", nombre=" + nombre +
                ", telefono='" + telefono + '\'' +
                ", tipo=" + tipo +
                ", sucursales=" + sucursales +
                ", eliminado=" + eliminado +
                ", time=" + time +
                ", conexion=" + conexion +
                '}';
    }
}
