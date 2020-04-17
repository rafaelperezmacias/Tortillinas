package com.zamnadev.tortillinas.Moldes;

public class Producto {

    private String idProducto;
    private String nombre;
    private double precio;
    private boolean modificado;
    private boolean eliminado;
    private boolean formulario;
    private Long alta;

    public Producto() {

    }

    //TODO Constructor copia, luego lo utilizaremos
    public Producto(final Producto p) {
        idProducto = p.idProducto;
        nombre = p.nombre;
        precio = p.precio;
        modificado = p.modificado;
        eliminado = p.eliminado;
        formulario = p.formulario;
    }

    public Producto(String idProducto, String nombre, double precio, boolean modificado, boolean eliminado, Long alta, boolean formulario) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.modificado = modificado;
        this.eliminado = eliminado;
        this.alta = alta;
        this.formulario = formulario;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isModificado() {
        return modificado;
    }

    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Long getAlta() {
        return alta;
    }

    public void setAlta(Long alta) {
        this.alta = alta;
    }

    public boolean isFormulario() {
        return formulario;
    }

    public void setFormulario(boolean formulario) {
        this.formulario = formulario;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "idProducto='" + idProducto + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", modificado=" + modificado +
                ", eliminado=" + eliminado +
                ", formulario=" + formulario +
                ", alta=" + alta +
                '}';
    }
}
