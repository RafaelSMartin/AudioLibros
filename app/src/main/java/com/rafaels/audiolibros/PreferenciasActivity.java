package com.rafaels.audiolibros;

import android.app.Activity;
import android.os.Bundle;

import com.rafaels.audiolibros.fragments.PreferenciasFragment;

/**
 * Created by Rafael S Martin on 03/02/2018.
 */

public class PreferenciasActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
    }
}
