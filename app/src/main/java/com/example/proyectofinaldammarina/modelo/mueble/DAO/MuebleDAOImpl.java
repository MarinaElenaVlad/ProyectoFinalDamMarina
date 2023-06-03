package com.example.proyectofinaldammarina.modelo.mueble.DAO;

import android.widget.Toast;

import com.example.proyectofinaldammarina.Adaptador;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
