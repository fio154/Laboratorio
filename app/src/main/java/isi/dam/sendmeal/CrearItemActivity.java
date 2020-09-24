package isi.dam.sendmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import model.Plato;

public class CrearItemActivity extends AppCompatActivity {

    EditText titulo, descripcion,precio, calorias;
    Toolbar toolbar;
    Button guardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_item);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titulo = (EditText) findViewById(R.id.titulo);
        descripcion = (EditText) findViewById(R.id.descripcion);
        precio = (EditText) findViewById(R.id.precio);
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

        Toast.makeText(this, "¡Se ha registrado el plato con exito!", Toast.LENGTH_SHORT).show();

        titulo.setText(null);
        descripcion.setText(null);
        precio.setText(null);
        calorias.setText(null);
    }

    public boolean platoRepetido(String plato){

        plato = plato.toLowerCase();

        for(int i=0; i<Plato.lista_platos.size(); i++){

            String plato2 = Plato.lista_platos.get(i).toString().toLowerCase();

            if(plato.equals(plato2))                return true;
        }

        return false;
    }
}