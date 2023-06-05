package com.example.proyectofinaldammarina;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class EscanerFragment extends Fragment {

    private CardView cardViewEscanear, cardViewHistorial;


    public EscanerFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_escaner, container, false);

        cardViewEscanear = root.findViewById(R.id.cardEscaner);

        cardViewEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
                intentIntegrator.setOrientationLocked(false);//MEJORAR
                intentIntegrator.setPrompt("Scan a QR code"); //titulo que aparece en el lector
                intentIntegrator.setCameraId(0); //camara trasera
                intentIntegrator.setBeepEnabled(true); //sonido al escanear
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);//tipo de código que puede leer
                intentIntegrator.initiateScan();//iniciar escaneo
            }
        });

        return root;
    }
//
//    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        // There are no request codes
//                        Intent data = result.getData();
//                        doSomeOperations();
//                    }
//                }
//            });



}