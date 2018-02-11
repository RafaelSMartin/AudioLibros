package com.rafaels.audiolibros.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.widget.RemoteViews;

import com.rafaels.audiolibros.Aplicacion;
import com.rafaels.audiolibros.MainActivity;
import com.rafaels.audiolibros.R;

import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rafael S Martin on 11/02/2018.
 */

public class MiAppWidgetProvider extends AppWidgetProvider {

    public static final String ACCION_REPRODUCTOR =
            "com.rafaels.audiolibros.ACCION_REPRODUCTOR";
    private Aplicacion aplicacion;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] widgetIds) {

        aplicacion= (Aplicacion) context.getApplicationContext();

        for (int widgetId: widgetIds) {
            actualizaWidget(context, widgetId);
        }
    }

    public static void actualizaWidget(Context context, int widgetId) {
        SharedPreferences pref = context.getSharedPreferences(
                "com.rafaels.audiolibros_internal", MODE_PRIVATE);
        boolean color = pref.getBoolean("color_" + widgetId,false);

        List<String> ultimo = obtenerUltimoLibro(context);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.mi_appwidget);
        remoteViews.setTextViewText(R.id.titulo_widget, ultimo.get(0));
        remoteViews.setTextViewText(R.id.autor_widget, ultimo.get(1));

        if(color){
            remoteViews.setTextColor(R.id.titulo_widget,Color.parseColor("#FF5722") );
            remoteViews.setTextColor(R.id.autor_widget,Color.parseColor("#FF5722") );
        }

        // lanzar actividad
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.imagen_widget, pendingIntent);

        // lanzar el libro
        Intent intentLibro = new Intent(context,MiAppWidgetProvider.class);
        intentLibro.setAction(ACCION_REPRODUCTOR);
        intentLibro.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intentLibro.setData(Uri.parse(intentLibro.toUri(Intent.URI_INTENT_SCHEME)));
        pendingIntent=PendingIntent.getBroadcast(context, 0 ,intentLibro,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.accion_widget, pendingIntent);

        AppWidgetManager.getInstance(context).updateAppWidget(widgetId, remoteViews);
    }

    private static List<String> obtenerUltimoLibro(Context context){
        SharedPreferences pref = context.getSharedPreferences(
                "com.rafaels.audiolibros_internal", MODE_PRIVATE);
        String titulo = pref.getString("titulo", "No se ha visitado ningún libro");
        String autor = pref.getString("autor", "");
        return Arrays.asList(titulo, autor);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        aplicacion = (Aplicacion) context.getApplicationContext();

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.mi_appwidget);

        if (intent.getAction().equals(ACCION_REPRODUCTOR)) {
            int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            if(!aplicacion.isPlaying()){
                aplicacion.play();
                remoteViews.setImageViewResource(R.id.accion_widget,
                        R.drawable.ic_stat_name);
            }
            else{
                aplicacion.stop();
                remoteViews.setImageViewResource(R.id.accion_widget,
                        R.drawable.ic_play);
            }
            AppWidgetManager.getInstance(context).updateAppWidget(widgetId, remoteViews);
        }
        super.onReceive(context, intent);
    }

}