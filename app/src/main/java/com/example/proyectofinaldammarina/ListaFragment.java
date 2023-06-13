package com.example.proyectofinaldammarina;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
/**
 * Fragment que se agrega dentro del activity del menú cuando se pulsa
 * la opción 'Lista muebles' del navegador inferior que está en la barra inferior
 */
public class ListaFragment extends Fragment {

    // Se declaran las variables
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
        // Se inicializan variables
        db = FirebaseFirestore.getInstance();

        muebleList = new ArrayList<>();

        // Se asocian las variables con los elementos xml del layout asociado a este activity
        searchView = root.findViewById(R.id.barraBusquedaLista);
        // Quitamos el foco de la barra de buscar
        searchView.clearFocus();
        /**
         * Controla el evento que ocurre cada vez que
         * se escribe texto nuevo en la barra de búsqueda
         */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Cada vez que se escribe en la barra de búsqueda se llama a esta función
                buscarLista(newText);
                return false;
            }
        });

        lista = root.findViewById(R.id.RecyclerViewMain);
        // Se indica como se va a mostrar la información en el recycler view (1 columna)
        lista.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Creamos un adaptador especifico para nuestro recyclerview
        adaptador = new Adaptador(muebleList, getActivity());
        lista.setAdapter(adaptador);

        CollectionReference collectionReference = db.collection("muebles");
        // Obtenemos todos los documentos de la colección "muebles"
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Mueble mueble = document.toObject(Mueble.class);
                    // Se añaden los muebles a la lista
                    muebleList.add(mueble);
                }
                // Se notifica al adaptador del cambio para que actualice la lista
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

    /**
     *
     * @param textoNuevo
     */
    private void buscarLista(String textoNuevo){
        List<Mueble> listaFiltrada = new ArrayList<>();

        CollectionReference collectionReference = db.collection("muebles");

        // Obtenemos todos los documentos de la colección "muebles"
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Mueble mueble = document.toObject(Mueble.class);
                    // Se añadirán a una lista nueva los documentos de
                    // la colección que contengan el texto nuevo en su nombre
                    if(mueble.getNombre().toLowerCase().contains(textoNuevo.toLowerCase())){
                        //Se añade el mueble a la lista
                        listaFiltrada.add(mueble);
                    }
                    // Se actualiza la lista que se muestra
                    if(listaFiltrada.isEmpty()){
//                        Toast.makeText(getContext(), "No se encuentran resultados.", Toast.LENGTH_LONG).show();
                        adaptador.setSearchView(listaFiltrada);
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