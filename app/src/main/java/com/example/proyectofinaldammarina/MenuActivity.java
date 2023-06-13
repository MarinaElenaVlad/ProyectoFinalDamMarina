package com.example.proyectofinaldammarina;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
 * Clase que controla los eventos del menú (XML) de la aplicación.
 */

public class MenuActivity extends AppCompatActivity {

    // Se declaran variables
    private BottomNavigationView bottomNavigationView;

    private Toolbar toolbar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // Se inicializan variables
        firebaseAuth = FirebaseAuth.getInstance();

        // Se asocian las variables con los elementos xml del layout asociado a este activity
        bottomNavigationView = findViewById(R.id.barraNavigationBottom);
        toolbar = findViewById(R.id.toolbar);

        // Esta comparación sirve si un usuario hace logout e intenta volver al intent anterior
        // Esto no le permitirá acceder
        if(firebaseAuth.getCurrentUser() != null) {
            setSupportActionBar(toolbar);

            // Cuando se carga el menú el fragment "Inicio" está marcada por defecto
            reemplazarFragment(new InicioFragment());
            bottomNavigationView.setBackground(null);

            //Cada vez que se seleccione un item del menú se cambiará de fragment
            bottomNavigationView.setOnItemSelectedListener(item -> {
                switch (item.getItemId()) {
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
        }else{
            // Si el usuario es nulo, se finaliza el intent
            finish();
        }
    }

    /**
     * Método que reemplaza el fragment que se está mostrando en la pantalla del menú
     * @param fragment
     */
    private void reemplazarFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Método que muestra la toolbar personalizada
     * @param menu
     * @return boolean
     */
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * Método que controla el evento que ocurre
     * al pulsar un item de la toolbar
     * @param item
     * @return boolean
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
           if(id == R.id.logout){
               AlertDialog dialogo = new AlertDialog
                       .Builder(MenuActivity.this).setPositiveButton("Sí, cerrar", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //Hace que el usuario sea null
                       firebaseAuth.signOut();
                       Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                       startActivity(intent);
                       finish();
                   }
               }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                       Toast.makeText(MenuActivity.this, "Logout cancelado.", Toast.LENGTH_LONG).show();
                   }
               }).setTitle("Confirmar") // El título
                       .setMessage("¿Desea cerrar sesión?") // El mensaje
                       .create();
               dialogo.show();
            }
        return true;
    }
}