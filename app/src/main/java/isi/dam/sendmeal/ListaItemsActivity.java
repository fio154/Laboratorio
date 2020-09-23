package isi.dam.sendmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import model.Plato;

public class ListaItemsActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<String> listaNombres, listaPrecios;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_items);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        listaNombres = new ArrayList<String>();
        listaPrecios = new ArrayList<String>();

        for(int i=0; i<Plato.lista_platos.size(); i++) {
            listaNombres.add(Plato.lista_platos.get(i).getTitulo());
            listaPrecios.add((Plato.lista_platos.get(i).getPrecio()).toString() + "$");
        }

        AdapterDatosRecycler adapter = new AdapterDatosRecycler(listaNombres, listaPrecios);
        recycler.setAdapter(adapter);
    }


}