package com.example.proyectofinaldammarina;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.mueble.DAO.MuebleDAOImpl;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaFragment extends Fragment {

    //Agenda clase!! proyecto tutorial

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private RecyclerView lista;
    private List<Mueble> muebleList;
    private Adaptador adaptador;

    public ListaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lista, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
       //Toast.makeText(getContext(), firebaseAuth.getUid(), Toast.LENGTH_LONG).show();

        db = FirebaseFirestore.getInstance();

        muebleList = new ArrayList<>();

        lista = root.findViewById(R.id.RecyclerViewMain);
        lista.setLayoutManager(new LinearLayoutManager(getActivity()));
        adaptador = new Adaptador(muebleList, getActivity());
        lista.setAdapter(adaptador);

        CollectionReference collectionReference = db.collection("muebles");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Mueble mueble = document.toObject(Mueble.class);
                    muebleList.add(mueble);
                    System.out.println(muebleList);
                }
                adaptador.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

}