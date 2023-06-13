package com.example.proyectofinaldammarina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.historial.Historial;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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
public class HistorialActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    // Se declaran las variables
    private ListView listaMostrar;
    private List<String> listaMuebles = new ArrayList<>();// Se inicializa para que no de error si el historial está vacío

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private Historial historial;

    private Toolbar toolbar;

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
                                // Se guardan los datos del historial (Tanto si la lista estaba vacía como si no)
                                listaMuebles = historial.getMuebleList();
                                // Se cambia el orden a la lista para que se muestren de más reciente a más antiguo
                                Collections.reverse(listaMuebles);
                            }
                        }else{
                            Toast.makeText(HistorialActivity.this, "Error getting documents:", Toast.LENGTH_LONG).show();
                        }

                        // Si la lista está llena se mostrará en la pantalla
                        if(listaMuebles.size() > 0) {
                            listaMostrar = findViewById(R.id.listaHistorial);
                            // Se usa el adaptador por defecto de android
                            ArrayAdapter<String> mueblesAdapter = new ArrayAdapter<String>(HistorialActivity.this,
                                    android.R.layout.simple_list_item_1, listaMuebles);
                            listaMostrar.setAdapter(mueblesAdapter);

                            // Se controla si se pulsa algún item del historial
                            listaMostrar.setOnItemClickListener(HistorialActivity.this);
                        }else{
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

        /**
         * CAMBIAR LO QUE SE MUESTRA EN EL HISTORIAL!!
         */
    }

    /**
     * Método que controla el evento que ocurre cuando se pulsa sobre un item de la lista del historial.
     * Si se pulsa se creará un intent de la clase ComparacionMuebleActivity o se le mostrará un mensaje
     * (Dependiendo si el item seleccionado sigue existiendo o no)
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idMueble = listaMuebles.get(position);

        db.collection("muebles").document(idMueble).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Mueble mueble = document.toObject(Mueble.class);
                    Intent intent = new Intent(HistorialActivity.this, ComparacionMuebleActivity.class);
                    intent.putExtra("mueble", mueble);
                    startActivity(intent);
                }else{
                    // Se usa un diálogo que informa al usuario que el mueble ya no existe
                    AlertDialog dialogoOk = new AlertDialog.Builder(HistorialActivity.this).create();
                    dialogoOk.setTitle("Aviso");
                    dialogoOk.setIcon(R.drawable.ic_baseline_info_24);
                    dialogoOk.setMessage("¡El mueble ya no exsite!.");

                    dialogoOk.setButton(Dialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialogoOk.dismiss();
                        }
                    });

                    //Se elimina de la lista el mueble que ya no existe!
                    /**
                     * PONER MENSAJE SI EL MUEBLE YA NO EXISTE!!
                     */

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