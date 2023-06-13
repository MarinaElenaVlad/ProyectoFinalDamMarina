package com.example.proyectofinaldammarina;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectofinaldammarina.modelo.mueble.Mueble;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Clase que extiende de RecyclerView.Adapter y que ayuda a personalizar
 * la forma en la que se muestran los items del recycler view del
 * fragment lista
 */
public class Adaptador extends RecyclerView.Adapter<Adaptador.Vista>{

    // Se declaran los atributos
    private List<Mueble> muebleList;
    private CardView filaMueble;
    private Context context;

    /**
     * Constructor con parámetros
     * @param muebleList
     * @param context
     */
    public Adaptador(List<Mueble> muebleList, Context context) {
        this.muebleList = muebleList;
        this.context = context;
    }

    /**
     * Método que actualiza la lista según lo que se escriba en la barra de búsqueda
     * @param listaFiltrada
     */
    public void setSearchView(List<Mueble> listaFiltrada){
        this.muebleList = listaFiltrada;
        notifyDataSetChanged();
    }

    // Se vincula el recycler y los elementos del layout mueble
    @NonNull
    @Override
    public Vista onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Recibimos un elemento de la vista que vamos a utilizar para visualizar los elementos
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());//crea el contenedor
        //Rellena el contenedor con los datos
        View v = inflater.inflate(R.layout.mueble, parent, false);

        return new Vista(v);
    }

    // Vinculamos vista (layout mueble) con el contenedor de la vista (layout lista fragment)
    @Override
    public void onBindViewHolder(@NonNull Vista holder, @SuppressLint("RecyclerView") int position) {
        // Se seleccionan los elementos del layout y se les asigna los datos que hay en el array
        Picasso.get().load(muebleList.get(position).getImagen()).into(holder.imagenMueble);
        holder.nombreMueble.setText(muebleList.get(position).getNombre());
        holder.precioMueble.setText(muebleList.get(position).getPrecio() + "€");

        /**
         * Se controla el evento que ocurre cuando se pulsa un item de la lista
         */
        filaMueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se crea un nuevo intent y se les pasa los siguientes valores
                Intent intent = new Intent(context, DetalleMuebleActivity.class);
                intent.putExtra("codigo", muebleList.get(position).getCodigoQr());
                intent.putExtra("nombre", muebleList.get(position).getNombre());
                intent.putExtra("precio", muebleList.get(position).getPrecio());
                intent.putExtra("medidas", muebleList.get(position).getMedidas());
                intent.putExtra("imagen", muebleList.get(position).getImagen());
                intent.putExtra("descripcion", muebleList.get(position).getDescripcion());
                intent.putExtra("zonaId", muebleList.get(position).getZonaId());

                // Cambiamos de intent
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return muebleList.size();
    }

    public class Vista extends RecyclerView.ViewHolder {

        // Se crea la clase vista como subclase de la clase adaptador y extiende de RecyclerView.ViewHolder

        //Declaramos variables del tipo que queremos mostrar
        TextView nombreMueble, precioMueble;

        ImageView imagenMueble;

        public Vista(@NonNull View itemView) {
            super(itemView);

            //Vinculamos variables con elementos del layout
            nombreMueble = itemView.findViewById(R.id.muebleNombreItem);
            precioMueble = itemView.findViewById(R.id.mueblePrecioItem);
            imagenMueble = itemView.findViewById(R.id.imagenMuebleItem);

            filaMueble = itemView.findViewById(R.id.filaMueble);

        }
    }
}
