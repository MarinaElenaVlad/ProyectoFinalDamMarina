package com.example.proyectofinaldammarina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.proyectofinaldammarina.modelo.mueble.DAO.MuebleDAOImpl;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.example.proyectofinaldammarina.modelo.usuario.DAO.UsuarioDAOImpl;
import com.example.proyectofinaldammarina.modelo.usuario.Usuario;
import com.example.proyectofinaldammarina.modelo.zona.ZonaExposicion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class DetalleMuebleActivity extends AppCompatActivity {

    // Se declaran variables
    private EditText nombreMueble, precioMueble, medidasMueble, descripcionMueble;

    private TextView zonaMueble;
    private ImageView imagenMueble, imagenZona;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private Toolbar toolbar;

    private FloatingActionButton botonEliminar, botonModificar;

    private EditText editMueble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mueble);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        toolbar = findViewById(R.id.toolbarDetalleMueble);
        setSupportActionBar(toolbar);

        // Se asocian las variables a un elemento xml del layout correspondiente
        nombreMueble = findViewById(R.id.detalleNombreMueble);
        precioMueble = findViewById(R.id.detallePrecioMueble);
        medidasMueble = findViewById(R.id.detalleMedidasMueble);
        descripcionMueble = findViewById(R.id.detalleDescripcionMueble);
        zonaMueble = findViewById(R.id.detalleZonaMueble);

        imagenMueble = findViewById(R.id.detalleImagenMueble);
        imagenZona = findViewById(R.id.detalleImagenZona);

        botonEliminar = findViewById(R.id.eliminarMueble);
        botonModificar = findViewById(R.id.modificarMueble);

        //editMueble = findViewById(R.id.editMueble);

        // Se reciben los datos del fragment lista
        Bundle bundle = getIntent().getExtras();

        String codigoQr = bundle.getString("codigo");
        nombreMueble.setText(bundle.getString("nombre"));
        precioMueble.setText(bundle.getDouble("precio") + "€");
        medidasMueble.setText(bundle.getString("medidas"));
        descripcionMueble.setText(bundle.getString("descripcion"));

        /**
         * MODIFICAR TODOS LOS MUEBLES, UNICO POSIBLE VALOR = SIN ESTANTERIA O UNA
         */
        if(bundle.getString("zonaId").equals("Sin zona")){
            zonaMueble.setText("");
            /**
             * dialogo "error" = no zona, no a la venta (PROXIMAMENTE)
             */
        }else{
            db.collection("zonas").document(bundle.getString("zonaId")).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ZonaExposicion zonaExposicion = documentSnapshot.toObject(ZonaExposicion.class);
                            zonaMueble.setText(zonaExposicion.getCategoria() + ", planta " + zonaExposicion.getPlanta());
                        }
                    });
        }

        Picasso.get().load(bundle.getString("imagen")).into(imagenMueble);

        /**
         * zona + imagenes!!
         */


        //Borrar segun rol!!
        db.collection("usuarios").document(firebaseAuth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Usuario usuario = document.toObject(Usuario.class);
                                System.out.println(usuario.toString());
                                System.out.println("ACTIVITY" + usuario.getIdRol());
                                if (usuario.getIdRol().equals("empleado")) {
                                    Toast.makeText(DetalleMuebleActivity.this, "empleado", Toast.LENGTH_LONG).show();
                                    botonEliminar.setVisibility(View.VISIBLE);
                                    botonModificar.setVisibility(View.VISIBLE);
                                    //editMueble.setFocusableInTouchMode(true);
                                    nombreMueble.setFocusableInTouchMode(true);
                                    precioMueble.setFocusableInTouchMode(true);
                                    medidasMueble.setFocusableInTouchMode(true);
                                    descripcionMueble.setFocusableInTouchMode(true);
                                    zonaMueble.setFocusableInTouchMode(true);
                                } else {
                                    Toast.makeText(DetalleMuebleActivity.this, "cliente", Toast.LENGTH_LONG).show();
                                    botonEliminar.setVisibility(View.GONE);
                                    botonModificar.setVisibility(View.GONE);
                                    nombreMueble.setFocusable(false);
                                    precioMueble.setFocusable(false);
                                    medidasMueble.setFocusable(false);
                                    descripcionMueble.setFocusable(false);
                                    zonaMueble.setFocusable(false);

                                }
                            }
                        }
                    }
                });

        /**
         * AÑADIR DIALOGO SI ESTA SEGURO + CONFIRMACION
         */
        botonEliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog dialogo = new AlertDialog
                        .Builder(DetalleMuebleActivity.this)
                        .setPositiveButton("Sí, eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Hicieron click en el botón positivo, así que la acción está confirmada
                            db.collection("muebles").document(codigoQr)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(DetalleMuebleActivity.this, "Mueble borrado correctamente.", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(DetalleMuebleActivity.this, MenuActivity.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DetalleMuebleActivity.this, "Error al intentar borrar.", Toast.LENGTH_LONG).show();
                                }
                            });
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
                        .setMessage("¿Desea eliminar este mueble de forma definitiva?") // El mensaje
                        .create();
                dialogo.show();
            }
        });


        botonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialogo = new AlertDialog
                        .Builder(DetalleMuebleActivity.this)
                        .setPositiveButton("Sí, modificar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MuebleDAOImpl muebleDAO = new MuebleDAOImpl(db, "muebles");
                                Mueble mueble = new Mueble(codigoQr, "",
                                        nombreMueble.getText().toString(), Double.parseDouble(precioMueble.getText().toString()),
                                        medidasMueble.getText().toString(), descripcionMueble.getText().toString());
                                muebleDAO.actualizarMueble(codigoQr, mueble, DetalleMuebleActivity.this);
                                startActivity(new Intent(DetalleMuebleActivity.this, MenuActivity.class));
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detalle_mueble, menu);
        return true;
    }

}