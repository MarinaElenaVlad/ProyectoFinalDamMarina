package com.example.proyectofinaldammarina;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.firestore.FirebaseFirestore;

public class CrearMuebleActivity extends AppCompatActivity {

    // Se declaran variables
    private EditText nombre, precio, medidas, descripcion;
    private ImageView imagenSubir;
    private Spinner spinnerEstanteria, spinnerZona;

    private FirebaseFirestore db;

//    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_mueble);

        // Se asocian las variables con los elementos xml del layout asociado a este activity
        nombre = findViewById(R.id.nombreCrear);
        precio = findViewById(R.id.precioCrear);
        medidas = findViewById(R.id.medidasCrear);
        descripcion = findViewById(R.id.descripcionCrear);

        imagenSubir = findViewById(R.id.imagenSubir);

        spinnerEstanteria = findViewById(R.id.spinnerEstanteria);
        spinnerZona = findViewById(R.id.spinnerZona);



    }
}