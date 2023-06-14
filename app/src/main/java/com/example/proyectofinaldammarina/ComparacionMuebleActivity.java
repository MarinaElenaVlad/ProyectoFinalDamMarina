package com.example.proyectofinaldammarina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinaldammarina.modelo.estanteria.Estanteria;
import com.example.proyectofinaldammarina.modelo.mueble.DAO.MuebleDAOImpl;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.example.proyectofinaldammarina.modelo.usuario.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

/**
 * Clase que controla los eventos del layout (XML) que se muestran cada vez que se escanea
 * un código qr asociado a un mueble o cada vez que se pulsa un item del historial de un mueble que existe
 */
public class ComparacionMuebleActivity extends AppCompatActivity {

    // Se declaran variables
    private Mueble mueble;

    private TextView nombre, medidas, precio, descripcion, textoEstanteria;
    private ImageView imagenMueble, imagenEstanteria;

    private Button botonEditar;

    private BottomSheetDialog dialog;

    private FirebaseFirestore db;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparacion_mueble);

        // Se instancia las variables
        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        // Se obtiene el objeto de tipo mueble que las clases que llaman a esta pasan (EscanerFragment y AdaptadorHistorial)
        mueble = (Mueble) getIntent().getSerializableExtra("mueble");

        //Se asocian las variables con los elementos xml del layout asociado a este activity
        nombre = findViewById(R.id.nombreComparacionMueble);
        medidas = findViewById(R.id.medidasComparacionMueble);
        precio = findViewById(R.id.precioComparacionMueble);
        descripcion = findViewById(R.id.descripcionComparacionMueble);

        imagenMueble = findViewById(R.id.imageComparacionMueble);
        imagenEstanteria = findViewById(R.id.imageComparacionEstanteria);

        textoEstanteria = findViewById(R.id.textViewEstanteria2);

        botonEditar = findViewById(R.id.botonModificarComparacionMueble);

        cargarDatos();

        comprobarPermisos();

        /**
         * Se controla el evento que sucede al pulsar el botón editar (Sólo será visible para los empleados)
         */
        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialog();
            }
        });

    }

    /**
     * Método que muestra un diálogo en el que se carga el contenido del layout
     * 'actualizar_mueble'. Desde aquí el empleado podrá modificar algunos
     * datos del mueble
     */
    private void mostrarDialog() {
        final Dialog dialog = new Dialog(ComparacionMuebleActivity.this);
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
        nombreActualizar.setText(mueble.getNombre());
        medidasActualizar.setText(mueble.getMedidas());
        precioActualizar.setText(mueble.getPrecio().toString());
        descripcionActualizar.setText(mueble.getDescripcion());

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
                            .Builder(ComparacionMuebleActivity.this)
                            .setPositiveButton("Sí, modificar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Se crea un objecto de tipo mueble con los datos que no han cambiado (codigo, imagen, zona y estanteria)
                                    // y se recogen los datos que sí que se pueden cambiar desde el layout 'actualizar_mueble'
                                    Mueble mueb = new Mueble(mueble.getCodigoQr(), mueble.getImagen(),
                                            nombreActualizar.getText().toString().trim(), Double.parseDouble(precioActualizar.getText().toString().trim()),
                                            medidasActualizar.getText().toString().trim(), descripcionActualizar.getText().toString().trim());
                                    //Actualizamos los campos modificados
                                    try {
                                        MuebleDAOImpl muebleDAO = new MuebleDAOImpl(db, "muebles");
                                        muebleDAO.actualizarMueble(mueb, ComparacionMuebleActivity.this);
                                    }catch (Exception e){
                                        Toast.makeText(ComparacionMuebleActivity.this, "Error al actualizar los campos.", Toast.LENGTH_LONG).show();
                                    }
                                    dialog.dismiss();
                                    startActivity(new Intent(ComparacionMuebleActivity.this, MenuActivity.class));
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
                    Toast.makeText(ComparacionMuebleActivity.this, "Los campos no pueden estar vacios", Toast.LENGTH_LONG).show();
                }

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
     * Este método sirve para cargar los datos del mueble correspondiente en los campos del layout
     */
    private void cargarDatos(){

        nombre.setText(mueble.getNombre());
        medidas.setText(mueble.getMedidas());
        precio.setText(mueble.getPrecio() + "€");
        descripcion.setText(mueble.getDescripcion());

        Picasso.get().load(mueble.getImagen()).into(imagenMueble);

        /**
         * La imagen de la estantería cambia según su stock o de si el mueble tiene una estantería
         */
        if(mueble.getEstanteriaId().equals("Sin estanteria")){

            textoEstanteria.setText("");
            imagenEstanteria.setImageResource(R.drawable.x);
            // Se usa un diálogo que informa al usuario que el mueble no está en ninguna estantería
            AlertDialog dialogoOkSinEstanteria = new AlertDialog.Builder(ComparacionMuebleActivity.this).create();
            dialogoOkSinEstanteria.setTitle("Aviso");
            dialogoOkSinEstanteria.setIcon(R.drawable.ic_baseline_info_24);
            dialogoOkSinEstanteria.setMessage("¡El mueble no está disponible en ninguna estantería en este momento!");

            dialogoOkSinEstanteria.setButton(Dialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialogoOkSinEstanteria.dismiss();
                }
            });

            dialogoOkSinEstanteria.show();

        }else{
            textoEstanteria.setText(mueble.getEstanteriaId());

            //Buscamos el stock de la estanteria
            db.collection("estanterias").document(mueble.getEstanteriaId())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Estanteria estanteria = documentSnapshot.toObject(Estanteria.class);
                            if (estanteria.getStock() == 0){
                                //Estantería vacía
                                imagenEstanteria.setImageResource(R.drawable.estanteriavacia);
                                // Se usa un diálogo que informa al usuario que el mueble ya está fuera de stock
                                AlertDialog dialogoOkFueraStock = new AlertDialog.Builder(ComparacionMuebleActivity.this).create();
                                dialogoOkFueraStock.setTitle("Aviso");
                                dialogoOkFueraStock.setIcon(R.drawable.ic_baseline_info_24);
                                dialogoOkFueraStock.setMessage("¡El mueble está fuera de stock en este momento!.");

                                dialogoOkFueraStock.setButton(Dialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialogoOkFueraStock.dismiss();
                                    }
                                });

                                dialogoOkFueraStock.show();

                            }else if(estanteria.getStock() == estanteria.getCantMax()){
                                //Estanteria llena
                                imagenEstanteria.setImageResource(R.drawable.estanteriallena);
                            }else{
                                imagenEstanteria.setImageResource(R.drawable.estanteriamedio);
                            }

                        }
                    });
        }
    }

    /**
     * Este método comprueba los permisos que el usuario tiene dependiendo de su rol
     */
    private void comprobarPermisos(){
        db.collection("usuarios").document(firebaseAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Usuario usuario = document.toObject(Usuario.class);
                        if (usuario.getIdRol().equals("empleado")) {
                            // Puede pulsar botón editar
                            botonEditar.setVisibility(View.VISIBLE);

                        } else { //cliente
                            // No puede pulsar botón editar
                            botonEditar.setVisibility(View.GONE);

                        }
                    }
                }
            }
        });
    }
}