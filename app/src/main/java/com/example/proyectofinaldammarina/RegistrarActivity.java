package com.example.proyectofinaldammarina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.usuario.DAO.UsuarioDAOImpl;
import com.example.proyectofinaldammarina.modelo.usuario.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * AÑADIR EXPLICACION + COMENTARIOS
 */
public class RegistrarActivity extends AppCompatActivity {

    // Se declaran variables
    private EditText campoNombre, campoEmail, campoPassword;
    private Button botonRegistrar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Se asocian las variables con los elementos xml del layout asociado a este activity
        campoNombre = findViewById(R.id.registrar_nombre);
        campoEmail = findViewById(R.id.registrar_email);
        campoPassword = findViewById(R.id.registrar_password);
        botonRegistrar = findViewById(R.id.boton_registrar);

        /**
         * Se controla el evento que ocurre cuando se pulsa el botón de registrar
         */
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = campoNombre.getText().toString().trim();
                String email = campoEmail.getText().toString().trim();
                String password = campoPassword.getText().toString().trim();

                boolean nombreCorrecto = comprobarNombre(nombre, campoNombre);
                boolean emailCorrecto = comprobarEmail(email, campoEmail);
                boolean passwordCorrecta = comprobarPassword(password, campoPassword);

                if(nombreCorrecto && emailCorrecto && passwordCorrecta){

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //Enviar rol + usuario, decidir que se muestra y que no
                            //uid usuario
                            try {
                                UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl(firebaseFirestore, "usuarios");
                                usuarioDAO.insertarUsuario(new Usuario(nombre, email,"cliente"), FirebaseAuth.getInstance().getUid());
                                Toast.makeText(RegistrarActivity.this, "¡Cuenta creada con éxito!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));

                            }catch (Exception e){
                                //Mensaje de errorr
                                Toast.makeText(RegistrarActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrarActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            //poner pausa, que no salgan mensjaes encima de otros si se pulsa el boton varias veces
                        }
                    });
                }
            }
        });
    }

    private boolean comprobarNombre(String nombre, EditText campoNombre){
        boolean esCorrecto;
        if(nombre.isEmpty()){
            campoNombre.setError("Introduzca un nombre");
            esCorrecto = false;
        }else{
            esCorrecto = true;
        }

        return esCorrecto;
    }

    /**
     * Método para comprobar el email es una cadena vacía, si lo es
     * se mostrará un error en el campo del email.
     * @param email
     * @param campoEmail
     * @return boolean
     */
    private boolean comprobarEmail(String email, EditText campoEmail){
        boolean esCorrecto;
        if(email.isEmpty()){
            campoEmail.setError("Introduzca un email");
            esCorrecto = false;
        }else{
            esCorrecto = true;
        }

        return esCorrecto;
    }

    /**
     * Método para comprobar si la contraseña es una cadena vacía, si lo es
     * se mostrará un error en el campo de la contraseña.
     * @param password
     * @param campoPassword
     * @return boolean
     */
    private boolean comprobarPassword(String password, EditText campoPassword){
        boolean esCorrecto;
        if(password.isEmpty()){
            campoPassword.setError("Introduzca su contraseña");
            esCorrecto = false;
        }else{
            esCorrecto = true;
        }
        return esCorrecto;
    }
}