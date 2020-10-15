package isi.dam.sendmeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import model.Plato;

import static isi.dam.sendmeal.R.menu.menu;

public class HomeActivity extends AppCompatActivity {

    Button registrarme, lista_items, crear_items;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        final Intent siguienteMain = new Intent(this, MainActivity.class);
        final Intent siguienteCrearItems = new Intent(this, CrearItemActivity.class);
        final Intent siguienteListaItems = new Intent(this, ListaItemsActivity.class);
        final Intent siguienteNuevoPedido = new Intent(this, PedidoActivity.class);

        switch(item.getItemId()){
            case R.id.registrame:
                startActivity(siguienteMain);
                break;
            case R.id.crear_item:
                startActivity(siguienteCrearItems);
                break;
            case R.id.lista_items:
                String pantalla = "home";
                siguienteListaItems.putExtra("pantalla", pantalla);
                startActivity(siguienteListaItems);
                break;
            case R.id.nuevo_pedido:
                AdapterDatosRecycler.listaPlatosPedidos = new ArrayList<Plato>();
                startActivity(siguienteNuevoPedido);
        }

        return super.onOptionsItemSelected(item);
    }

}
