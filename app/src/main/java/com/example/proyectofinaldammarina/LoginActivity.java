package com.example.proyectofinaldammarina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.usuario.DAO.UsuarioDAOImpl;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Clase ejecutable que controla los eventos de la primera pantalla (XML) de la aplicacion (Login).
 */
public class LoginActivity extends AppCompatActivity {

    // Se declaran variables
    private EditText campoEmail, campoPassword;
    private Button botonLogin;
    private TextView textoRegistrar, textoResetearPassword;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Se inicializan variables
        firebaseAuth = FirebaseAuth.getInstance();

        // Se asocian las variables con los elementos xml del layout asociado a este activity
        campoEmail = findViewById(R.id.login_email);
        campoPassword = findViewById(R.id.login_password);
        botonLogin = findViewById(R.id.boton_login);
        textoRegistrar = findViewById(R.id.loginRedireccionar);
        textoResetearPassword = findViewById(R.id.loginResetearPassword);


        /**
         * Se controla el evento que ocurre cuando se pulsa el botón de login
         */
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString().trim();
                String password = campoPassword.getText().toString().trim();

                // Se comprueba que no se manden cadenas vacías
                boolean emailCorrecto = comprobarEmail(email, campoEmail);
                boolean passwordCorrecta = comprobarPassword(password, campoPassword);

                if(emailCorrecto && passwordCorrecta) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            //Se accede al menú de la aplicación
                            Toast.makeText(LoginActivity.this, "Login exitoso", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
//                            intent.putExtra("uid", FirebaseAuth.getInstance().getUid()); //mandamos llave bd firestore
                            startActivity(intent);
                            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Login fallido: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });


        /**
         * Se controla el evento que ocurre cuando se pulsa el text view
         * con la pregunta de si se ha olvidado la contraseña.
         * Se reseteará la contraseña del usuario.
         */
        textoResetearPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString().trim();
                AlertDialog dialogo = new AlertDialog
                        .Builder(LoginActivity.this).setPositiveButton("Sí, resetear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!email.isEmpty()) {
                            firebaseAuth.sendPasswordResetEmail(email)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(LoginActivity.this, "Email enviado.", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginActivity.this, "Reseteo fallido: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }else{
                            campoEmail.setError("Introduzca el email para poder resetear la contraseña.");
                        }
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Reseteo cancelado.", Toast.LENGTH_LONG).show();
                    }
                }).setTitle("Confirmar") // El título
                        .setMessage("¿Quiere resetear su contraseña?") // El mensaje
                        .create();
                dialogo.show();
            }
        });


        /**
         * Se controla el evento que ocurre cuando se pulsa el text view
         * en el que pone '¿No tiene una cuenta? ¡Regístrese!'
         */
        textoRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se abre la pantalla de registrar
                startActivity(new Intent(LoginActivity.this, RegistrarActivity.class));
            }
        });

    }

    /**
     * Método para comprobar si el email es una cadena vacía, si lo es
     * se mostrará un error al lado del campo del email.
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