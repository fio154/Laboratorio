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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import bdd.AppRepositoryPlato;
import bdd.AppRepositoryPedido;
import model.Pedido;
import model.Plato;
import retrofit.PedidoService;
import retrofit.PlatoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PedidoActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, AppRepositoryPedido.OnResultCallback {

    Context contexto = this;
    Toolbar toolbar;
    ArrayList<String> listaNombres, listaPrecios, listaCantidades;
    ArrayList<Double> listaPrecios_double;
    RecyclerView recycler;
    Button agregarPlato, confirmarPedido, ubicacion;
    TextView total, email, direccion;
    RadioButton envioDomicilio, takeAway;
    static final int REQUEST_CODE = 222;
    static final int REQUEST_CODE_MAPS = 111;
    AdapterDatosPedido adapterPedido;
    AppRepositoryPedido repositoryPedido;

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
                AdapterDatosRecycler.listaPlatosPedidos.clear();
            }
        });

        repositoryPedido = new AppRepositoryPedido(this.getApplication(), (AppRepositoryPedido.OnResultCallback) this);

        confirmarPedido = (Button) findViewById(R.id.confirmarPedido_btn);
        confirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar()) {
                    new Task1().execute("");
                }
            }
        });


        final Intent ubicacionMapa = new Intent(this, MapActivity.class);

        ubicacion = (Button) findViewById(R.id.ubicacion);
        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(ubicacionMapa, REQUEST_CODE_MAPS);
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
                AdapterDatosRecycler.listaPlatosPedidos = listaPlatos;
                Log.i("CANTIDAD", listaCantidades.toString());
                Log.i("NOMBRES", listaNombres.toString());
                Log.i("Precios", listaPrecios.toString());
                Log.i("Precios Double", listaPrecios_double.toString());
                adapterPedido = new AdapterDatosPedido(listaCantidades, listaNombres, listaPrecios, listaPrecios_double, total);
                recycler.setAdapter(adapterPedido);

                ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, PedidoActivity.this);
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

        if(request_code == REQUEST_CODE_MAPS) {
            if (result_code == RESULT_OK) {

            }else if(result_code == RESULT_CANCELED){
                System.out.println("NO FUNCIONO");
            }
        }
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        adapterPedido.removeItem(viewHolder.getAdapterPosition());
    }

    @Override
    public void onResult(List result) {
        Toast.makeText(this, "Exito!", Toast.LENGTH_SHORT).show();
        for(Object p : result) {
            Pedido ped = (Pedido) p;
            Log.i("Platos del pedido: ", ped.getPlatos().toString());
            Log.i("Mail del pedido: ", ped.getEmail());
        }
    }

    class Task1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try{
                Thread.sleep(2000);
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

                Log.i("holis", nuevoPedido.getPlatos().toString());
                repositoryPedido.insertar(nuevoPedido);
                repositoryPedido.buscarTodos();

                String url = "http://192.168.0.16:3001/";

                Gson gson = new GsonBuilder().setLenient().create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        // En la siguiente linea, le especificamos a Retrofit que tiene que usar Gson para deserializar nuestros objetos
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                PedidoService pedidoService = retrofit.create(PedidoService.class);

                pedidoService.createPedido(nuevoPedido);

                Call<List<Pedido>> callPedidos = pedidoService.getPedidoList();

                callPedidos.enqueue(
                        new Callback<List<Pedido>>() {
                            @Override
                            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                                if (response.code() == 200) {
                                    System.out.println("exitooo");
                                    Log.d("DEBUG", "Returno Exitoso");
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                                System.out.println("falloooo");
                                Log.d("DEBUG", "Returno Fallido");
                            }
                        }
                );

                AdapterDatosRecycler.listaPlatosPedidos.clear();

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