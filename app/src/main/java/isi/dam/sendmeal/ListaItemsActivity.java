package isi.dam.sendmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import model.Plato;

public class ListaItemsActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<String> listaNombres, listaPrecios;
    ArrayList<Plato> listaPlatosPedidos;
    RecyclerView recycler;
    String pantallaAnterior;
    Button confirmarPlato, pedir;
    TextView plato;

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

        Bundle extras = getIntent().getExtras();

        pantallaAnterior = extras.getString("pantalla");

        for (int i = 0; i < Plato.lista_platos.size(); i++) {
            listaNombres.add(Plato.lista_platos.get(i).getTitulo());
            listaPrecios.add("Precio: $" + (Plato.lista_platos.get(i).getPrecio()).toString());
        }

        AdapterDatosRecycler adapter = new AdapterDatosRecycler(listaNombres, listaPrecios, pantallaAnterior);
        recycler.setAdapter(adapter);

        confirmarPlato = (Button) findViewById(R.id.confirmarPlato_btn);

        if(pantallaAnterior.equals("home")){
            confirmarPlato.setVisibility(View.GONE);
        } else if(pantallaAnterior.equals("pedido")){
            confirmarPlato.setVisibility(View.VISIBLE);
        }


        confirmarPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mandar info del plato pedido
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


}