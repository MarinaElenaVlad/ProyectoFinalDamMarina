package com.example.proyectofinaldammarina.modelo.usuario.DAO;

import android.content.Context;

import com.example.proyectofinaldammarina.modelo.usuario.Usuario;

/**
 * Interfaz específica para la clase Usuario.
 * Recordemos que las interfaces no tienen constructor
 * y que todos sus métodos son abstractos (No tienen
 * definición, sólo declaramos su estructura en la interfaz).
 */
public interface IUsuarioDAO {

   public Usuario insertarUsuario(Usuario usuario, String idNuevoUsuario);

   public void actualizarFotoPerfil(String id, String imagenUri, Context context);

   public void actualizarNombrePerfil(String id, String nombre, Context context);



}
