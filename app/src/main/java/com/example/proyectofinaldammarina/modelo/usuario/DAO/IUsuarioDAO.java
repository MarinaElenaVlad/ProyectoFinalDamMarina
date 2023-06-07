package com.example.proyectofinaldammarina.modelo.usuario.DAO;

import android.content.Context;

import com.example.proyectofinaldammarina.modelo.usuario.Usuario;

/**
 * comentario
 */
public interface IUsuarioDAO {

   public Usuario insertarUsuario(Usuario usuario, String idNuevoUsuario);

   //Actualizar contraseña
//   public void actualizarPassword(String idUsuario, String nuevaPassword);

//   public String devolverRolUsuario(String key);

   public void actualizarFotoPerfil(String id, String imagenUri, Context context);



}
