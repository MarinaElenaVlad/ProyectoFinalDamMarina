package com.example.proyectofinaldammarina;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.estanteria.Estanteria;
import com.example.proyectofinaldammarina.modelo.mueble.DAO.MuebleDAOImpl;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.example.proyectofinaldammarina.modelo.zona.ZonaExposicion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrearMuebleActivity extends AppCompatActivity {

    // Se declaran variables
    private EditText nombre, precio, medidas, descripcion;
    private ImageView imagenSubir;

    private Uri uriImagen;
    private Spinner spinnerEstanteria, spinnerZona;

    private Button botonGuardar;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    private String idMueble;

    private String idEstanteria;

    private String idZona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_mueble);

        storage = FirebaseStorage.getInstance();

        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();

        idMueble = bundle.getString("id");

        // Se asocian las variables con los elementos xml del layout asociado a este activity
        nombre = findViewById(R.id.nombreCrear);
        precio = findViewById(R.id.precioCrear);
        medidas = findViewById(R.id.medidasCrear);
        descripcion = findViewById(R.id.descripcionCrear);

        imagenSubir = findViewById(R.id.imagenSubir);
        botonGuardar = findViewById(R.id.botonGuardarMueble);

        spinnerEstanteria = findViewById(R.id.spinnerEstanteria);

        rellenarSpinnerEstanteria();

        spinnerZona = findViewById(R.id.spinnerZona);

        rellenarSpinnerZona();

        imagenSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se obtiene una imagen de la galeria
                activityResultLauncher.launch("image/*");
            }
        });

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatos();
            }
        });

    }

    ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null){
                        imagenSubir.setImageURI(result);
                        uriImagen = result;
                    }
                }
            });

    private void guardarDatos(){
        if(datosRellenados()){
            StorageReference reference = storage.getReference().child("images/" + UUID.randomUUID().toString());
            reference.putFile(uriImagen).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(CrearMuebleActivity.this, "id" + uri.toString(), Toast.LENGTH_SHORT).show();

                                /**
                                 * falta estanteria y zona!!
                                 */
                                Mueble mueble = new Mueble(idMueble,  uri.toString(), nombre.getText().toString().trim(),
                                        Double.parseDouble(precio.getText().toString().trim()), medidas.getText().toString().trim() ,descripcion.getText().toString(), idEstanteria, idZona);

                                /**
                                 * ver si sobreescribe!, ponerlo en dao
                                 */
                                MuebleDAOImpl muebleDAO = new MuebleDAOImpl(db, "muebles");
                                muebleDAO.insertarMueble(mueble, CrearMuebleActivity.this);
                            }
                        });

                    }else{
                        Toast.makeText(CrearMuebleActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            /**
             * usar alerta + cambiar todos campos si no es obligatorio spinners
             */
            Toast.makeText(CrearMuebleActivity.this, "Debe rellenar todos los campos", Toast.LENGTH_LONG).show();

        }


    }

    private boolean datosRellenados(){
        String nombreComprobar = nombre.getText().toString().trim();
        /**
         * comprobar porque es double
         */
        String precioComprobar = precio.getText().toString().trim();
        String medidasComprobar = medidas.getText().toString().trim();
        String descripcionComprobar = descripcion.getText().toString().trim();

        /**
         * AÑADIR SPINNER SI ALGUNO NO PUEDE SER NULL!!
         */

        if(nombreComprobar.isEmpty() || precioComprobar.isEmpty() || medidasComprobar.isEmpty()
                || descripcionComprobar.isEmpty() || uriImagen == null){
            return false;
        }else{
            return true;
        }
    }

    private void rellenarSpinnerEstanteria(){

        final List<Estanteria> list = new ArrayList<>();

        db.collection("estanterias")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Estanteria estanteria = document.toObject(Estanteria.class);

                                //Muestra documentos en los que existe el campo estanteria de MUEBLE!! con el valor con el que se compara
                                Query referenciasEstanterias = db.collection("muebles").whereEqualTo("estanteriaId", estanteria.getCodigo());
                                List<Mueble> mueblesConEstanteria = new ArrayList<>();

                                referenciasEstanterias.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            mueblesConEstanteria.clear();
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Mueble mueble = document.toObject(Mueble.class);
                                                //error:     java.lang.RuntimeException: Could not deserialize object. Can't convert object of type java.lang.String to type com.example.pruebaescanearqr.modelo.Estanteria (found in field 'estanteria')
                                                mueblesConEstanteria.add(mueble);
                                                System.out.println("Cod:" + mueble.toString());
                                                // list.add(estanteriaRef);
                                            }
                                            System.out.println(mueblesConEstanteria.size());
                                            if(mueblesConEstanteria.size() == 0){//La estanteria no esta asociada con ningun mueble
                                                list.add(estanteria);
                                            }
                                        }
                                    }
                                });
                            }
                            /**
                             * añadir opcion vacia a la lista (si codigo == "")
                             * IMPORTANTE, NO AÑADIR ESTANTERIAS CON CODIGO = "", SINO DEJARA DE SALIR
                             */
                            list.add(new Estanteria("Sin estanteria"));

                            //list tiene las estanterias que no están asociadas a ningun mueble
                            ArrayAdapter<Estanteria> arrayAdapter = new ArrayAdapter<>(CrearMuebleActivity.this, android.R.layout.simple_spinner_item, list);
                            spinnerEstanteria.setAdapter(arrayAdapter);
                            spinnerEstanteria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    //Se obtiene el código de la estantería seleccionada
                                    idEstanteria = parent.getItemAtPosition(position).toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    idEstanteria = "Sin estanteria";
                                }
                            });
                        } else {
                            Toast.makeText(CrearMuebleActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * zona, 1:n #?
     */

    /**
     * No hay que controlar que zona se repita, puede aparecer en varios muebles
     */
    private void rellenarSpinnerZona(){
        List<ZonaExposicion> listZona = new ArrayList<>();

        /**
         * Quitar lo de la imagen???????????????
         */
        db.collection("zonas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ZonaExposicion zona = document.toObject(ZonaExposicion.class);
                                listZona.add(zona);
                            }
                            listZona.add(new ZonaExposicion("Sin zona", "Ninguna", "Ninguna", ""));
                            ArrayAdapter<ZonaExposicion> arrayAdapter = new ArrayAdapter<>(CrearMuebleActivity.this, android.R.layout.simple_spinner_item, listZona);
                            spinnerZona.setAdapter(arrayAdapter);
                            spinnerZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    //Se obtiene el código de la estantería seleccionada (pone lo del toString)
                                    ZonaExposicion zona = (ZonaExposicion) parent.getItemAtPosition(position);
                                    idZona = zona.getCodigo();
                                    Toast.makeText(CrearMuebleActivity.this, idZona, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    idZona = "Sin zona";
                                }
                            });
                        }else {
                            Toast.makeText(CrearMuebleActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}