package com.example.proyectofinaldammarina;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.example.proyectofinaldammarina.modelo.usuario.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class EscanerFragment extends Fragment {

    private CardView cardViewEscanear, cardViewHistorial;

    private String valorEscaneo = "";

    private FirebaseFirestore db;

    private FirebaseAuth firebaseAuth;


    public EscanerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_escaner, container, false);

        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();


        cardViewEscanear = root.findViewById(R.id.cardEscaner);

        cardViewEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escanear();
            }
        });

        return root;
    }

//    video : https://youtu.be/5WzKeY6uVX4
    public void escanear(){
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        scanOptions.setPrompt("Escanea un código QR"); //titulo que aparece en el lector
        scanOptions.setCameraId(0); //camara trasera
        scanOptions.setBeepEnabled(false);//sonido al escanear
        scanOptions.setOrientationLocked(false);
        scanOptions.setCaptureActivity(CaptureActivityPortraint.class);
        scanOptions.setBarcodeImageEnabled(false);

        launcherEscaner.launch(scanOptions);
    }

    private final ActivityResultLauncher<ScanOptions> launcherEscaner = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() == null){
            Toast.makeText(getActivity(), "Escaneo cancelado", Toast.LENGTH_LONG).show();
        }else{
            valorEscaneo = result.getContents();
            DocumentReference docRef = db.collection("muebles").document(valorEscaneo);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        //Si ese documento con ese id existe
                        if (document.exists()) {
                            Toast.makeText(getActivity(), "Búsqueda exitosa!", Toast.LENGTH_LONG).show();

                            /**
                             * AÑADIR AL HISTORIAL!!
                             */

                            Mueble mueble = document.toObject(Mueble.class);
                            Intent intent = new Intent(getActivity(), ComparacionMuebleActivity.class);
                            //intent.putExtra("id", valorEscaneo);
                            intent.putExtra("mueble", mueble);
                            startActivity(intent);

                        } else {//El documento no existe!
                            //si el usuario es un cliente se le muestra un mensaje de error
                            noEncontrado(firebaseAuth.getUid());
                            //si el usuario es un empleado se le muestra un mensaje de si o no

//                                Intent intent = new Intent(MainActivity.this, NoEncontrar.class);
//                                intent.putExtra("id", resultadoEscaneo);
//                                startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getActivity(), "get failed with " + task.getException(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    });

    private void noEncontrado(String idUsuario){


        db.collection("usuarios").document(idUsuario).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Usuario usuario = document.toObject(Usuario.class);
                                System.out.println(usuario.toString());
                                System.out.println("ACTIVITY" + usuario.getIdRol());
                                if (usuario.getIdRol().equals("empleado")) {
                                    AlertDialog.Builder builder;
                                    AlertDialog alerta;

                                    View layoutView = getLayoutInflater().inflate(R.layout.dialogo_pregunta, null);
                                    AppCompatButton botonOkDialogo = layoutView.findViewById(R.id.botonOkDuda);
                                    AppCompatButton botonCancelarDialogo = layoutView.findViewById(R.id.botonCancelarDuda);

                                    builder = new AlertDialog.Builder(getActivity());
                                    builder.setView(layoutView);
                                    alerta = builder.create();
                                    /**
                                     * controlar que no pueda cerrar el dialogo
                                     */
                                    alerta.setCanceledOnTouchOutside(false);
                                    alerta.setCancelable(false); //flecha hacía atrás

                                    /**
                                     * usar mismos dialogos en otros errores
                                     */
                                    alerta.show();

                                    botonOkDialogo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            /**
                                             * No puedes añadir "" como id, numeros -1? aunque sea string?
                                             */
                                            startActivity(new Intent(getActivity(), CrearMuebleActivity.class));
                                            alerta.dismiss();//se quita el dialogo si decide volver atras
                                        }
                                    });

                                    botonCancelarDialogo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alerta.dismiss();
                                        }
                                    });

                                } else { //cliente
                                    AlertDialog.Builder builder;
                                    AlertDialog alerta;

                                    View layoutView = getLayoutInflater().inflate(R.layout.dialogo_error, null);
                                    AppCompatButton botonDialogo = layoutView.findViewById(R.id.botonOkError);

                                    builder = new AlertDialog.Builder(getActivity());
                                    builder.setView(layoutView);
                                    alerta = builder.create();
                                    alerta.show();

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

}