package com.rafaels.audiolibros.adaptador;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rafaels.audiolibros.R;

import java.util.List;

/**
 * Created by Rafael S Martin on 23/01/2018.
 */

public class AdaptadorLibros extends RecyclerView.Adapter<AdaptadorLibros.ViewHolder> {

    private LayoutInflater inflador;    //Crea Layouts a partir del XML
    protected List<Libro> listaLibros;  //Lista de libros a visualizar,
                                        // nos permite acceder a ella desde
                                        // un descendiente (AdaptadorLibrosFiltro)
    private Context context;

    private View.OnClickListener onClickListener; //Escuchador independiente para elemento
    private View.OnLongClickListener onLongClickListener; //Escuchador para click largo

    // set del Listener
    public void setOnItemClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public void setOnItemLongClickListener(View.OnLongClickListener onLongClickListener){
        this.onLongClickListener = onLongClickListener;
    }

    public AdaptadorLibros(Context contexto, List<Libro> listaLibros){
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listaLibros = listaLibros;
        this.context = contexto;
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView portada;
        public TextView titulo;

        public ViewHolder (View itemView){
            super(itemView);
            portada = (ImageView) itemView.findViewById(R.id.portada);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
        }
    }

    // Creamos el ViewHolder con las vista de un elemento sin personalizar
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.elemento_selector, null);

        //Instanciar el listener
        v.setOnClickListener(onClickListener);
        v.setOnLongClickListener(onLongClickListener);
        return new ViewHolder(v);
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion){
        Libro libro = listaLibros.get(posicion);
        holder.portada.setImageResource(libro.recursoImagen);
        holder.titulo.setText(libro.titulo);
    }

    // Indicamos el número de elementos de la lista
     @Override
     public int getItemCount() { return listaLibros.size(); }



}
