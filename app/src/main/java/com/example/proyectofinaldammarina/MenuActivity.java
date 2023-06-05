package com.example.proyectofinaldammarina;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.mueble.DAO.MuebleDAOImpl;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * HACER ESTO EN UNA RAMA APARTE Y LUEGO JUNTARLA, POR SI TE EQUIVOCAS, mirar video!!
 *
 * USUARIO != NULL, VIDEO !MIRAR !
 */

public class MenuActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private BottomNavigationView bottomNavigationView;

    private Toolbar toolbar;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        firebaseAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.barraNavigationBottom);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Toast.makeText(MenuActivity.this, firebaseAuth.getUid(), Toast.LENGTH_LONG).show();

        // Cuando se carga el menú la opción "Inicio" está marcada por defecto
        reemplazarFragment(new InicioFragment());
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.inicio:
                    reemplazarFragment(new InicioFragment());
                    break;
                case R.id.escanearQr:
                    reemplazarFragment(new EscanerFragment());
                    break;
                case R.id.listaMuebles:
                    reemplazarFragment(new ListaFragment());
                    break;
                case R.id.perfil:
                    reemplazarFragment(new PerfilFragment());
                    break;
            }
            return true;
        });
    }

    private void reemplazarFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                finish();
                break;
        }

        return true;
    }


}