package isi.dam.sendmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.Pattern;

import model.Pedido;
import model.Plato;

public class PedidoActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    Context contexto = this;
    Toolbar toolbar;
    ArrayList<String> listaNombres, listaPrecios, listaCantidades;
    ArrayList<Double> listaPrecios_double;
    RecyclerView recycler;
    Button agregarPlato, confirmarPedido;
    TextView total, email, direccion;
    RadioButton envioDomicilio, takeAway;
    static final int REQUEST_CODE = 222;
    AdapterDatosPedido adapterPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = (TextView) findViewById(R.id.mail_pedido);
        direccion = (TextView) findViewById(R.id.direccion_pedido);
        envioDomicilio = (RadioButton) findViewById(R.id.envio_domicilio);
        takeAway = (RadioButton) findViewById(R.id.take_away);
        total = (TextView) findViewById(R.id.total);

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
                if(validar()) {
                    new Task1().execute("");
                }
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
                listaCantidades = new ArrayList<String>();
                listaPrecios_double = new ArrayList<Double>();


                for(int i = 0; i < listaPlatos.size(); i++) {
                    int contador = 0;
                    for(int j=0; j < listaPlatos.size(); j++) {
                        if (listaPlatos.get(i).getTitulo().equals(listaPlatos.get(j).getTitulo())) {
                            contador += 1;
                        }
                    }
                    if(!listaNombres.contains(listaPlatos.get(i).getTitulo())) {
                        listaCantidades.add(Integer.toString(contador));
                        listaNombres.add(listaPlatos.get(i).getTitulo());
                        listaPrecios_double.add(listaPlatos.get(i).getPrecio());
                        listaPrecios.add("$"+(listaPlatos.get(i).getPrecio()).toString());
                    }
                }

                Log.i("PLATOS", listaPlatos.toString());
                Log.i("CANTIDAD", listaCantidades.toString());
                Log.i("NOMBRES", listaNombres.toString());
                Log.i("Precios", listaPrecios.toString());
                Log.i("Precios Double", listaPrecios_double.toString());
                adapterPedido = new AdapterDatosPedido(listaCantidades, listaNombres, listaPrecios, listaPrecios_double, total);
                recycler.setAdapter(adapterPedido);

                ItemTouchHelper.SimpleCallback simpleCallback =
                        new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, PedidoActivity.this);

                new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler);


                Double total_aux = 0.0;
                for(int j = 0; j < listaPrecios.size(); j++) {
                    int cant = Integer.parseInt(listaCantidades.get(j));
                    total_aux += listaPrecios_double.get(j) * cant;
                };
                total.setText(String.valueOf(total_aux));

            } else if(result_code == RESULT_CANCELED){
                System.out.println("NO FUNCIONO");
            }
        }
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        adapterPedido.removeItem(viewHolder.getAdapterPosition());
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
                Pedido nuevoPedido = new Pedido();
                nuevoPedido.setPlatos(AdapterDatosRecycler.listaPlatosPedidos);
                AdapterDatosRecycler.listaPlatosPedidos.clear();

                nuevoPedido.setDireccion(direccion.getText().toString());
                nuevoPedido.setEmail(email.getText().toString());
                nuevoPedido.setParaEnviar(envioDomicilio.isSelected());

                Intent notificationIntent = new Intent(contexto, MyNotificationPublisher.class);
                contexto.sendBroadcast(notificationIntent);
                direccion.setText(null);
                email.setText(null);
                total.setText("0.0");
                envioDomicilio.setChecked(false);
                takeAway.setChecked(false);
                adapterPedido.listaNombres.clear();
                adapterPedido.listaCantidades.clear();
                adapterPedido.listaPrecios.clear();
                adapterPedido.notifyDataSetChanged();

        }
    }

    public boolean validar(){

        boolean retorno = true;
        String email_str = email.getText().toString();
        String direccion_str = direccion.getText().toString();
        String expresion = "[^@]*(@[A-Za-z]{3}[^@]*)+";

        if(email_str.isEmpty()){
            email.setError("E-mail es un campo obligatorio");
            retorno = false;
        }else if(!Pattern.matches(expresion,email_str)){
            email.setError("Debe contener al menos un @ y 3 letras después de este");
            retorno = false;
        }

        if(direccion_str.isEmpty()){
            direccion.setError("Dirección es un ampo obligatorio");
            retorno = false;
        }

        if(AdapterDatosRecycler.listaPlatosPedidos.isEmpty()){
            Toast.makeText(this, "Debe agregar al menos un plato", Toast.LENGTH_SHORT).show();
            retorno = false;
        }else if(!envioDomicilio.isChecked() && !takeAway.isChecked()){
            Toast.makeText(this, "Debe seleccionar una forma de envio", Toast.LENGTH_SHORT).show();
            retorno = false;
        }

        return retorno;
    }

}