package com.example.proyectofinaldammarina.modelo.usuario;

/**
 * Explicacion clase!!
 */
public class Usuario {

    // Se declaran los atributos
    private String nombre;
    private String email;
    private String password;
    private String idRol;
    /**
     * HISTORIAL!!!!!
     */

    /**
     * Constructor sin parámetros
     */
    public Usuario() {
    }

    /**
     * Constructor con parámetros
     */
    public Usuario(String nombre, String email, String idRol) {
        this.nombre = nombre;
        this.email = email;
//        this.password = password;
        this.idRol = idRol;
    }

    /**
     * Métodos get y set de cada atributo
     */
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getIdRol() {
        return idRol;
    }

    public void setIdRol(String idRol) {
        this.idRol = idRol;
    }
}
