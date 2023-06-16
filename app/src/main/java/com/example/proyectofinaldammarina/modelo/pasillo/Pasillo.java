package com.example.proyectofinaldammarina.modelo.pasillo;

/**
 * Modelo de clase reflejo de la colección pasillos de FireStore Database
 */
public class Pasillo {

    // Atributos
    private String codigo;
    private int cantMax;
    private int stock;

    /**
     * Constructor sin parametros
     */
    public Pasillo() {
    }

    /**
     * Constructor con todos los parámetros
     * @param codigo
     * @param cantMax
     * @param stock
     */
    public Pasillo(String codigo, int cantMax, int stock) {
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
        return "Pasillo{" +
                "codigo='" + codigo + '\'' +
                ", cantMax=" + cantMax +
                ", stock=" + stock +
                '}';
    }
}
