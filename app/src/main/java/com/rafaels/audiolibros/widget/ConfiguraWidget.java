package com.rafaels.audiolibros.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;

import com.rafaels.audiolibros.R;

/**
 * Created by Rafael S Martin on 11/02/2018.
 */

public class ConfiguraWidget extends AppCompatActivity {

    int widgetId;
    Switch colorSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configura_widget);

        colorSwitch = (Switch) findViewById(R.id.switch1);
        setResult(RESULT_CANCELED);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        }

        widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }


    public void buttonOK(View view) {
        SharedPreferences prefs = getSharedPreferences(
                "com.rafaels.audiolibros_internal", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("color_" + widgetId, colorSwitch.isChecked());
        editor.commit();

        MiAppWidgetProvider.actualizaWidget(this, widgetId);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

}