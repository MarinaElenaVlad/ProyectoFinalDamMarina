package com.example.proyectofinaldammarina;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinaldammarina.modelo.historial.Historial;
import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *  Clase que extiende de RecyclerView.Adapter y que ayuda a personalizar
 *  la forma en la que se muestran los items del recycler view del
 *  historial
 */
public class AdaptadorHistorial extends RecyclerView.Adapter<AdaptadorHistorial.Vist> {

    // Se declaran variables
    private Context context;
    private List<Mueble> muebles;
    ConstraintLayout filaHistorial;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private Historial historial;

    /**
     * Constructor con parámetros
     * @param context
     * @param muebles
     */
    public AdaptadorHistorial(Context context, List<Mueble> muebles) {
        this.context = context;
        this.muebles = muebles;
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public Vist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Recibimos un elemento de la vista que vamos a utilizar para visualizar los elementos
        LayoutInflater inflater = LayoutInflater.from(context);//crea el contenedor
        //Rellena el contenedor con los datos
        View v = inflater.inflate(R.layout.mueblehistorial, parent, false);

        return new Vist(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Vist holder, @SuppressLint("RecyclerView") int position) {
        holder.nombreMueble.setText("[" + muebles.get(position).getNombre() + "]");

        /**
         * Se controla el evento que sucede cuando se pulsa un elemento del historial
         */
        filaHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("muebles").document(muebles.get(position).getCodigoQr()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Mueble mueble = document.toObject(Mueble.class);
                            // Se vuelve a mostrar la comparación
                            Intent intent = new Intent(context, ComparacionMuebleActivity.class);
                            intent.putExtra("mueble", mueble);
                            context.startActivity(intent);
                        }else{
                            // Se usa un diálogo que informa al usuario que el mueble ya no existe
                            AlertDialog dialogoOk = new AlertDialog.Builder(context).create();
                            dialogoOk.setTitle("Aviso");
                            dialogoOk.setIcon(R.drawable.ic_baseline_info_24);
                            dialogoOk.setMessage("¡El mueble ya no existe!.");

                            dialogoOk.setButton(Dialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialogoOk.dismiss();
                                }
                            });

                            dialogoOk.show();

                            /**
                             * Si el mueble ya no existe es borrado cuando el usuario lo pulsa
                             */
                            // 1º buscamos la lista del historial
                            db.collection("historial").whereEqualTo("usuario", firebaseAuth.getUid()).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    historial = document.toObject(Historial.class);
                                                }

                                                //1º Se borra el mueble que no ya no existe de la lista
                                                for(Iterator iterator = historial.getMuebleList().iterator(); iterator.hasNext();){
                                                    Mueble mueble = (Mueble) iterator.next();
                                                    if(mueble.getCodigoQr().equals(muebles.get(position).getCodigoQr())){
                                                        // Se borra la referencia del mueble que ya no existe de la lista del historial
                                                        iterator.remove();
                                                    }
                                                }

                                                db.collection("historial").document(historial.getIdentificador()).update("muebleList", historial.getMuebleList())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        System.out.println("lista historial actualizada");
                                                                        context.startActivity(new Intent(context, MenuActivity.class));
                                                                    }
                                                                });

                                            } else {
                                                Toast.makeText(context, "Error getting documents:", Toast.LENGTH_LONG).show();
                                            }
                                        }});
                        }
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return muebles.size();
    }

    public class Vist extends RecyclerView.ViewHolder {
        TextView nombreMueble;

        public Vist(@NonNull View itemView) {
            super(itemView);

            nombreMueble = itemView.findViewById(R.id.textViewHistorial);

            filaHistorial = itemView.findViewById(R.id.filaElementoHistorial);
        }
    }
}
