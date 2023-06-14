package com.example.proyectofinaldammarina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.historial.Historial;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que mostrará el historial de muebles escaneados
 * correspondiente al usuario que está usando la aplicación.
 */
public class HistorialActivity extends AppCompatActivity{

    // Se declaran las variables
    private RecyclerView listaMostrar;
    private List<Mueble> listaMuebles;// Se inicializa para que no de error si el historial está vacío

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private Historial historial;

    private Toolbar toolbar;

    private AdaptadorHistorial adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        // Se inicializan variables
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbarHistorialMueble);
        // Se carga la toolbar
        setSupportActionBar(toolbar);

        listaMuebles = new ArrayList<>();

        listaMostrar = findViewById(R.id.listaHistorial);
        listaMostrar.setLayoutManager(new LinearLayoutManager(HistorialActivity.this));
        adaptador = new AdaptadorHistorial(HistorialActivity.this, listaMuebles);
        listaMostrar.setAdapter(adaptador);


        /**
         * Se busca el historial del usuario actual
         */
        db.collection("historial").whereEqualTo("usuario", firebaseAuth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                historial = document.toObject(Historial.class);
                            }
                            listaMuebles.clear();
                            // Se guardan los datos del historial (Tanto si la lista estaba vacía como si no)
                            listaMuebles.addAll(historial.getMuebleList());
                            // Se cambia el orden a la lista para que se muestren de más reciente a más antiguo
                            Collections.reverse(listaMuebles);
                            adaptador.notifyDataSetChanged();

                        }else{
                            Toast.makeText(HistorialActivity.this, "Error getting documents:", Toast.LENGTH_LONG).show();
                        }

                        if(listaMuebles.isEmpty()) {
                            // Se usa un diálogo que informa al usuario si la lista está vacía
                            AlertDialog dialogoOk = new AlertDialog.Builder(HistorialActivity.this).create();
                            dialogoOk.setTitle("Aviso");
                            dialogoOk.setIcon(R.drawable.ic_baseline_info_24);
                            dialogoOk.setMessage("¡El historial está vacío! Empiece escaneando algún código QR.");

                            dialogoOk.setButton(Dialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialogoOk.dismiss();
                                }
                            });

                            dialogoOk.show();
                        }
                    }
                });
    }

    /**
     * Método que muestra la toolbar personalizada
     * @param menu
     * @return boolean
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detalle_mueble, menu);
        return true;
    }

}