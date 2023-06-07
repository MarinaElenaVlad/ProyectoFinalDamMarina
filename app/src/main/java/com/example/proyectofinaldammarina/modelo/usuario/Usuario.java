package com.example.proyectofinaldammarina.modelo.usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Explicacion clase!!
 */
public class Usuario {

    // Se declaran los atributos
    /**
     * poner id??
     */
    private String nombre;
    private String email;
    private String idRol;
    private String imagenUsuario;
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
    public Usuario(String nombre, String email, String idRol, String imagenUsuario) {
        this.nombre = nombre;
        this.email = email;
//        this.password = password;
        this.idRol = idRol;
        this.imagenUsuario = imagenUsuario;
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


    public String getIdRol() {
        return idRol;
    }

    public void setIdRol(String idRol) {
        this.idRol = idRol;
    }

    public String getImagenUsuario() {
        return imagenUsuario;
    }

    public void setImagenUsuario(String imagenUsuario) {
        this.imagenUsuario = imagenUsuario;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", idRol='" + idRol + '\'' +
                '}';
    }
}
