package com.example.proyectofinaldammarina.modelo.historial.DAO;

import androidx.annotation.NonNull;

import com.example.proyectofinaldammarina.modelo.historial.Historial;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class HistorialDAOImpl implements  IHistorialDAO{

    // Atributos
    FirebaseFirestore database;
    String nombreColeccion;

    public HistorialDAOImpl(FirebaseFirestore database, String nombreColeccion) {
        this.database = database;
        this.nombreColeccion = nombreColeccion;
    }

    @Override
    public void crearHistorial(String idUsuario) {

        Historial historial = new Historial(Arrays.asList(), idUsuario);

        database.collection("historial")
                .add(historial)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Se guarda el id automatico
                        //MIRAR SI SE GUARDA EN LA BD!!
                        historial.setIdentificador(documentReference.getId());
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
                                        System.out.println("Error updating document");                                    }
                                });
                    }
        });

    }
}
