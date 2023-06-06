package com.example.proyectofinaldammarina;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.example.proyectofinaldammarina.modelo.usuario.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HistorialFragment extends Fragment {

    private ListView listaMostrar;
    private List<String> listaMuebles;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;


    public HistorialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_historial, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        db.collection("usuarios").document(firebaseAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);

                listaMuebles = usuario.getMuebles();
                System.out.println("ARRAY:" + usuario.getMuebles());
                System.out.println("ARRAY2:" + listaMuebles);

                if(listaMuebles.size() > 0) {
                    listaMostrar = root.findViewById(R.id.listaHistorial);
                    ArrayAdapter<String> mueblesAdapter = new ArrayAdapter<String>(root.getContext(),
                            android.R.layout.simple_list_item_1, listaMuebles);
                    listaMostrar.setAdapter(mueblesAdapter);
                }else{
                    Toast.makeText(getActivity(), "HISTORIAL VACÍO", Toast.LENGTH_LONG).show();
                }

            }
        });

        return root;
    }
}