package com.rafaels.audiolibros.fragments;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.rafaels.audiolibros.Aplicacion;
import com.rafaels.audiolibros.MainActivity;
import com.rafaels.audiolibros.adaptador.Libro;
import com.rafaels.audiolibros.R;
import com.rafaels.audiolibros.zoomSeekBar.OnValListener;
import com.rafaels.audiolibros.zoomSeekBar.ZoomSeekBar;

import java.io.IOException;

/**
 * Created by Rafael S Martin on 26/01/2018.
 */

public class DetalleFragment extends Fragment implements
        View.OnTouchListener, MediaPlayer.OnPreparedListener,
        MediaController.MediaPlayerControl, OnValListener
{

    public static String ARG_ID_LIBRO = "id_libro";
    MediaPlayer mediaPlayer;
    MediaController mediaController;

    // Variable tipo ZoomSeekBar
    ZoomSeekBar zoomSeekBar;
    private Handler handler;

    //Notificaciones
    private static final int ID_NOTIFICACION = 1;
    static final String ID_CANAL = "channel_id";
    private NotificationManager notificManager;
    private NotificationCompat.Builder notificacion;
    private RemoteViews remoteViews;

    public static final String ACCION_DEMO =
            "com.rafaels.audiolibros.ACCION_DEMO";
    public static final String EXTRA_PARAM =
            "com.rafaels.audiolibros.EXTRA_PARAM";



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

        // Instacio y pongo a escuchar el zoomSeekBar
        zoomSeekBar = (ZoomSeekBar) vista.findViewById(R.id.zoomSeekBar);
        zoomSeekBar.setOnValListener(this);

        handler = new Handler();


        return vista;
    }

    @Override
    public void onResume(){
        DetalleFragment detalleFragment = (DetalleFragment)getFragmentManager().findFragmentById(R.id.detalle_fragment);
        if (detalleFragment == null){
            ((MainActivity)getActivity()).mostrarElementos(false);
        }
        super.onResume();
    }

    // Introducimos la info en la vista
    private void ponInfoLibro(int id, View vista){
        // Referencia del libro a representar
        final Libro libro = ((Aplicacion) getActivity().getApplication()).getListaLibros().get(id);

        // Actualizamos la info
        ((TextView) vista.findViewById(R.id.titulo)).setText(libro.titulo);
        ((TextView) vista.findViewById(R.id.autor)).setText(libro.autor);
//        ((ImageView) vista.findViewById(R.id.portada)).setImageResource(libro.recursoImagen);
        final Aplicacion aplicacion = (Aplicacion)getActivity().getApplication();
//        ((NetworkImageView) vista.findViewById(R.id.portada)).setImageUrl(
//                libro.urlImagen,aplicacion.getLectorImagenes());


        RequestQueue colaPeticiones = Volley.newRequestQueue(aplicacion);
        ((NetworkImageView) vista.findViewById(R.id.portada)).setImageUrl( libro.urlImagen,
                new ImageLoader(
                colaPeticiones,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache = new LruCache<>(10);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        crearNotificacion(libro,bitmap,aplicacion);
                        cache.put(url,bitmap);
                    }
                }));




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
//        mediaPlayer.start();
        SharedPreferences preferencias = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        if(preferencias.getBoolean("pref_autoreproducir", true)){
            this.start();
        }

        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(getView().findViewById(R.id.fragment_detalle));
//        mediaController.setPadding(0,0,0,110);
        mediaController.setEnabled(true);
        // solo aparece 3 seg
        mediaController.show();

        int duration = mediaPlayer.getDuration() / 1000;
        zoomSeekBar.setValMin(0);
        zoomSeekBar.setValMax(duration);
        zoomSeekBar.setEscalaMin(0);
        zoomSeekBar.setEscalaMax(duration);
        zoomSeekBar.setEscalaIni(0);
        zoomSeekBar.setEscalaRaya(duration/20);
        zoomSeekBar.setEscalaRayaLarga(5);

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
        updateProgress();
    }

    @Override public int getAudioSessionId() {
        return 0;
    }


    @Override
    public void onChangeVal(int newVal) {
        this.seekTo((newVal * 1000));
    }

    public void updateProgress(){
        if(handler != null){
            handler.postDelayed(updateProgress,1000);
        }
    }

    private Runnable updateProgress = new Runnable(){
        public void run(){
            int pos = mediaPlayer.getCurrentPosition();
            Log.d("POS", pos+" - "+zoomSeekBar.getValMin()+"-"+zoomSeekBar.getValMax());
            zoomSeekBar.setValNoEvent((pos/1000));
            handler.postDelayed(this,1000);
        }
    };

    @Override
    public void onPause() {
        handler.removeCallbacks(updateProgress);
        super.onPause();
    }

    private void crearNotificacion(Libro libro, Bitmap bitmap, Aplicacion aplicacion){
        remoteViews = new RemoteViews(aplicacion.getPackageName(),
                R.layout.custom_notification);
        // remoteViews.setImageViewResource(R.id.imagen_notificacion, R.drawable.ic_book_white_24dp);
        remoteViews.setImageViewBitmap(R.id.imagen_notificacion, bitmap);
        remoteViews.setImageViewResource(R.id.accion_notificacion,
                android.R.drawable.ic_media_play);
        remoteViews.setTextViewText(R.id.titulo_notificacion, libro.titulo);
        remoteViews.setTextColor(R.id.titulo_notificacion, Color.WHITE);
        remoteViews.setTextViewText(R.id.autor_notificacion, libro.autor);
        remoteViews.setTextColor(R.id.autor_notificacion, Color.WHITE);


        Intent intent = new Intent();
        intent.setAction(ACCION_DEMO);
        intent.putExtra(EXTRA_PARAM, "otro parámetro");
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.accion_notificacion, pendingIntent);


        notificacion = new NotificationCompat.Builder(aplicacion, ID_CANAL)
                .setContent(remoteViews)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Custom Notification");
        notificManager = (NotificationManager)aplicacion.getSystemService(
                Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26){
            NotificationChannel channel = new NotificationChannel(ID_CANAL,"Nombre del canal",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Descripción del canal");
            notificManager.createNotificationChannel(channel);
        }

        notificManager.notify(ID_NOTIFICACION, notificacion.build());

        IntentFilter filtro = new IntentFilter(ACCION_DEMO);
        getActivity().registerReceiver(new ReceptorAnuncio(), filtro);



    }

    public class ReceptorAnuncio extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String param = intent.getStringExtra(EXTRA_PARAM);
            Toast.makeText(getActivity(), "Parámetro:" + param, Toast.LENGTH_LONG).show();

            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                remoteViews.setImageViewResource(R.id.accion_notificacion,
                        R.drawable.ic_stat_name);
            } else{
                mediaPlayer.start();
                remoteViews.setImageViewResource(R.id.accion_notificacion,
                        android.R.drawable.ic_media_play);
            }

            notificManager.notify(ID_NOTIFICACION, notificacion.build());
        }
    }



}