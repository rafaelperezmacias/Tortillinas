package com.zamnadev.tortillinas.Moldes;

public class Cuenta {

    private String idCuenta;
    private String usuario;
    private String password;

    public Cuenta() {

    }

    public Cuenta(String idCuenta, String usuario, String password) {
        this.idCuenta = idCuenta;
        this.usuario = usuario;
        this.password = password;
    }

    public String getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
