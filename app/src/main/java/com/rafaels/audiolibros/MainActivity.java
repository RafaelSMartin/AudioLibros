package com.rafaels.audiolibros;

import android.app.AlertDialog;
import android.app.Application;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rafaels.audiolibros.adaptador.AdaptadorLibrosFiltro;
import com.rafaels.audiolibros.adaptador.Libro;
import com.rafaels.audiolibros.fragments.DetalleFragment;
import com.rafaels.audiolibros.fragments.PreferenciasFragment;
import com.rafaels.audiolibros.fragments.SelectorFragment;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener
{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorLibrosFiltro adaptador;

    private AppBarLayout appBarLayout;
    private TabLayout tabs;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);

        adaptador = ((Aplicacion) getApplicationContext()).getAdaptador();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                irUltimoVisitado();
            }
        });

//        if((findViewById(R.id.contenedor_pequeno) != null) &&
//                (getFragmentManager().findFragmentById(R.id.contenedor_pequeno) == null)){
//            SelectorFragment primerFragment = new SelectorFragment();
//            getFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.contenedor_pequeno, primerFragment)
//                    .commit();
//        }

        int idContenedor = (findViewById(R.id.contenedor_pequeno) != null) ?
                R.id.contenedor_pequeno : R.id.contenedor_izquierdo;
        SelectorFragment primerFragment = new SelectorFragment();
        getFragmentManager()
                .beginTransaction()
                .add(idContenedor, primerFragment)
                .commit();

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
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Todos"));
        tabs.addTab(tabs.newTab().setText("Nuevos"));
        tabs.addTab(tabs.newTab().setText("Leidos"));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: //Todos
                        adaptador.setNovedad(false);
                        adaptador.setLeido(false);
                        break;
                    case 1: // Nuevos
                        adaptador.setNovedad(true);
                        adaptador.setLeido(false);
                        break;
                    case 2: // Leidos
                        adaptador.setNovedad(false);
                        adaptador.setLeido(true);
                        break;
                }
                adaptador.notifyDataSetChanged();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Navigation Drawer
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.nav_todos) {
            adaptador.setGenero("");
            adaptador.notifyDataSetChanged();
        } else if (id == R.id.nav_epico) {
            adaptador.setGenero(Libro.G_EPICO);
            adaptador.notifyDataSetChanged();
        } else if (id == R.id.nav_XIX) {
            adaptador.setGenero(Libro.G_S_XIX);
            adaptador.notifyDataSetChanged();
        } else if(id == R.id.nav_suspense){
            adaptador.setGenero(Libro.G_SUSPENSE);
            adaptador.notifyDataSetChanged();
        } else if (id == R.id.nav_preferencias){
            abrePreferencias();
        } else if(id == R.id.nav_compartir){
            Toast.makeText(this, "share elemento:", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
//            Toast.makeText(this, "Preferencias", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(this, PreferenciasActivity.class);
//            startActivity(i);
            abrePreferencias();
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

    public void mostrarElementos(boolean mostrar){
        appBarLayout.setExpanded(mostrar);
        toggle.setDrawerIndicatorEnabled(mostrar);

        if(mostrar){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            tabs.setVisibility(View.VISIBLE);
        } else{
            tabs.setVisibility(View.GONE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void irUltimoVisitado(){
        SharedPreferences pref = getSharedPreferences(
                "com.rafaels.audiolibros_internal", MODE_PRIVATE);
        int id = pref.getInt("ultimo", -1);
        if(id >= 0){
            mostrarDetalle(id);
        } else{
            Toast.makeText(this, "Sin ultima visita", Toast.LENGTH_LONG).show();
        }
    }

    public void abrePreferencias() {
        int idContenedor = (findViewById(R.id.contenedor_pequeno) != null) ?
                R.id.contenedor_pequeno : R.id.contenedor_izquierdo;
        PreferenciasFragment prefFragment = new PreferenciasFragment();
        getFragmentManager().beginTransaction()
                .replace(idContenedor, prefFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
