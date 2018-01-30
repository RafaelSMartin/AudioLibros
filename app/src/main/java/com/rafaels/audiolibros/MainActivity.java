package com.rafaels.audiolibros;

import android.app.AlertDialog;
import android.app.Application;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rafaels.audiolibros.fragments.DetalleFragment;
import com.rafaels.audiolibros.fragments.SelectorFragment;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                irUltimoVisitado();
            }
        });

        if((findViewById(R.id.contenedor_pequeno) != null) &&
                (getFragmentManager().findFragmentById(R.id.contenedor_pequeno) == null)){
            SelectorFragment primerFragment = new SelectorFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.contenedor_pequeno, primerFragment)
                    .commit();
        }

//        Aplicacion app = (Aplicacion) getApplication();
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        recyclerView.setAdapter(app.getAdaptador());
//        layoutManager = new LinearLayoutManager(this);
////        layoutManager = new GridLayoutManager(this, 2);
////        layoutManager = new GridLayoutManager(this, 2,
////                LinearLayoutManager.HORIZONTAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//
//        app.getAdaptador().setOnItemClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Toast.makeText(MainActivity.this, "Seleccionado elemento: "
//                + recyclerView.getChildAdapterPosition(v), Toast.LENGTH_SHORT).show();
//            }
//        });

        //Pestañas
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Todos"));
        tabs.addTab(tabs.newTab().setText("Nuevos"));
        tabs.addTab(tabs.newTab().setText("Leidos"));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: //Todos
//                        adaptador.setNovedad(false);
//                        adaptador.setLeido(false);
                        break;
                    case 1: // Nuevos
//                        adaptador.setNovedad(true);
//                        adaptador.setLeido(false);
                        break;
                    case 2: // Leidos
//                        adaptador.setNovedad(false);
//                        adaptador.setLeido(true);
                        break;
                }
//                adaptador.notifyDataSetChanged();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_preferencias) {
            Toast.makeText(this, "Preferencias", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_acerca){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Mensaje Acerca De");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void mostrarDetalle(int id){
        DetalleFragment detalleFragment = (DetalleFragment) getFragmentManager().findFragmentById(R.id.detalle_fragment);
        if(detalleFragment != null){
            detalleFragment.ponInfoLibro(id);
        }else{
            DetalleFragment nuevoFragment = new DetalleFragment();
            Bundle args = new Bundle();
            args.putInt(DetalleFragment.ARG_ID_LIBRO, id);
            nuevoFragment.setArguments(args);
            FragmentTransaction transaccion = getFragmentManager()
                    .beginTransaction();
            transaccion.replace(R.id.contenedor_pequeno, nuevoFragment);
            transaccion.addToBackStack(null);
            transaccion.commit();

            SharedPreferences pref = getSharedPreferences(
                    "com.rafaels.audiolibros_internal", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("ultimo", id);
            editor.commit();

        }
    }

    public void irUltimoVisitado(){
        SharedPreferences pref = getSharedPreferences(
                "com.rafaels.audiolibros_internal", MODE_PRIVATE);
        int id = pref.getInt("ultimo", -1);
        if(id >= 0){
            mostrarDetalle(id);
        } else{
            Toast.makeText(this, "Sin ultima vista", Toast.LENGTH_LONG).show();
        }
    }


}
