package com.zamnadev.tortillinas.Moldes;

public class Empleado {

    public static final int TIPO_MOSTRADOR = 1;
    public static final int TIPO_REPARTIDOR = 2;
    public static final int TIPO_ADMIN = 3;

    private String idEmpleado;
    private Nombre nombre;
    private String telefono;
    private int tipo;
    private String idSucursal;
    private boolean eliminado;

    public Empleado() {

    }

    public Empleado(String idEmpleado, Nombre nombre, String telefono, int tipo, String idSucursal, boolean eliminado) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.telefono = telefono;
        this.tipo = tipo;
        this.idSucursal = idSucursal;
        this.eliminado = eliminado;
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

    public String getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(String idSucursal) {
        this.idSucursal = idSucursal;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
}
