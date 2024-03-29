package com.example.proyectofinaldammarina.modelo.estanteria;

import java.io.Serializable;

/**
 * Modelo de clase reflejo de la colección estanterias de FireStore Database
 */
public class Estanteria implements Serializable {

    // Atributos
    private String codigo;
    private int cantMax;
    private int stock;

    /**
     * Constructor sin parametros
     */
    public Estanteria() {
    }

    /**
     * Constructor con un parámetro
     * @param codigo
     */
    public Estanteria(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Constructor con todos los parámetros
     * @param codigo
     * @param cantMax
     * @param stock
     */
    public Estanteria(String codigo, int cantMax, int stock) {
        this.codigo = codigo;
        this.cantMax = cantMax;
        this.stock = stock;
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

    public int getCantMax() {
        return cantMax;
    }

    public void setCantMax(int cantMax) {
        this.cantMax = cantMax;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return codigo;
    }
}
