package com.rafaels.audiolibros;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
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

    private static RequestQueue colaPeticiones;
    private static ImageLoader lectorImagenes;

    @Override
    public void onCreate(){
        listaLibros = Libro.ejemploLibros();
        adaptador = new AdaptadorLibrosFiltro(this, listaLibros);

        colaPeticiones = Volley.newRequestQueue(this);
        lectorImagenes = new ImageLoader(colaPeticiones,
                new ImageLoader.ImageCache() {

                    private final LruCache<String, Bitmap> cache =
                            new LruCache<String, Bitmap>(10);
                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }
                });
    }

    public AdaptadorLibrosFiltro getAdaptador(){
        return adaptador;
    }

    public List<Libro> getListaLibros(){
        return listaLibros;
    }

    public static RequestQueue getColaPeticiones() {
        return colaPeticiones;
    }

    public static ImageLoader getLectorImagenes() {
        return lectorImagenes;
    }
}
