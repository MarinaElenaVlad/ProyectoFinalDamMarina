package com.example.proyectofinaldammarina;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ListaFragment extends Fragment {


    private FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        System.out.println(firebaseAuth.getUid());

        return inflater.inflate(R.layout.fragment_lista, container, false);
    }
}