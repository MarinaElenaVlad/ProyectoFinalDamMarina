package com.example.proyectofinaldammarina;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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
import java.util.Locale;
/**
 * Fragment que se agrega dentro del activity del menú cuando se pulsa
 * la opción 'Lista muebles' del navegador inferior que está en la barra inferior
 */
public class ListaFragment extends Fragment {

    // Se declaran las variables
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private RecyclerView lista;
    private List<Mueble> muebleList;
    private Adaptador adaptador;

    private SearchView searchView;

    /**
     * Constructor vacío
     */
    public ListaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lista, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        muebleList = new ArrayList<>();

        // Se asocian las variables con los elementos xml del layout asociado a este activity
        searchView = root.findViewById(R.id.barraBusquedaLista);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarLista(newText);
                return false;
            }
        });

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

    private void buscarLista(String textoNuevo){
        List<Mueble> listaFiltrada = new ArrayList<>();

        /***
         * PROBLEMAS GET!!
         */
        CollectionReference collectionReference = db.collection("muebles");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Mueble mueble = document.toObject(Mueble.class);
                    if(mueble.getNombre().toLowerCase().contains(textoNuevo.toLowerCase())){
                        //Se añade el mueble a la lista
                        listaFiltrada.add(mueble);
                    }
                    if(listaFiltrada.isEmpty()){
                       // Toast.makeText(getContext(), "No hay resultados", Toast.LENGTH_LONG).show();
                        adaptador.setSearchView(listaFiltrada);//Asi no sale nada en la lista
                    }else{
                        adaptador.setSearchView(listaFiltrada);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure( Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}