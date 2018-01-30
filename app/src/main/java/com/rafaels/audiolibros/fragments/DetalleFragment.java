package com.rafaels.audiolibros.fragments;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import com.rafaels.audiolibros.Aplicacion;
import com.rafaels.audiolibros.adaptador.Libro;
import com.rafaels.audiolibros.R;

import java.io.IOException;

/**
 * Created by Rafael S Martin on 26/01/2018.
 */

public class DetalleFragment extends Fragment implements
        View.OnTouchListener, MediaPlayer.OnPreparedListener,
        MediaController.MediaPlayerControl
{

    public static String ARG_ID_LIBRO = "id_libro";
    MediaPlayer mediaPlayer;
    MediaController mediaController;

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor,
                             Bundle savedInstanceState){
        View vista = inflador.inflate(R.layout.fragment_detalle, contenedor, false);

        //Vemos si nos han pasado argumentos con tag "id_libros" para visualizarlo
        // si no visualizamos el id 0
        Bundle args = getArguments();
        if(args != null){
            int position = args.getInt(ARG_ID_LIBRO);
            ponInfoLibro(position, vista);
        } else {
            ponInfoLibro(0, vista);
        }
        return vista;
    }

    // Introducimos la info en la vista
    private void ponInfoLibro(int id, View vista){
        // Referencia del libro a representar
        Libro libro = ((Aplicacion) getActivity().getApplication()).getListaLibros().get(id);

        // Actualizamos la info
        ((TextView) vista.findViewById(R.id.titulo)).setText(libro.titulo);
        ((TextView) vista.findViewById(R.id.autor)).setText(libro.autor);
        ((ImageView) vista.findViewById(R.id.portada)).setImageResource(libro.recursoImagen);

        //Ponemos a escuchar la vista
        vista.setOnTouchListener(this);

        if(mediaPlayer != null){
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaController = new MediaController(getActivity());
        //Al set el audio de web, se hace asincrono
        Uri audio = Uri.parse(libro.urlAudio);
        try{
            mediaPlayer.setDataSource(getActivity(), audio);
            mediaPlayer.prepareAsync();
        } catch(IOException e){
            Log.e("AudioLibros", "ERROR: No se puede reproducir" + audio,e);
        }
    }

    public void ponInfoLibro(int id){
        ponInfoLibro(id, getView());
    }

    //Cuando se tenga preparado el audio para reproducir se ejecutara onPrepared()
    @Override
    public void onPrepared(MediaPlayer mediaPlayer){
        Log.d("AudioLibros", "Entramos en onPrepared de MediaPlayer");
        mediaPlayer.start();
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(getView().findViewById(R.id.fragment_detalle));
//        mediaController.setPadding(0,0,0,110);
        mediaController.setEnabled(true);
        // solo aparece 3 seg
        mediaController.show();
    }

    @Override
    public boolean onTouch(View vista, MotionEvent evento){
        // solo aparece 3 seg
        mediaController.show();
        return false;
    }

    @Override
    public void onStop(){
        mediaController.hide();
        try{
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch(Exception e){
            Log.d("AudioLibros", "Error en mediaPlayer.stop()");
        }
        super.onStop();
    }

    @Override
    public boolean canPause(){
        return true;
    }

    @Override public boolean canSeekBackward() {
        return true;
    }

    @Override public boolean canSeekForward() {
        return true;
    }

    @Override public int getBufferPercentage() {
        return 0;
    }

    @Override public int getCurrentPosition() {
        try {
            return mediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override public void pause() {
        mediaPlayer.pause();
    }

    @Override public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override public void start() {
        mediaPlayer.start();
    }

    @Override public int getAudioSessionId() {
        return 0;
    }






}