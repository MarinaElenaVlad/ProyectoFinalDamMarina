package com.example.proyectofinaldammarina.modelo.mueble.DAO;

import android.content.Context;

import com.example.proyectofinaldammarina.Adaptador;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;

import java.util.List;

public interface IMuebleDAO {

//    public List<Mueble> rellenarListaMuebles(Adaptador adaptador);

    public void actualizarMueble(String id, Mueble mueble, Context context);
}
