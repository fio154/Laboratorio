package isi.dam.sendmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import bdd.AppRepositoryPlato;
import model.Plato;

public class ListaItemsActivity extends AppCompatActivity implements AppRepositoryPlato.OnResultCallback {

    Toolbar toolbar;
    ArrayList<String> listaNombres, listaPrecios, listaDescripciones, listaImagenes;
    ArrayList<StorageReference> listaStorage;
    RecyclerView recycler;
    String pantallaAnterior;
    Button confirmarPlato;
    AdapterDatosRecycler adapter;
    AppRepositoryPlato repository;
    Context context;
    List<Plato> platos;


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
        listaDescripciones = new ArrayList<String>();
        listaImagenes = new ArrayList<String>();
        listaStorage = new ArrayList<StorageReference>();

        Bundle extras = getIntent().getExtras();

        pantallaAnterior = extras.getString("pantalla");

        System.out.println("PLATOSSSS: "+Plato.lista_platos);

        for (int i = 0; i < Plato.lista_platos.size(); i++) {
            listaNombres.add(Plato.lista_platos.get(i).getTitulo());
            listaPrecios.add("Precio: $" + (Plato.lista_platos.get(i).getPrecio()).toString());
            listaDescripciones.add(Plato.lista_platos.get(i).getDescripcion());
            listaImagenes.add(Plato.lista_platos.get(i).getUrlFoto());
        }
        // BASE DE DATOS
        repository = new AppRepositoryPlato(this.getApplication(), this);
        repository.buscarTodos();

        final AdapterDatosRecycler adapter = new AdapterDatosRecycler(listaImagenes, listaNombres, listaPrecios, listaDescripciones, pantallaAnterior, new Dialog(this));
        recycler.setAdapter(adapter);
        context = this;

        confirmarPlato = (Button) findViewById(R.id.confirmarPedido_btn);

        if(pantallaAnterior.equals("home")){
            confirmarPlato.setVisibility(View.GONE);
        } else if(pantallaAnterior.equals("pedido")){
            confirmarPlato.setVisibility(View.VISIBLE);
        }

        confirmarPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<platos.size(); i++) {
                    for(int j=0; j < Integer.parseInt(adapter.listaCantidades.get(i).getText().toString()); j++) {
                        if(platos.get(i).getTitulo().equals(adapter.listaNombres.get(i))){
                            AdapterDatosRecycler.listaPlatosPedidos.add(platos.get(i));
                        }
                    }
                }
                Log.i("LISTA", AdapterDatosRecycler.listaPlatosPedidos.toString());
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("listaPlatos", AdapterDatosRecycler.listaPlatosPedidos);
                setResult(RESULT_OK, intent);
                finish();
                }
            });
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        final Intent anteriorHome = new Intent(this, HomeActivity.class);
        final Intent anteriorPedido = new Intent(this, PedidoActivity.class);

        if(pantallaAnterior.equals("home")){
             startActivity(anteriorHome);
        }else if(pantallaAnterior.equals("pedido")){
             startActivity(anteriorPedido);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResult(List result) {
        //Toast.makeText(this, "Exito!", Toast.LENGTH_SHORT).show();

        platos = (List<Plato>) result;

        for (int i = 0; i < platos.size(); i++) {
            listaNombres.add(platos.get(i).getTitulo());
            System.out.println("nombreeee: " + platos.get(i).getTitulo());
            listaPrecios.add("Precio: $" + (platos.get(i).getPrecio()).toString());
            listaDescripciones.add(platos.get(i).getDescripcion());
        }

        adapter = new AdapterDatosRecycler(listaNombres, listaPrecios, listaDescripciones, pantallaAnterior, new Dialog(context));
        recycler.setAdapter(adapter);
    }
}