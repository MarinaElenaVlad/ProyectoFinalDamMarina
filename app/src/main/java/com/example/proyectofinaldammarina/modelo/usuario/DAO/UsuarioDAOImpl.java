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

    @Override
    public void actualizarFotoPerfil(String id, String imagenUri, Context context) {
        DocumentReference usuarioReferencia = database.collection(nombreColeccion).document(id);

        /**
         * campo mismo nombre que el de la bd, sino te crea uno nuevo!
         */
        usuarioReferencia.update("imagenUsuario", imagenUri).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Campo/s actualizados.", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, MenuActivity.class));
                }else{
                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void actualizarNombrePerfil(String id, String nombre, Context context) {
        DocumentReference usuarioReferencia = database.collection(nombreColeccion).document(id);

        /**
         * campo mismo nombre que el de la bd, sino te crea uno nuevo!
         */
        usuarioReferencia.update("nombre", nombre).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){
                   Toast.makeText(context, "Campo/s actualizados.", Toast.LENGTH_SHORT).show();
                   context.startActivity(new Intent(context, MenuActivity.class));
               }else{
                   Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show();
               }
           }
        });
    }

//    @Override
//    public String devolverRolUsuario(String key) {
//        /**
//         * no ponerlo final, porque luego su valor no puede cambiar!!
//         */
//        String[] rol = {""};
//        DocumentReference docRef = database.collection(nombreColeccion).document(key);
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    Usuario usuario = documentSnapshot.toObject(Usuario.class);
//                    System.out.println(usuario.toString());
//                    rol[0] = usuario.getIdRol();
//                }
//            }
//        });
//
//        if(docRef.get().isSuccessful()){
//            System.out.println("ROL" + rol[0]);
//            return rol[0];
//        }
//    }

//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Usuario usuario = document.toObject(Usuario.class);
//                        System.out.println(usuario.toString());
//                        rol[0] = usuario.getIdRol();
//                    }
//                }
//            }
//        });
//        System.out.println("ROL" + rol[0]);
//        return rol[0];
//    }

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
