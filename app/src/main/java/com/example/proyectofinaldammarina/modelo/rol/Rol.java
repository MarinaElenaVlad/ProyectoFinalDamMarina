package com.example.proyectofinaldammarina.modelo.rol;

/**
 * Modelo de clase reflejo de la colección roles de FireStore Database
 */
public class Rol {

    // Atributos
    private String id;
    private String descripcion;

    /**
     * Constructor vacío
     */
    public Rol() {
    }

    /**
     * Constructor con parámetros
     */
    public Rol(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    /**
     * Métodos get y set de cada atributo
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Rol{" +
                "id='" + id + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
