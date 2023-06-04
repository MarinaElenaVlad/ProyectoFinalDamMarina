package com.example.proyectofinaldammarina;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.Vista>{

    private List<Mueble> muebleList;
    private CardView filaMueble;
    private Context context;

    public Adaptador(List<Mueble> muebleList, Context context) {
        this.muebleList = muebleList;
        this.context = context;
    }

    public void setSearchView(List<Mueble> listaFiltrada){
        this.muebleList = listaFiltrada;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Vista onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Recibimos un elemento de la vista que vamos a utilizar para visualizar los elementos
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());//crea el contenedor
        //Rellena el contenedor con los datos
        View v = inflater.inflate(R.layout.mueble, parent, false);

        return new Vista(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Vista holder, @SuppressLint("RecyclerView") int position) {
        /**
         * LA IMAGEN!! + controlar si se pulsa ese item
         */
        holder.nombreMueble.setText(muebleList.get(position).getNombre());
//        holder.precioMueble.setText(muebleList.get(position).getPrecio() + "€");
        holder.precioMueble.setText(muebleList.get(position).getPrecio() + "");
        /**
         * poner signo euro en la pantalla, no en la variable!
         */


        filaMueble.setOnClickListener(new View.OnClickListener() { //NUEVO
            @Override
            public void onClick(View v) {
                /**
                 * Pruebas, dio una vez error al buscar y dar click, pero luego no!
                 */
                Intent intent = new Intent(context, DetalleMuebleActivity.class);
                intent.putExtra("codigo", muebleList.get(position).getCodigoQr());
                intent.putExtra("nombre", muebleList.get(position).getNombre());
                intent.putExtra("precio", muebleList.get(position).getPrecio());
                intent.putExtra("medidas", muebleList.get(position).getMedidas());
                intent.putExtra("imagen", muebleList.get(position).getImagen());
                intent.putExtra("descripcion", muebleList.get(position).getDescripcion());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return muebleList.size();
    }

    public class Vista extends RecyclerView.ViewHolder {
        TextView nombreMueble, precioMueble;

        public Vista(@NonNull View itemView) {
            super(itemView);
            nombreMueble = itemView.findViewById(R.id.muebleNombreItem);
            precioMueble = itemView.findViewById(R.id.mueblePrecioItem);

            filaMueble = itemView.findViewById(R.id.filaMueble);

        }
    }
}
