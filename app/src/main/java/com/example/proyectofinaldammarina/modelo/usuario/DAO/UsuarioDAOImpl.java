package com.example.proyectofinaldammarina.modelo.usuario.DAO;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.proyectofinaldammarina.MenuActivity;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.example.proyectofinaldammarina.modelo.usuario.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Esta clase implementa la interfaz específica
 * IUsuarioDAO para poder desarrollar sus métodos abstractos.
 */
public class UsuarioDAOImpl implements IUsuarioDAO {

    // Se declaran los atributos
    FirebaseFirestore database;
    String nombreColeccion;

    public UsuarioDAOImpl(FirebaseFirestore db, String nombreColeccion) {
        //instancia de la base de datos
        this.database = db;
        //nombre de la colección a la que se quiere acceder
        this.nombreColeccion = nombreColeccion;
    }

    /**
     * Método que inserta un usuario en la base de datos (Firestore)
     * Por parámetro recibe el usuario (Objecto) a insertar y el id
     * (Clave primaria de ese documento en la colección usuario).
     * El id será el uid (Único) que la propiedad authentification asigna a cada usuario.
     * @param usuario
     * @param idNuevoUsuario
     * @return Usuario
     */
    @Override
    public Usuario insertarUsuario(Usuario usuario, String idNuevoUsuario) {
        database.collection(nombreColeccion).document(idNuevoUsuario)
                .set(usuario);
        return null;
    }

    /**
     * Método que actualiza la foto de perfil del usuario
     * @param id
     * @param imagenUri
     * @param context
     */
    @Override
    public void actualizarFotoPerfil(String id, String imagenUri, Context context) {
        DocumentReference usuarioReferencia = database.collection(nombreColeccion).document(id);

        usuarioReferencia.update("imagenUsuario", imagenUri).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Foto actualizada.", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, MenuActivity.class));
                }else{
                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Método que actualiza el nombre del usuario
     * @param id
     * @param nombre
     * @param context
     */
    @Override
    public void actualizarNombrePerfil(String id, String nombre, Context context) {
        DocumentReference usuarioReferencia = database.collection(nombreColeccion).document(id);
        usuarioReferencia.update("nombre", nombre).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){
                   Toast.makeText(context, "Nombre actualizado.", Toast.LENGTH_SHORT).show();
                   context.startActivity(new Intent(context, MenuActivity.class));
               }else{
                   Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show();
               }
           }
        });
    }
}
