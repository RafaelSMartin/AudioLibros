package com.rafaels.audiolibros.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rafaels.audiolibros.adaptador.AdaptadorLibros;
import com.rafaels.audiolibros.Aplicacion;
import com.rafaels.audiolibros.adaptador.AdaptadorLibrosFiltro;
import com.rafaels.audiolibros.adaptador.Libro;
import com.rafaels.audiolibros.MainActivity;
import com.rafaels.audiolibros.R;

import java.util.List;

/**
 * Created by Rafael S Martin on 26/01/2018.
 */

public class SelectorFragment extends Fragment {

    private Activity actividad;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorLibrosFiltro adaptador;
    private List<Libro> listaLibros;

    @Override
    public void onAttach(Activity actividad){
        super.onAttach(actividad);
        this.actividad = actividad;

        Aplicacion app = (Aplicacion) actividad.getApplication();
        adaptador = app.getAdaptador();
        listaLibros = app.getListaLibros();
    }

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor,
                             Bundle savedInstanceState){
        View vista = inflador.inflate(R.layout.fragment_selector, contenedor, false);

        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) vista.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(layoutManager);

        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(actividad, "Seleccionado Elemento: "
//                        + recyclerView.getChildAdapterPosition(v),
//                        Toast.LENGTH_SHORT).show();
                ((MainActivity) actividad).mostrarDetalle((int) adaptador.getItemId(
                        recyclerView.getChildAdapterPosition(v)));
            }
        });

        adaptador.setOnItemLongClickListener(new View.OnLongClickListener(){
           public boolean onLongClick(final View v){
               final int id = recyclerView.getChildAdapterPosition(v);
               AlertDialog.Builder menu = new AlertDialog.Builder(actividad);
               CharSequence[] opciones = { "Compartir", "Borrar", "Insertar" };
               menu.setItems(opciones, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int opcion) {
                       switch (opcion){
                           case 0: //Compartir
                               Libro libro = listaLibros.get(id);
                               Intent i = new Intent(Intent.ACTION_SEND);
                               i.setType("text/plain");
                               i.putExtra(Intent.EXTRA_SUBJECT, libro.titulo);
                               i.putExtra(Intent.EXTRA_TEXT, libro.urlAudio);
                               startActivity(Intent.createChooser(i, "Compartir"));
                               break;
                           case 1: //Borrar
                               Snackbar.make(v, "¿Estas seguro?", Snackbar.LENGTH_LONG)
                                       .setAction("SI", new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               adaptador.borrar(id);
                                               adaptador.notifyDataSetChanged();
                                           }
                                       }).show();
                               break;
                           case 2: //Insertar
                               int position = recyclerView.getChildLayoutPosition(v);
                               adaptador.insertar((Libro)adaptador.getItem(position));
                               adaptador.notifyDataSetChanged();
                               Snackbar.make(v, "Libro insertado", Snackbar.LENGTH_INDEFINITE)
                                       .show();
                               break;
                       }
                   }
               });
               menu.create().show();
               return true;
           }
        });
        return vista;
    }

    @Override
    public void onResume(){
        ((MainActivity)getActivity()).mostrarElementos(true);
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_selector, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_buscar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextChange(String query) {
                        adaptador.setBusqueda(query);
                        adaptador.notifyDataSetChanged();
                        return false;
                    }
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                });
        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adaptador.setBusqueda("");
                adaptador.notifyDataSetChanged();
                return true; // Para permitir cierre
            }
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true; // Para permitir expansión
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.menu_ultimo){
            ((MainActivity)actividad).irUltimoVisitado();
            return true;
        } else if(id == R.id.menu_buscar){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
