package com.example.proyectofinaldammarina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.mueble.Mueble;

/**
 * QUE NO SE HAYA CREADO COMO KOTLIN!! LA CLASE
 */
public class ComparacionMuebleActivity extends AppCompatActivity {

    private Mueble mueble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparacion_mueble);

        mueble = (Mueble) getIntent().getSerializableExtra("mueble");



    }
}