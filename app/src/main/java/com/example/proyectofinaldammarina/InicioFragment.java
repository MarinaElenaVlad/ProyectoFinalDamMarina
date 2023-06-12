package com.example.proyectofinaldammarina;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment que se agrega dentro del activity del menú cuando se pulsa
 * la opción 'Inicio' del navegador inferior que está en la barra inferior
 */
public class InicioFragment extends Fragment {

    /**
     * Constructor vacío
     */
    public InicioFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }
}