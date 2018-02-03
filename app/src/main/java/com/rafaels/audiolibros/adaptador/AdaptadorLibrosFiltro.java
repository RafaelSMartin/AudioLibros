package com.rafaels.audiolibros.adaptador;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafael S Martin on 03/02/2018.
 */

public class AdaptadorLibrosFiltro extends AdaptadorLibros {

    private List<Libro> listaSinFiltro; // Lista con todos los libros
    private List<Integer> indiceFiltro; // Índice en listaSinFiltro de cada elemento de listaLibros
    private String busqueda = "";            // Búsqueda sobre autor o título
    private String genero = "";         // Genero seleccionado
    private boolean novedad = false;    // Si queremos ver solo novedades
    private boolean leido = false;      // Si queremos ver solo leido

    public AdaptadorLibrosFiltro(Context contexto, List<Libro> listaLibros){
        super(contexto, listaLibros);
        listaSinFiltro = listaLibros;
        recalcularFiltro();
    }


    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
        recalcularFiltro();
    }

    public void setGenero(String genero) {
        this.genero = genero;
        recalcularFiltro();
    }

    public void setNovedad(boolean novedad) {
        this.novedad = novedad;
        recalcularFiltro();
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
        recalcularFiltro();
    }

    public void recalcularFiltro(){
        listaLibros = new ArrayList<Libro>();
        indiceFiltro = new ArrayList<Integer>();
        for (int i = 0; i < listaSinFiltro.size(); i++){
            Libro libro = listaSinFiltro.get(i);
            if((libro.titulo.toLowerCase().contains(busqueda) ||
                    libro.autor.toLowerCase().contains(busqueda))
                    && (libro.genero.startsWith(genero))
                    && (!novedad || (novedad && libro.novedad))
                    && (!leido || (leido && libro.leido))){
                listaLibros.add(libro);
                indiceFiltro.add(i);
            }
        }
    }

    public Libro getItem (int position){
        return listaSinFiltro.get(indiceFiltro.get(position));
    }

    public long getItemId (int position){
        return indiceFiltro.get(position);
    }

    public void borrar (int position){
        listaSinFiltro.remove((int)getItemId(position));
        recalcularFiltro();
    }

    public void insertar(Libro libro){
        listaSinFiltro.add(libro);
        recalcularFiltro();
    }


}
