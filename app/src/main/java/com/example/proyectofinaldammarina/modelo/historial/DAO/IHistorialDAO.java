package com.example.proyectofinaldammarina.modelo.historial.DAO;

/**
 * Interfaz específica para la clase Historial.
 * Recordemos que las interfaces no tienen constructor
 * y que todos sus métodos son abstractos (No tienen
 * definición, sólo declaramos su estructura en la interfaz).
 */
public interface IHistorialDAO {

    public void crearHistorial(String idUsuario);
}
