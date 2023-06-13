package com.example.proyectofinaldammarina;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyectofinaldammarina.modelo.historial.Historial;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.example.proyectofinaldammarina.modelo.usuario.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Fragment que se agrega dentro del activity del menú cuando se pulsa
 * la opción 'Escanear QR' del navegador inferior que está en la barra inferior
 */
public class EscanerFragment extends Fragment {

    // Se declaran las variables
    private CardView cardViewEscanear, cardViewHistorial;

    private String valorEscaneo = "";

    private FirebaseFirestore db;

    private FirebaseAuth firebaseAuth;

    private Historial historial;

    /**
     * Constructor vacío
     */
    public EscanerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_escaner, container, false);

        // Se inicializan variables
        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        cardViewEscanear = root.findViewById(R.id.cardEscaner);

        /**
         * Se controla el evento que se produce cuando se pulsa el card view Escanear
         */
        cardViewEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanear();
            }
        });

        cardViewHistorial = root.findViewById(R.id.cardHistorial);

        /**
         * Se controla el evento que se produce cuando se pulsa el card view Historial
         */
        cardViewHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se abre el historial
                startActivity(new Intent(getActivity(), HistorialActivity.class));
            }
        });

        return root;
    }

    /**
     * Método que abre el escaner de códigos qr
     */
    public void escanear(){
        ScanOptions scanOptions = new ScanOptions();
        // Propiedades escaner
        scanOptions.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        scanOptions.setPrompt("Escanea un código QR"); //título que aparece en el lector
        scanOptions.setCameraId(0); //camara trasera
        scanOptions.setBeepEnabled(false);//sonido al escanear
        scanOptions.setOrientationLocked(false);
        scanOptions.setCaptureActivity(CaptureActivityPortraint.class);
        scanOptions.setBarcodeImageEnabled(false);

        launcherEscaner.launch(scanOptions);
    }

    /**
     * Este método recoge el resultado que estaba asociado al código escaneado y a partir de
     * él se toman decisiones en el código
     */
    private final ActivityResultLauncher<ScanOptions> launcherEscaner = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() == null){
            Toast.makeText(getActivity(), "Escaneo cancelado", Toast.LENGTH_LONG).show();
        }else{
            valorEscaneo = result.getContents();
            // Se recorre la lista de muebles
            DocumentReference docRef = db.collection("muebles").document(valorEscaneo);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        //Si ese documento con ese id existe
                        if (document.exists()) {
                            Toast.makeText(getActivity(), "Búsqueda exitosa!", Toast.LENGTH_LONG).show();

                            // Se busca el historial del usuario
                            db.collection("historial").whereEqualTo("usuario", firebaseAuth.getUid()).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            historial = document.toObject(Historial.class);
                                        }
                                        // Se añade el mueble al historial
                                        actualizarHistorial(historial, valorEscaneo);
                                    }else{
                                        Toast.makeText(getActivity(), "Error getting documents:", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                            Mueble mueble = document.toObject(Mueble.class);
                            Intent intent = new Intent(getActivity(), ComparacionMuebleActivity.class);
                            intent.putExtra("mueble", mueble);
                            startActivity(intent);


                        } else {//El documento no existe!
                            //si el usuario es un cliente se le mostrará un mensaje de error
                            noEncontrado(firebaseAuth.getUid());
                            //si el usuario es un empleado se le mostrará un mensaje de si o no
                        }
                    } else {
                        Toast.makeText(getActivity(), "get failed with " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    });

    /**
     * Método que se ejecutará si el código escaneado no pertenece a ningún mueble de la base de datos
     * @param idUsuario
     */
    private void noEncontrado(String idUsuario){

        db.collection("usuarios").document(idUsuario).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Usuario usuario = document.toObject(Usuario.class);
                                // Determinamos cómo actuará el programa según el rol del usuario
                                if (usuario.getIdRol().equals("empleado")) {
                                    AlertDialog.Builder builder;
                                    AlertDialog alerta;

                                    // Dialogo personalizado (layout 'dialogo_pregunta')
                                    View layoutView = getLayoutInflater().inflate(R.layout.dialogo_pregunta, null);
                                    AppCompatButton botonOkDialogo = layoutView.findViewById(R.id.botonSiDuda);
                                    AppCompatButton botonCancelarDialogo = layoutView.findViewById(R.id.botonNoDuda);

                                    builder = new AlertDialog.Builder(getActivity());
                                    builder.setView(layoutView);
                                    alerta = builder.create();

                                    // Se controla de que no se pueda cerrar el diálogo a menos
                                    // que se pulse el botón cancelar
                                    alerta.setCanceledOnTouchOutside(false);
                                    alerta.setCancelable(false); //flecha hacía atrás

                                    alerta.show();

                                    // Se controla el evento que sucede al pulsar el botón ok
                                    // del diálogo
                                    botonOkDialogo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Se va al intent donde se podrá crear un nuevo mueble
                                            Intent intent = new Intent(getActivity(), CrearMuebleActivity.class);
                                            intent.putExtra("id", valorEscaneo);
                                            startActivity(intent);
                                            alerta.dismiss();//se quita el dialogo
                                        }
                                    });
                                    // Se controla el evento que sucede al pulsar el botón cancelar
                                    // del diálogo
                                    botonCancelarDialogo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //se quita el dialogo
                                            alerta.dismiss();
                                        }
                                    });

                                } else { //cliente
                                    AlertDialog.Builder builder;
                                    AlertDialog alerta;

                                    // Dialogo personalizado (layout 'dialogo_error')
                                    View layoutView = getLayoutInflater().inflate(R.layout.dialogo_error, null);
                                    AppCompatButton botonDialogo = layoutView.findViewById(R.id.botonOkError);

                                    builder = new AlertDialog.Builder(getActivity());
                                    builder.setView(layoutView);
                                    alerta = builder.create();
                                    alerta.show();

                                    // Se controla el evento que sucede cuando se pulsa el botón ok
                                    // del diálogo
                                    botonDialogo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alerta.dismiss();
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
    }

    /**
     * Método que añade el mueble escaneado al historial correspondiente a ese usuario
     * @param his
     * @param muebleConsultado
     */
    private void actualizarHistorial(Historial his, String muebleConsultado){

        // Comprobamos si el mueble ya existe en el historial (Ya hay una o varias referencias a él)
        if(his.getMuebleList().contains(muebleConsultado)) {
            // Antes de insertar la nueva referencia borramos la referencia existente del historial (Para que no se repitan referencias)
            his.getMuebleList().removeAll(Collections.singleton(muebleConsultado));
        }
        // Se añade el id del mueble buscado a la lista
        his.getMuebleList().add(muebleConsultado);
        db.collection("historial").document(his.getIdentificador())
                .update("muebleList", his.getMuebleList()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "DocumentSnapshot successfully updated!!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error updating document!!", Toast.LENGTH_LONG).show();
                    }
                });
        }
    }

