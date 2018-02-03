package com.rafaels.audiolibros.adaptador;

import com.rafaels.audiolibros.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rafael S Martin on 12/01/2018.
 */

public class _Libro {

    public String titulo;
    public String autor;
    public String urlImagen;
    public String urlAudio;
    public String genero;   // Genero literario
    public Boolean novedad; // Es una novedad
    public Boolean leido;   // Leido por el usuario

    public final static String G_TODOS = "Todos los géneros";
    public final static String G_EPICO = "Poema épico";
    public final static String G_S_XIX = "Literatura siglo XIX";
    public final static String G_SUSPENSE = "Suspense";
    public final static String[] G_ARRAY = new String[] {G_TODOS, G_EPICO,
            G_S_XIX, G_SUSPENSE };


    public _Libro(String titulo, String autor, String urlImagen,
                 String urlAudio, String genero, Boolean novedad, Boolean leido) {
        this.titulo = titulo;
        this.autor = autor;
        this.urlImagen = urlImagen;
        this.urlAudio = urlAudio;
        this.genero = genero;
        this.novedad = novedad;
        this.leido = leido;
    }

    public static List<_Libro> _ejemploLibros() {
        final String SERVIDOR = "http://mmoviles.upv.es/audiolibros/";
        List<_Libro> libros = new ArrayList<_Libro>();

        libros.add(new _Libro("Kappa", "Akutagawa",
                SERVIDOR+"kappa.jpg",SERVIDOR+"kappa.mp3",
                Libro.G_S_XIX, false, false));

        libros.add(new _Libro("Avecilla", "Alas Clarín, Leopoldo",
                SERVIDOR+"avecilla.jpg",SERVIDOR+"avecilla.mp3",
                Libro.G_S_XIX, true, false));

        libros.add(new _Libro("Divina Comedia", "Dante",
                SERVIDOR+"divina_comedia.jpg",SERVIDOR+"divina_comedia.mp3",
                Libro.G_EPICO, true, false));

        libros.add(new _Libro("Viejo Pancho, El", "Alonso y Trelles, José",
                SERVIDOR+"viejo_pancho.jpg",SERVIDOR+"viejo_pancho.mp3",
                Libro.G_S_XIX, true, true));

        libros.add(new _Libro("Canción de Rolando", "Anónimo",
                SERVIDOR+"cancion_rolando.jpg",SERVIDOR+"cancion_rolando.mp3",
                Libro.G_EPICO, false, true));

        libros.add(new _Libro("Matrimonio de sabuesos", "Agata Christie",
                SERVIDOR+"matrim_sabuesos.jpg",SERVIDOR+"matrim_sabuesos.mp3",
                Libro.G_SUSPENSE, false, true));

        libros.add(new _Libro("La iliada", "Homero",
                SERVIDOR+"la_iliada.jpg",SERVIDOR+"la_iliada.mp3",
                Libro.G_EPICO, true, false));
        return libros;
    }


}