package com.rafaels.audiolibros;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by Rafael S Martin on 04/02/2018.
 */

public class SplashScreenActivity extends Activity{

    // Duracion de  splash screen
    private static final long SPLASH_SCREEN_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Animacion de ImageView
        ImageView iv = (ImageView)findViewById(R.id.image_view_splash);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.entrada_alfa);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(1000);
        iv.startAnimation(anim);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Comienzo de MainActivity
                Intent mainIntent = new Intent().setClass(
                        SplashScreenActivity.this, MainActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);

                // Cierro la actividad para q no se pueda volver atras
                finish();
            }
        };

        // Simula un proceso de carga al inicio de la app
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

}
