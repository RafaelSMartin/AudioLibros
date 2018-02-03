package com.rafaels.audiolibros.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.rafaels.audiolibros.R;

/**
 * Created by Rafael S Martin on 03/02/2018.
 */

public class PreferenciasFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
