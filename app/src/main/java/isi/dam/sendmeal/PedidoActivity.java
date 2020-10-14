package isi.dam.sendmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;

import model.CuentaBancaria;
import model.Plato;
import model.Tarjeta;
import model.Usuario;

public class PedidoActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<String> listaNombres, listaPrecios;
    RecyclerView recycler;
    Button agregarPlato;
    ArrayList<Plato> listaPlatosPedidos;
    static final int REQUEST_CODE = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        final Intent siguienteListaItems = new Intent(this, ListaItemsActivity.class);

        agregarPlato = (Button) findViewById(R.id.agregarPlato_btn);

        agregarPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pantalla = "pedido";
                siguienteListaItems.putExtra("pantalla",pantalla);
                startActivityForResult(siguienteListaItems, REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int request_code, int result_code, Intent data) {
        super.onActivityResult(request_code, result_code, data);

        if(request_code == REQUEST_CODE){
            if(result_code == RESULT_OK){
                ArrayList<Plato> listaPlatos = data.getParcelableArrayListExtra("listaPlatos");

                recycler = (RecyclerView) findViewById(R.id.recycler_pedido);
                recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

                listaNombres = new ArrayList<String>();
                listaPrecios = new ArrayList<String>();

                for(int i = 0; i< listaPlatos.size(); i++) {
                    listaNombres.add(listaPlatos.get(i).getTitulo());
                    listaPrecios.add("$"+(listaPlatos.get(i).getPrecio()).toString());
                }

                AdapterDatosPedido adapter = new AdapterDatosPedido(listaNombres, listaPrecios);
                recycler.setAdapter(adapter);

            }else if(result_code == RESULT_CANCELED){
                System.out.println("NO FUNCIONO");
            }
        }
    }



}