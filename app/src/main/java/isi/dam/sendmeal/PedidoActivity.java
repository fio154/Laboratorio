package isi.dam.sendmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import model.CuentaBancaria;
import model.Plato;
import model.Tarjeta;
import model.Usuario;

public class PedidoActivity extends AppCompatActivity {

    Context contexto = this;
    Toolbar toolbar;
    ArrayList<String> listaNombres, listaPrecios, listaCantidades;
    ArrayList<Double> listaPrecios_double;
    RecyclerView recycler;
    Button agregarPlato, confirmarPedido;
    ArrayList<Plato> listaPlatosPedidos;
    TextView total;
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

        confirmarPedido = (Button) findViewById(R.id.confirmarPedido_btn);
        confirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              new Task1().execute("");
            }
        });

        total = (TextView) findViewById(R.id.total);

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
                listaCantidades = new ArrayList<String>();
                listaPrecios_double = new ArrayList<Double>();

                for(int i = 0; i < listaPlatos.size(); i++) {
                    int aux = 1;
                    int j = 0;
                    for(j = i; j < listaPlatos.size() - 1; j++) {
                        if (listaPlatos.get(j).getTitulo().equals(listaPlatos.get(j+1).getTitulo())) {
                            aux += 1;
                        }
                    }
                    i = j;
                    listaCantidades.add(Integer.toString(aux));
                    listaNombres.add(listaPlatos.get(i).getTitulo());
                    listaPrecios_double.add(listaPlatos.get(i).getPrecio());
                    listaPrecios.add("$"+(listaPlatos.get(i).getPrecio()).toString());
                }

                AdapterDatosPedido adapter = new AdapterDatosPedido(listaCantidades, listaNombres, listaPrecios);
                recycler.setAdapter(adapter);

                Double total_aux = 0.0;
                for(int j = 0; j < listaPrecios.size(); j++) {
                    total_aux += listaPrecios_double.get(j);
                };
                total.setText(String.valueOf(total_aux));

            }else if(result_code == RESULT_CANCELED){
                System.out.println("NO FUNCIONO");
            }
        }
    }

    class Task1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try{
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            Intent notificationIntent = new Intent(contexto, MyNotificationPublisher.class);
            contexto.sendBroadcast(notificationIntent);
        }
    }






}