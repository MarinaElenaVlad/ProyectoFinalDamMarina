package com.example.proyectofinaldammarina;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.mueble.DAO.MuebleDAOImpl;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.example.proyectofinaldammarina.modelo.usuario.DAO.UsuarioDAOImpl;
import com.example.proyectofinaldammarina.modelo.usuario.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * QUE PUEDAN CAMBIAR NOMBRE TAMBIEN!!
 */
public class PerfilFragment extends Fragment {

    private FirebaseFirestore db;

    private FirebaseAuth firebaseAuth;

    private ImageView imagenUsuario, imagenSegunUsuario;

    private TextView nombre, correo;

    private FirebaseStorage storage;

    private Uri uriImagen;

    private Usuario usuario;

    private Button botonActualizar;

    public PerfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

/**
 * PUEDO PONER ROL TAMBIEN + FOTO RELACIONADA, CORREO , NOMBRE, FOTO PERFIL + hola empleado/cliente (bienvenido a tienda/ trabajo/ informar funciones q tiene)
 */
        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();

        imagenUsuario = root.findViewById(R.id.imagenPerfil);

        imagenSegunUsuario = root.findViewById(R.id.imagenSegunRol);

        nombre = root.findViewById(R.id.nombrePerfil);
        correo = root.findViewById(R.id.correoPerfil);

        botonActualizar = root.findViewById(R.id.botonActualizarPerfil);

        botonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Este método la guarda en el storage y la asocia con su documento en la base de datos
                actualizarPerfil();
            }
        });

        db.collection("usuarios").document(firebaseAuth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    usuario = document.toObject(Usuario.class);

                    /**
                     * da error porque este usuario se creo sin el campo imagen!!
                     * asegurarse todos los tienen!!
                     * cambiar usuarios o sus campos!!
                     */
                    if(!usuario.getImagenUsuario().equals("")) {
                        Picasso.get().load(usuario.getImagenUsuario()).into(imagenUsuario);
                    }

                    if (usuario.getIdRol().equals("empleado")){
                        imagenSegunUsuario.setImageResource(R.drawable.imagenempleados);
                    }else{
                        imagenSegunUsuario.setImageResource(R.drawable.imagenclientes);
                    }
                    nombre.setText(usuario.getNombre());
                    correo.setText(usuario.getEmail());
                }
            }
        });

        return root;

    }

    ImageView imagenActualizar;

    /***
     * controlar no insertar dos veces misma foto?? deselecciona y vuelve a seleccionar la misma??
     */
    private void actualizarPerfil(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.actualizar_perfil);

        AppCompatButton botonGuardar;
        EditText nombrePerfil;

        nombrePerfil = dialog.findViewById(R.id.nombreActualizarPerfil);
        botonGuardar = dialog.findViewById(R.id.botonModificarPerfil);
        imagenActualizar = dialog.findViewById(R.id.imagenActualizarPerfil);

        nombrePerfil.setText(nombre.getText().toString().trim());

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uriImagen != null && !nombrePerfil.getText().toString().trim().isEmpty()){
                    StorageReference reference = storage.getReference().child("perfiles/" + UUID.randomUUID().toString());
                    reference.putFile(uriImagen).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl(db, "usuarios");
                                        usuarioDAO.actualizarFotoPerfil(firebaseAuth.getUid(), uri.toString(), getActivity());
                                        usuarioDAO.actualizarNombrePerfil(firebaseAuth.getUid(), nombrePerfil.getText().toString().trim(), getActivity());
                                    }
                                });

                            }else{
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(getContext(), "Seleccione una nueva foto y/o escriba un nombre!", Toast.LENGTH_LONG).show();
                }

            }
        });

        imagenActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se obtiene una imagen de la galeria
                activityResultLauncher.launch("image/*");

            }
        });

        /**
         * AL BORRAR FOTO QUE SE BORRE REFERENCIA (STORAGE)
         */

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.animacionDialogo;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    ActivityResultLauncher activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null){
                        imagenActualizar.setImageURI(result);
                        uriImagen = result;
                    }
                }
            });

}