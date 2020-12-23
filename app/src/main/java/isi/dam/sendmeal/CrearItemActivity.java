package isi.dam.sendmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import bdd.AppRepositoryPlato;
import model.Plato;
import retrofit.PlatoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearItemActivity extends AppCompatActivity implements AppRepositoryPlato.OnResultCallback {

    EditText titulo, descripcion,precio, calorias;
    Toolbar toolbar;
    Button guardar;

    AppRepositoryPlato repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_item);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titulo = (EditText) findViewById(R.id.titulo);
        descripcion = (EditText) findViewById(R.id.descripcion_crear);
        precio = (EditText) findViewById(R.id.precio_pedido);
        calorias = (EditText) findViewById(R.id.calorias);
        guardar = (Button) findViewById(R.id.btn_guardar);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarDatos()) {
                    crearPlato();
                }
            }
        });

        // BASE DE DATOS
        repository = new AppRepositoryPlato(this.getApplication(), (AppRepositoryPlato.OnResultCallback) this);

    }

    public boolean validarDatos() {
        boolean validos = true;

        if(titulo.getText().toString().isEmpty()) {
            validos = false;
            titulo.setError("Campo obligatorio");
        } else if(platoRepetido(titulo.getText().toString())){
            validos = false;
            titulo.setError("El plato ya existe");
        }
        if(descripcion.getText().toString().isEmpty()) {
            validos = false;
            descripcion.setError("Campo obligatorio");
        }
        if(precio.getText().toString().isEmpty()) {
            validos = false;
            precio.setError("Campo obligatorio");
        }
        if(calorias.getText().toString().isEmpty()) {
            validos = false;
            calorias.setError("Campo obligatorio");
        }


        return validos;
    }

    public void crearPlato() {
        String titulo_str = titulo.getText().toString();
        String descripcion_str = descripcion.getText().toString();
        Double precio_db = Double.parseDouble(precio.getText().toString());
        Integer calorias_int = Integer.parseInt(calorias.getText().toString());

        Plato plato = new Plato();
        plato.setTitulo(titulo_str);
        plato.setDescripcion(descripcion_str);
        plato.setPrecio(precio_db);
        plato.setCalorias(calorias_int);

        Plato.lista_platos.add(plato);

        repository.insertar(plato);
        repository.buscarTodos();

        String url = "http://192.168.0.16:3001/";

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                // En la siguiente linea, le especificamos a Retrofit que tiene que usar Gson para deserializar nuestros objetos
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        PlatoService platoService = retrofit.create(PlatoService.class);

        Call<List<Plato>> callPlatos = platoService.getPlatoList();

        callPlatos.enqueue(
                new Callback<List<Plato>>() {
                    @Override
                    public void onResponse(Call<List<Plato>> call, Response<List<Plato>> response) {
                        if (response.code() == 200) {
                            System.out.println("exitooo");
                            Log.d("DEBUG", "Returno Exitoso");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Plato>> call, Throwable t) {
                        System.out.println("falloooo");
                        Log.d("DEBUG", "Returno Fallido");
                    }
                }
        );

        Toast.makeText(this, "¡Se ha registrado el plato con exito!", Toast.LENGTH_SHORT).show();

        titulo.setText(null);
        descripcion.setText(null);
        precio.setText(null);
        calorias.setText(null);
    }

    public boolean platoRepetido(String plato){
        plato = plato.toLowerCase();
        for(int i=0; i<Plato.lista_platos.size(); i++){
            String plato2 = Plato.lista_platos.get(i).getTitulo().toLowerCase();
            if(plato.equals(plato2)) return true;
        }
        return false;
    }

    @Override
    public void onResult(List result) {
        Toast.makeText(this, "Exito!", Toast.LENGTH_SHORT).show();
        for(Object p : result) {
            Plato pl = (Plato) p;
            Log.i("Plato: ", pl.getTitulo());
        }
    }
}