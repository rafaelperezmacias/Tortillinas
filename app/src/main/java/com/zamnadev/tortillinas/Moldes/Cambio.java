package com.zamnadev.tortillinas.Moldes;

public class Cambio {

    //Registrar cambios
    //Categoria
    public static final int SUCURSALES = 10;
    //Subcategoria
    public static final int SUCURSALES_NOMBRE = 11;
    public static final int SUCURSALES_DIRECCION = 12;
    public static final int SUCURSALES_CUBETAS = 13;
    public static final int SUCURSALES_ELIMINADO = 14;

    private String idCambio;
    private int categoria;
    private int subCategoria;
    private String mensaje;
    private Long fecha;

    public Cambio()
    {

    }

    public Cambio(String idCambio, int categoria, int subCategoria, String mensaje, Long fecha)
    {
        this.idCambio = idCambio;
        this.categoria = categoria;
        this.subCategoria = subCategoria;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public String getIdCambio() {
        return idCambio;
    }

    public void setIdCambio(String idCambio) {
        this.idCambio = idCambio;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(int subCategoria) {
        this.subCategoria = subCategoria;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getFecha() {
        return fecha;
    }

    public void setFecha(Long fecha) {
        this.fecha = fecha;
    }
}
