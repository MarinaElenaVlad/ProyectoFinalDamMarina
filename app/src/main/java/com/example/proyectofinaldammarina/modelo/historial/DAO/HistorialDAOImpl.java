package com.example.proyectofinaldammarina.modelo.historial.DAO;

import androidx.annotation.NonNull;

import com.example.proyectofinaldammarina.modelo.historial.Historial;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Esta clase implementa la interfaz específica
 * IHistorialDAO para poder desarrollar sus métodos abstractos.
 */
public class HistorialDAOImpl implements  IHistorialDAO{

    // Se declaran los atributos
    FirebaseFirestore database;
    String nombreColeccion;

    public HistorialDAOImpl(FirebaseFirestore database, String nombreColeccion) {
        //instancia de la base de datos
        this.database = database;
        //nombre de la colección a la que se quiere acceder
        this.nombreColeccion = nombreColeccion;
    }

    /**
     * Método que crea un historial que contendrá una lista con la referencia
     * a los muebles escaneados y el id del usuario al que pertenece
     * @param idUsuario
     */
    @Override
    public void crearHistorial(String idUsuario) {

        Historial historial = new Historial(Arrays.asList(), idUsuario);

        // add() generá un id único para el documento de cada historial de la colección
        database.collection("historial")
                .add(historial)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Después de crear un nuevo historial con éxito se guarda el id autogenerado del documento
                        historial.setIdentificador(documentReference.getId());
                        // También actualizamos ese campo en la base de datos
                        database.collection("historial").document(historial.getIdentificador())
                                .update("identificador", historial.getIdentificador())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        System.out.println("DocumentSnapshot successfully updated!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        System.out.println("Error updating document");
                                    }
                                });
                    }
        });

    }
}
