package com.example.proyectofinaldammarina.modelo.usuario.DAO;

import com.example.proyectofinaldammarina.modelo.usuario.Usuario;

/**
 * comentario
 */
public interface IUsuarioDAO {

   public Usuario insertarUsuario(Usuario usuario, String idNuevoUsuario);

   //Actualizar contraseña
//   public void actualizarPassword(String idUsuario, String nuevaPassword);



}
