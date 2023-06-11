package com.example.proyectofinaldammarina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
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
    private TextView nombreMueble, precioMueble, medidasMueble, descripcionMueble,  zonaMueble;
    private ImageView imagenMueble, imagenZona;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private Toolbar toolbar;

    private FloatingActionButton botonEliminar;
    private AppCompatButton botonModificar;

    private EditText editMueble;

    private Bundle bundle;

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
        bundle = getIntent().getExtras();

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
//                                    nombreMueble.setFocusableInTouchMode(true);
//                                    precioMueble.setFocusableInTouchMode(true);
//                                    medidasMueble.setFocusableInTouchMode(true);
//                                    descripcionMueble.setFocusableInTouchMode(true);
//                                    zonaMueble.setFocusableInTouchMode(true);
                                } else {
                                    Toast.makeText(DetalleMuebleActivity.this, "cliente", Toast.LENGTH_LONG).show();
                                    botonEliminar.setVisibility(View.GONE);
                                    botonModificar.setVisibility(View.GONE);
//                                    nombreMueble.setFocusable(false);
//                                    precioMueble.setFocusable(false);
//                                    medidasMueble.setFocusable(false);
//                                    descripcionMueble.setFocusable(false);
//                                    zonaMueble.setFocusable(false);

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
                 showDialog();
//                AlertDialog dialogo = new AlertDialog
//                        .Builder(DetalleMuebleActivity.this)
//                        .setPositiveButton("Sí, modificar", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                MuebleDAOImpl muebleDAO = new MuebleDAOImpl(db, "muebles");
//                                Mueble mueble = new Mueble(codigoQr, "",
//                                        nombreMueble.getText().toString(), Double.parseDouble(precioMueble.getText().toString()),
//                                        medidasMueble.getText().toString(), descripcionMueble.getText().toString());
//                                muebleDAO.actualizarMueble(codigoQr, mueble, DetalleMuebleActivity.this);
//                                startActivity(new Intent(DetalleMuebleActivity.this, MenuActivity.class));
//                                /**
//                                 * cuidado con precio, dolar!! + ver si update cambia o sustituye tood!!
//                                 * +codigo qr obligatorio al añadir + todos campos, QUITAR IMAGEN CONSTRUCTOR
//                                 * VER SI PONE NULL O MATIENE + quitar signo euro!!!!
//                                 * controlar tipo de dato edittext
//                                 */
//                            }
//
//
//                        })
//                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // Hicieron click en el botón negativo, no confirmaron
//                                // Simplemente se descarta el diálogo
//                                dialog.dismiss();
//                            }
//                        })
//                        .setTitle("Confirmar") // El título
//                        .setMessage("¿Desea modificar los datos de este mueble?") // El mensaje
//                        .create();
//                dialogo.show();
            }
        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(DetalleMuebleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.actualizar_mueble);

        TextView nombreActualizar, medidasActualizar, precioActualizar, descripcionActualizar;
        Button guardarCambios;

        nombreActualizar = dialog.findViewById(R.id.nombreActualizarMueble);
        medidasActualizar = dialog.findViewById(R.id.medidasActualizarMueble);
        precioActualizar = dialog.findViewById(R.id.precioActualizarMueble);
        descripcionActualizar = dialog.findViewById(R.id.descripcionActualizarMueble);
        guardarCambios = dialog.findViewById(R.id.botonModificarMueble);

        nombreActualizar.setText(bundle.getString("nombre"));
        medidasActualizar.setText(bundle.getString("medidas"));
        precioActualizar.setText(bundle.getDouble("precio") + "");
        descripcionActualizar.setText(bundle.getString("descripcion"));

        guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!estaVacio(nombreActualizar.getText().toString().trim(), medidasActualizar.getText().toString().trim(),
                        precioActualizar.getText().toString().trim(), descripcionActualizar.getText().toString().trim())){
                    AlertDialog dialogo = new AlertDialog
                            .Builder(DetalleMuebleActivity.this)
                            .setPositiveButton("Sí, modificar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Mueble mueb = new Mueble(bundle.getString("codigo"), bundle.getString("imagen"),
                                            nombreActualizar.getText().toString().trim(), Double.parseDouble(precioActualizar.getText().toString().trim()),
                                            medidasActualizar.getText().toString().trim(), descripcionActualizar.getText().toString().trim());
                                    MuebleDAOImpl muebleDAO = new MuebleDAOImpl(db, "muebles");
                                    muebleDAO.actualizarMueble(mueb.getCodigoQr(), mueb, DetalleMuebleActivity.this);
                                    dialog.dismiss();
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

                }else{
                    Toast.makeText(DetalleMuebleActivity.this, "Los campos no pueden estar vacios", Toast.LENGTH_LONG).show();
                }

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.animacionDialogo;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private boolean estaVacio(String nombre, String medidas, String precio, String descripcion){
        if(nombre.isEmpty() || medidas.isEmpty() || precio.isEmpty() || descripcion.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detalle_mueble, menu);
        return true;
    }

}