package com.example.proyectofinaldammarina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.historial.Historial;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistorialActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listaMostrar;
    private List<String> listaMuebles = new ArrayList<>();//la inicializamos para que no de error si el historial está vacío

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private Historial historial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Toast.makeText(HistorialActivity.this, firebaseAuth.getUid(), Toast.LENGTH_LONG).show();


        db.collection("historial").whereEqualTo("usuario", firebaseAuth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                historial = document.toObject(Historial.class);
                                listaMuebles = historial.getMuebleList();
                                //https://examples.javacodegeeks.com/java-development/core-java/util/collections/reverse-order-of-list-example/
                                Collections.reverse(listaMuebles);
                            }

                        }else{
                            Toast.makeText(HistorialActivity.this, "Error getting documents:", Toast.LENGTH_LONG).show();
                        }

                        if(listaMuebles.size() > 0) {
                            listaMostrar = findViewById(R.id.listaHistorial);
                            ArrayAdapter<String> mueblesAdapter = new ArrayAdapter<String>(HistorialActivity.this,
                                    android.R.layout.simple_list_item_1, listaMuebles);
                            listaMostrar.setAdapter(mueblesAdapter);

                            listaMostrar.setOnItemClickListener(HistorialActivity.this);
                        }else{
                            Toast.makeText(HistorialActivity.this, "HISTORIAL VACÍO", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //https://www.youtube.com/watch?v=NhiUTjm2BrE
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idMueble = listaMuebles.get(position);

        db.collection("muebles").document(idMueble).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Mueble mueble = document.toObject(Mueble.class);
                    Intent intent = new Intent(HistorialActivity.this, ComparacionMuebleActivity.class);
                    intent.putExtra("mueble", mueble);
                    startActivity(intent);
                }
            }
        });



    }
}