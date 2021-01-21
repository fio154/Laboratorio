package isi.dam.sendmeal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import model.Plato;

import static isi.dam.sendmeal.R.menu.menu;

public class HomeActivity extends AppCompatActivity {

    Button registrarme, lista_items, crear_items;
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    private AppCompatActivity act= this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Iniciar Session como usuario anónimo
        signInAnonymously();

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

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Exito
                            Log.d("info", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // Error
                            Log.w("info", "signInAnonymously:failure", task.getException());
                            Toast.makeText(act, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
