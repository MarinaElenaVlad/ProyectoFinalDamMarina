package com.example.proyectofinaldammarina.modelo.mueble;

import java.io.Serializable;

/**
 * Explicacion clase!!
 */
public class Mueble implements Serializable {

    private String codigoQr;
    private String imagen;
    private String nombre;
    private Double precio;
    private String medidas;
    private String descripcion;
    //Relaciones
    /**
     * Mirar!!
     */
    private String estanteriaId;
    private String zonaId;

    /**
     * Constructor sin parámetros
     */
    public Mueble() {
    }

    /**
     * Constructor con parámetros
     */
    public Mueble(String codigoQr, String imagen, String nombre, Double precio, String medidas, String descripcion) {
        this.codigoQr = codigoQr;
        this.imagen = imagen;
        this.nombre = nombre;
        this.precio = precio;
        this.medidas = medidas;
        this.descripcion = descripcion;
    }



    /**
     * Métodos get y set de cada atributo
     */
    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getMedidas() {
        return medidas;
    }

    public void setMedidas(String medidas) {
        this.medidas = medidas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstanteriaId() {
        return estanteriaId;
    }

    public void setEstanteriaId(String estanteriaId) {
        this.estanteriaId = estanteriaId;
    }

    public String getZonaId() {
        return zonaId;
    }

    public void setZonaId(String zonaId) {
        this.zonaId = zonaId;
    }

    @Override
    public String toString() {
        return "Mueble{" +
                "codigoQr='" + codigoQr + '\'' +
                ", imagen='" + imagen + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", medidas='" + medidas + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
