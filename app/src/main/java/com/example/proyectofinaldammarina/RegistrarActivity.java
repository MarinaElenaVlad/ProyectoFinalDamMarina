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

import com.example.proyectofinaldammarina.modelo.historial.DAO.HistorialDAOImpl;
import com.example.proyectofinaldammarina.modelo.historial.Historial;
import com.example.proyectofinaldammarina.modelo.usuario.DAO.UsuarioDAOImpl;
import com.example.proyectofinaldammarina.modelo.usuario.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

/**
 * Clase que controla los eventos de la pantalla del registro (XML) de un usuario.
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
        // Se inicializan variables
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

                // Se comprueba que no se manden cadenas vacías
                boolean nombreCorrecto = comprobarNombre(nombre, campoNombre);
                boolean emailCorrecto = comprobarEmail(email, campoEmail);
                boolean passwordCorrecta = comprobarPassword(password, campoPassword);

                // Para que se pueda crear un nuevo usuario todos los campos deben ser correctos (Que no sean cadenas vacías)
                if(nombreCorrecto && emailCorrecto && passwordCorrecta){

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            try {
                                // Si el nuevo usuario se crea con éxito, se creará un usuario en la base de datos de Firestore
                                UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl(firebaseFirestore, "usuarios");
                                // Por defecto todos los usuarios tendrá el rol "cliente" y ninguna imagen asociada
                                usuarioDAO.insertarUsuario(new Usuario(nombre, email,"cliente", ""), FirebaseAuth.getInstance().getUid());

                                // También se crea un historial que está asoaciado a ese usuario
                                HistorialDAOImpl historialDAO = new HistorialDAOImpl(firebaseFirestore, "historial");
                                historialDAO.crearHistorial(firebaseAuth.getUid());

                                Toast.makeText(RegistrarActivity.this, "¡Cuenta creada con éxito!", Toast.LENGTH_LONG).show();
                                // Si la cuenta se crea con éxito se volverá a la pantalla de login
                                startActivity(new Intent(RegistrarActivity.this, LoginActivity.class));

                            }catch (Exception e){
                                // Mensaje de error si la creación falla.
                                Toast.makeText(RegistrarActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrarActivity.this, "Registro fallido: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * Método para comprobar si el nombre es una cadena vacía, si lo es
     * se mostrará un error en el campo del nombre.
     * @param nombre
     * @param campoNombre
     * @return boolean
     */
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
     * Método para comprobar si el email es una cadena vacía, si lo es
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