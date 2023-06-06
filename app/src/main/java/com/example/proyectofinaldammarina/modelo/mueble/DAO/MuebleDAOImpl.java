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

public class MuebleDAOImpl implements IMuebleDAO {

    // Atributos
    FirebaseFirestore database;
    String nombreColeccion;

    public MuebleDAOImpl(FirebaseFirestore database, String nombreColeccion) {
        this.database = database;
        this.nombreColeccion = nombreColeccion;
    }

    @Override
    public void actualizarMueble(String id, Mueble mueble, Context context) {
        DocumentReference muebleReferencia = database.collection(nombreColeccion).document(id);

        muebleReferencia.update("nombre", mueble.getNombre(), "precio", mueble.getPrecio(),
                "medidas", mueble.getMedidas(), "descripcion", mueble.getDescripcion()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Campo/s actualizados.", Toast.LENGTH_SHORT).show();
                    //Volver a la pantalla principal o algo!
                }else{
                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void insertarMueble(Mueble mueble, Context context) {
        DocumentReference docRef = database.collection(nombreColeccion).document(mueble.getCodigoQr());
        docRef.set(mueble)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(context, "DocumentSnapshot successfully written!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MenuActivity.class);
                        context.startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error writing document!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    @Override
//    public List<Mueble> rellenarListaMuebles(Adaptador adaptador) {
//        List<Mueble> muebleList = new ArrayList<>();
//            database.collection(nombreColeccion).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(Task<QuerySnapshot> task) {
//                    System.out.println("ACAAAAAAAAAAA");
//                    muebleList.clear();
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Mueble mueble = document.toObject(Mueble.class);
//                        muebleList.add(mueble);
//                    }
//                    adaptador.notifyDataSetChanged();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(Exception e) {
//                    e.printStackTrace();
//                }
//            });
//
//        return muebleList;
//        }

}
