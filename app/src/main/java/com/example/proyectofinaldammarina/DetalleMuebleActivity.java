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

/**
 * Clase que controla los eventos del layout (XML) que se muestran cada vez que se pulsa
 * un item del recycler view del fragment de la lista.
 */
public class DetalleMuebleActivity extends AppCompatActivity {

    // Se declaran las variables
    private TextView nombreMueble, precioMueble, medidasMueble, descripcionMueble,  zonaMueble;
    private ImageView imagenMueble;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private Toolbar toolbar;

    private FloatingActionButton botonEliminar;
    private AppCompatButton botonModificar;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_mueble);
        // Se inicializan variables
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.toolbarDetalleMueble);
        // Se carga la toolbar
        setSupportActionBar(toolbar);

        // Se asocian las variables a su elemento correspondiente del layout
        nombreMueble = findViewById(R.id.detalleNombreMueble);
        precioMueble = findViewById(R.id.detallePrecioMueble);
        medidasMueble = findViewById(R.id.detalleMedidasMueble);
        descripcionMueble = findViewById(R.id.detalleDescripcionMueble);
        zonaMueble = findViewById(R.id.detalleZonaMueble);

        imagenMueble = findViewById(R.id.detalleImagenMueble);

        botonEliminar = findViewById(R.id.eliminarMueble);
        botonModificar = findViewById(R.id.modificarMueble);

        // Se reciben los datos del fragment lista y se muestran en el layout
        bundle = getIntent().getExtras();

        String codigoQr = bundle.getString("codigo");
        nombreMueble.setText(bundle.getString("nombre"));
        precioMueble.setText(bundle.getDouble("precio") + "€");
        medidasMueble.setText(bundle.getString("medidas"));
        descripcionMueble.setText(bundle.getString("descripcion"));

        if(bundle.getString("zonaId").equals("Sin zona")){
            zonaMueble.setText("");
            // Se usa un diálogo que informa al usuario
            AlertDialog dialogoOk = new AlertDialog.Builder(DetalleMuebleActivity.this).create();
            dialogoOk.setTitle("Aviso sobre la zona");
            dialogoOk.setIcon(R.drawable.ic_baseline_info_24);
            dialogoOk.setMessage("¡El mueble aún no se encuentra expuesto en la tienda!");

            dialogoOk.setButton(Dialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialogoOk.dismiss();
                }
            });

            dialogoOk.show();
        }else{
            // Se obtiene la zona de la base de datos
            db.collection("zonas").document(bundle.getString("zonaId")).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ZonaExposicion zonaExposicion = documentSnapshot.toObject(ZonaExposicion.class);
                            zonaMueble.setText(zonaExposicion.getCategoria() + ", planta " + zonaExposicion.getPlanta());
                        }
                    });
        }

        // Se carga la imagen de la base de datos
        Picasso.get().load(bundle.getString("imagen")).into(imagenMueble);

        /**
         * Se dan o se quita permisos según el rol que el usuario tenga
         */
        db.collection("usuarios").document(firebaseAuth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Usuario usuario = document.toObject(Usuario.class);
                                // Se quitan o se mantienen permisos sobre pulsar los botones
                                // de eliminar y modificar
                                if (usuario.getIdRol().equals("empleado")) {
                                    botonEliminar.setVisibility(View.VISIBLE);
                                    botonModificar.setVisibility(View.VISIBLE);
                                } else {
                                    botonEliminar.setVisibility(View.GONE);
                                    botonModificar.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                });

        /**
         * Se controlar el evento que sucede al pulsar el botón eliminar
         */
        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se usa un diálogo de sí o no
                AlertDialog dialogo = new AlertDialog
                        .Builder(DetalleMuebleActivity.this)
                        .setPositiveButton("Sí, eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            // Cuando se pulsa el botón positivo la acción se confirma (Se borra ese documento)
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
                                // La acción se cancela y
                                // simplemente se descarta el diálogo
                                dialog.dismiss();
                            }
                        })
                        .setTitle("Confirmar") // El título
                        .setMessage("¿Desea eliminar este mueble de forma definitiva?") // El mensaje
                        .create();
                dialogo.show();
            }
        });


        /**
         * Se controla el evento que sucede al pulsar el botón de modificar datos
         */
        botonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // Se llama a la siguiente función
                mostrarDialog();
            }
        });
    }

    /**
     * Método que muestra un diálogo en el que se carga el contenido del layout
     * 'actualizar_mueble'. Desde aquí el usuario podrá modificar algunos
     * datos del mueble
     */
    private void mostrarDialog() {
        final Dialog dialog = new Dialog(DetalleMuebleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // El contenido del diálogo será el layout 'actualizar_mueble'
        dialog.setContentView(R.layout.actualizar_mueble);

        // Se declaran las variables propias del layout 'actualizar_mueble'
        TextView nombreActualizar, medidasActualizar, precioActualizar, descripcionActualizar;
        Button guardarCambios;

        // Se asocian las variables a los elementos xml del layout 'actualizar_mueble'
        nombreActualizar = dialog.findViewById(R.id.nombreActualizarMueble);
        medidasActualizar = dialog.findViewById(R.id.medidasActualizarMueble);
        precioActualizar = dialog.findViewById(R.id.precioActualizarMueble);
        descripcionActualizar = dialog.findViewById(R.id.descripcionActualizarMueble);
        guardarCambios = dialog.findViewById(R.id.botonModificarMueble);

        // Se cargan los datos del mueble en los campos
        nombreActualizar.setText(bundle.getString("nombre"));
        medidasActualizar.setText(bundle.getString("medidas"));
        precioActualizar.setText(bundle.getDouble("precio") + "");
        descripcionActualizar.setText(bundle.getString("descripcion"));

        /**
         * Se controla el evento que ocurre al pulsar el botón guardar cambios
         * del layout 'actualizar_mueble'
         */
        guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se comprueban que los campos no estén vacíos
                if(!estaVacio(nombreActualizar.getText().toString().trim(), medidasActualizar.getText().toString().trim(),
                        precioActualizar.getText().toString().trim(), descripcionActualizar.getText().toString().trim())){
                    // Se muestra un diálogo de si o no
                    AlertDialog dialogo = new AlertDialog
                            .Builder(DetalleMuebleActivity.this)
                            .setPositiveButton("Sí, modificar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Se crea un objecto de tipo mueble con los datos que no han cambiado (codigo, imagen, zona y estanteria)
                                    // y se recogen los datos que sí que se pueden cambiar desde el layout 'actualizar_mueble'
                                    Mueble mueb = new Mueble(bundle.getString("codigo"), bundle.getString("imagen"),
                                            nombreActualizar.getText().toString().trim(), Double.parseDouble(precioActualizar.getText().toString().trim()),
                                            medidasActualizar.getText().toString().trim(), descripcionActualizar.getText().toString().trim());
                                    try {
                                        MuebleDAOImpl muebleDAO = new MuebleDAOImpl(db, "muebles");
                                        //Actualizamos los campos modificados
                                        muebleDAO.actualizarMueble(mueb, DetalleMuebleActivity.this);
                                    }catch (Exception e){
                                        Toast.makeText(DetalleMuebleActivity.this, "Error al actualizar los campos.", Toast.LENGTH_LONG).show();
                                    }

                                    dialog.dismiss();
                                    startActivity(new Intent(DetalleMuebleActivity.this, MenuActivity.class));
                                }

                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // La acción se cancela y
                                    // simplemente se descarta el diálogo
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
        // Configuramos la animación del diálogo cuando se muestra y cuando desaparece de la pantalla
        dialog.getWindow().getAttributes().windowAnimations = R.style.animacionDialogo;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * Método que comprueba si los campos modificados están vacíos, si alguno
     * de ellos lo está devolverá true.
     * @param nombre
     * @param medidas
     * @param precio
     * @param descripcion
     * @return boolean
     */
    private boolean estaVacio(String nombre, String medidas, String precio, String descripcion){
        if(nombre.isEmpty() || medidas.isEmpty() || precio.isEmpty() || descripcion.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Método que muestra la toolbar personalizada
     * @param menu
     * @return boolean
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detalle_mueble, menu);
        return true;
    }

}