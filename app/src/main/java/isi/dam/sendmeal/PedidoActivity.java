package isi.dam.sendmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import model.Plato;

public class PedidoActivity extends AppCompatActivity {

    ArrayList<String> listaNombres, listaPrecios;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        recycler = (RecyclerView) findViewById(R.id.recycler_pedido);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        listaNombres = new ArrayList<String>();
        listaPrecios = new ArrayList<String>();

        for(int i = 0; i< Plato.lista_platos.size(); i++) {
            listaNombres.add(Plato.lista_platos.get(i).getTitulo());
            listaPrecios.add("$"+(Plato.lista_platos.get(i).getPrecio()).toString());
        }

        AdapterDatosPedido adapter = new AdapterDatosPedido(listaNombres, listaPrecios);
        recycler.setAdapter(adapter);

    }
}