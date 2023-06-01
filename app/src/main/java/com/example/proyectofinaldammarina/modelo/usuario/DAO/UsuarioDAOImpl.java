package com.example.proyectofinaldammarina.modelo.usuario.DAO;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.proyectofinaldammarina.modelo.usuario.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * comentario
 */
public class UsuarioDAOImpl implements IUsuarioDAO {

    // Atributos
    FirebaseFirestore database;
    String nombreColeccion;

    public UsuarioDAOImpl(FirebaseFirestore db, String nombreColeccion) {
        this.database = db;
        this.nombreColeccion = nombreColeccion;

    }

    /**
     * Método save usuario en la base de datos (Firestore)
     * @param usuario
     * @return
     */
    @Override
    public Usuario insertarUsuario(Usuario usuario, String idNuevoUsuario) {
        database.collection(nombreColeccion).document(idNuevoUsuario)
                .set(new Usuario(usuario.getNombre(),usuario.getEmail(),
                        usuario.getPassword(), "cliente"));

        return null;
    }

//    @Override
//    public void actualizarPassword(String idUsuario, String nuevaPassword) {
//        DocumentReference userReferencia = database.collection(nombreColeccion)
//                .document(idUsuario);
//
//        userReferencia.update("password", nuevaPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                }else{
//                    System.out.println("Error al actualizar.");
//                }
//            }
//        });
//    }
}
