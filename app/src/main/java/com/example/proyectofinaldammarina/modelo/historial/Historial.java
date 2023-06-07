package com.example.proyectofinaldammarina.modelo.historial;

import java.util.ArrayList;
import java.util.List;

public class Historial {

    String identificador;
    List<String> muebleList;
    String usuario;

    public Historial() {
    }

    public Historial(List<String> muebleList, String usuario) {
        this.muebleList = muebleList;
        this.usuario = usuario;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public List<String> getMuebleList() {
        return muebleList;
    }

    public void setMuebleList(List<String> muebleList) {
        this.muebleList = muebleList;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Historial(String identificador, List<String> muebleList, String usuario) {
        this.identificador = identificador;
        this.muebleList = muebleList;
        this.usuario = usuario;
    }
}
