package com.rafaels.audiolibros;

import android.app.Application;

import com.rafaels.audiolibros.adaptador.AdaptadorLibros;
import com.rafaels.audiolibros.adaptador.AdaptadorLibrosFiltro;
import com.rafaels.audiolibros.adaptador.Libro;

import java.util.List;

/**
 * Created by Rafael S Martin on 12/01/2018.
 */

public class Aplicacion extends Application {

    private List<Libro> listaLibros;
    private AdaptadorLibrosFiltro adaptador;

    @Override
    public void onCreate(){
        listaLibros = Libro.ejemploLibros();
        adaptador = new AdaptadorLibrosFiltro(this, listaLibros);
    }

    public AdaptadorLibrosFiltro getAdaptador(){
        return adaptador;
    }

    public List<Libro> getListaLibros(){
        return listaLibros;
    }

}
