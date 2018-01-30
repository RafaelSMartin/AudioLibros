package com.rafaels.audiolibros;

import android.app.Application;

import com.rafaels.audiolibros.adaptador.AdaptadorLibros;
import com.rafaels.audiolibros.adaptador.Libro;

import java.util.List;

/**
 * Created by Rafael S Martin on 12/01/2018.
 */

public class Aplicacion extends Application {

    private List<Libro> listaLibros;
    private AdaptadorLibros adaptador;

    @Override
    public void onCreate(){
        listaLibros = Libro.ejemploLibros();
        adaptador = new AdaptadorLibros(this, listaLibros);
    }

    public AdaptadorLibros getAdaptador(){
        return adaptador;
    }

    public List<Libro> getListaLibros(){
        return listaLibros;
    }

}
