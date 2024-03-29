package com.example.proyectofinaldammarina.modelo.zona;

/**
 * Modelo de clase reflejo de la colección zonas de FireStore Database
 */
public class ZonaExposicion {

    // Atributos
    private String codigo;
    private String categoria;

    private String planta;
    private String imagen;

    /**
     * Constructor sin parametros
     */
    public ZonaExposicion() {
    }

    /**
     * Constructor con parámetros
     * @param codigo
     * @param categoria
     * @param planta
     * @param imagen
     */
    public ZonaExposicion(String codigo, String categoria, String planta, String imagen) {
        this.codigo = codigo;
        this.categoria = categoria;
        this.planta = planta;
        this.imagen = imagen;
    }

    /**
     * Métodos get y set de cada atributo
     */
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
