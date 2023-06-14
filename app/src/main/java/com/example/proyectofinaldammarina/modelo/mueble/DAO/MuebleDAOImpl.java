package com.example.proyectofinaldammarina.modelo.mueble.DAO;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.proyectofinaldammarina.Adaptador;
import com.example.proyectofinaldammarina.CrearMuebleActivity;
import com.example.proyectofinaldammarina.DetalleMuebleActivity;
import com.example.proyectofinaldammarina.MenuActivity;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
/**
 * Esta clase implementa la interfaz específica
 * IMuebleDAO para poder desarrollar sus métodos abstractos.
 */
public class MuebleDAOImpl implements IMuebleDAO {

    // Se declaran los atributos
    FirebaseFirestore database;
    String nombreColeccion;

    public MuebleDAOImpl(FirebaseFirestore database, String nombreColeccion) {
        //instancia de la base de datos
        this.database = database;
        //nombre de la colección a la que se quiere acceder
        this.nombreColeccion = nombreColeccion;
    }

    /**
     * Método que actualiza 4 atributos de un objeto de la clase Mueble.
     * @param mueble
     * @param context
     */
    @Override
    public void actualizarMueble(Mueble mueble, Context context) {
        DocumentReference muebleReferencia = database.collection(nombreColeccion).document(mueble.getCodigoQr());

        // Actualizamos los campos nombre, precio, medidas y descripcion
        muebleReferencia.update("nombre", mueble.getNombre(), "precio", mueble.getPrecio(),
                "medidas", mueble.getMedidas(), "descripcion", mueble.getDescripcion()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Campo/s actualizados.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Método que inserta un mueble en la base de datos
     * @param mueble
     * @param context
     */
    @Override
    public void insertarMueble(Mueble mueble, Context context) {
        DocumentReference docRef = database.collection(nombreColeccion).document(mueble.getCodigoQr());
        docRef.set(mueble)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(context, "Mueble creado con éxito!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MenuActivity.class);
                        // Se vuelve al menu
                        context.startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error al crear mueble", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
