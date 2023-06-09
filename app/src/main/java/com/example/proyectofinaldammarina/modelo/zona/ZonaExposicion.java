package com.example.proyectofinaldammarina.modelo.zona;

public class ZonaExposicion {

    private String codigo;
    private String planta;
    private String categoria;
    private String imagen;

    //constructor sin parametros
    public ZonaExposicion() {
    }

    //constructor con parametros
    public ZonaExposicion(String codigo, String planta, String categoria, String imagen) {
        this.codigo = codigo;
        this.planta = planta;
        this.categoria = categoria;
        this.imagen = imagen;
    }

    //get y set

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPlanta() {
        return planta;
    }

    public void setPlanta(String planta) {
        this.planta = planta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return categoria + ", planta: " + planta;
    }
}
