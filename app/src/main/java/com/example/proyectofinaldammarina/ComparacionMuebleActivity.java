package com.example.proyectofinaldammarina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.mueble.DAO.MuebleDAOImpl;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.example.proyectofinaldammarina.modelo.usuario.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/**
 * QUE NO SE HAYA CREADO COMO KOTLIN!! LA CLASE
 */
public class ComparacionMuebleActivity extends AppCompatActivity {

    /**
     * se llama a esta clase desde dos partes, CUIDADO BUNDLE!! id y mueble
     */
    private Mueble mueble;

    private EditText nombre, medidas, precio, descripcion;
    private ImageView imagenMueble;

    private Button botonActualizar;

    private FirebaseFirestore db;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparacion_mueble);

        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        mueble = (Mueble) getIntent().getSerializableExtra("mueble");

        nombre = findViewById(R.id.nombreComparacionMueble);
        medidas = findViewById(R.id.medidasComparacionMueble);
        precio = findViewById(R.id.precioComparacionMueble);
        descripcion = findViewById(R.id.descripcionComparacionMueble);

        imagenMueble = findViewById(R.id.imageComparacionMueble);

        botonActualizar = findViewById(R.id.botonModificarComparacionMueble);

        cargarDatos();

        comprobarPermisos();

        /**
         * borrar del historial si no existe ese mueble (se ha borrado)
         */

        /**
         * no funcionaaaaaaaaaaaa POR EL SIMBOLO DEL EURO!!! CUIDADO, QUITAR SIMBOLO AL GUARDAR, PONER CONDICION DE NO BORRAR EL EURO??
         */
        botonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialogo = new AlertDialog
                        .Builder(ComparacionMuebleActivity.this)
                        .setPositiveButton("Sí, modificar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Mueble mueb = new Mueble(mueble.getCodigoQr(), mueble.getImagen(),
                                        nombre.getText().toString(), Double.parseDouble(precio.getText().toString()),
                                        medidas.getText().toString(), descripcion.getText().toString());
                                MuebleDAOImpl muebleDAO = new MuebleDAOImpl(db, "muebles");
                                muebleDAO.actualizarMueble(mueb.getCodigoQr(), mueb, ComparacionMuebleActivity.this);
                                startActivity(new Intent(ComparacionMuebleActivity.this, MenuActivity.class));
                                /**
                                 * cuidado con precio, dolar!! + ver si update cambia o sustituye tood!!
                                 * +codigo qr obligatorio al añadir + todos campos, QUITAR IMAGEN CONSTRUCTOR
                                 * VER SI PONE NULL O MATIENE + quitar signo euro!!!!
                                 * controlar tipo de dato edittext
                                 */
                            }

                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Hicieron click en el botón negativo, no confirmaron
                                // Simplemente se descarta el diálogo
                                dialog.dismiss();
                            }
                        })
                        .setTitle("Confirmar") // El título
                        .setMessage("¿Desea modificar los datos de este mueble?") // El mensaje
                        .create();
                dialogo.show();
            }
        });

    }

    private void cargarDatos(){

        nombre.setText(mueble.getNombre());
        medidas.setText(mueble.getMedidas());
        precio.setText(mueble.getPrecio() + "€");
        descripcion.setText(mueble.getDescripcion());
        /**
         * problema signo euro precio + imagen por defecto si no hay
         */

        /**
         * problema imagen no ocupa toda pantalla
         */

        Picasso.get().load(mueble.getImagen()).into(imagenMueble);

        /**
         * imagenes estanterias cambian segun stock
         */

        /**
         * modificar estanterias
         */
    }

    private void comprobarPermisos(){
        db.collection("usuarios").document(firebaseAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Usuario usuario = document.toObject(Usuario.class);
                        if (usuario.getIdRol().equals("empleado")) {
                            botonActualizar.setVisibility(View.VISIBLE);
                            nombre.setFocusableInTouchMode(true);
                            medidas.setFocusableInTouchMode(true);
                            precio.setFocusableInTouchMode(true);
                            descripcion.setFocusableInTouchMode(true);

                        } else { //cliente
                            botonActualizar.setVisibility(View.GONE);
                            nombre.setFocusable(false);
                            medidas.setFocusable(false);
                            precio.setFocusable(false);
                            descripcion.setFocusable(false);
                        }
                    }
                }
            }
        });
    }
}