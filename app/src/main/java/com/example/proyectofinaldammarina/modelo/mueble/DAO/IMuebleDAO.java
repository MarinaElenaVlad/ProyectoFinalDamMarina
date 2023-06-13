package com.example.proyectofinaldammarina.modelo.mueble.DAO;

import android.content.Context;

import com.example.proyectofinaldammarina.Adaptador;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;

import java.util.List;

/**
 * Interfaz específica para la clase Mueble.
 * Recordemos que las interfaces no tienen constructor
 * y que todos sus métodos son abstractos (No tienen
 * definición, sólo declaramos su estructura en la interfaz).
 */
public interface IMuebleDAO {

    public void actualizarMueble(Mueble mueble, Context context);

    public void insertarMueble(Mueble mueble, Context context);
}
