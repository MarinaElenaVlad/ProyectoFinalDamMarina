package com.example.proyectofinaldammarina.modelo.historial;

import com.example.proyectofinaldammarina.modelo.mueble.Mueble;

import java.util.List;

/**
 * Modelo de clase reflejo de la colección historial de FireStore Database
 */
public class Historial {

    // Se declaran los atributos
    String identificador;
    List<Mueble> muebleList;
    String usuario;

    /**
     * Constructor sin parámetros
     */
    public Historial() {
    }

    /**
     * Constructor con parámetros
     */
    public Historial(List<Mueble> muebleList, String usuario) {
        this.muebleList = muebleList;
        this.usuario = usuario;
    }

    /**
     * Métodos get y set de cada atributo
     */
    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public List<Mueble> getMuebleList() {
        return muebleList;
    }

    public void setMuebleList(List<Mueble> muebleList) {
        this.muebleList = muebleList;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Historial(String identificador, List<Mueble> muebleList, String usuario) {
        this.identificador = identificador;
        this.muebleList = muebleList;
        this.usuario = usuario;
    }
}
