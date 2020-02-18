package com.zamnadev.tortillinas.Moldes;

import java.io.Serializable;

public class Nombre implements Serializable {

    private String nombres;
    private String apellidos;

    public Nombre() {

    }

    public Nombre(String nombres, String apellidos) {
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @Override
    public String toString() {
        return "Nombre{" +
                "nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                '}';
    }
}
