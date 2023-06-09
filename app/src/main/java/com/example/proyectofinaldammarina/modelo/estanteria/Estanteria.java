package com.example.proyectofinaldammarina.modelo.estanteria;

import java.io.Serializable;

public class Estanteria implements Serializable {


    /**
     * obtener pasillo??? # + DEPENENCIA
     */
    private String codigo; //compuesto del cod pasillo
    private int cantMax;
    private int stock;

    public Estanteria() {
    }

    public Estanteria(String codigo) {
        this.codigo = codigo;
    }

    public Estanteria(String codigo, int cantMax, int stock) {
        this.codigo = codigo;
        this.cantMax = cantMax;
        this.stock = stock;
    }

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
