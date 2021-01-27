package isi.dam.sendmeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import model.Plato;

public class CrearItemActivity extends AppCompatActivity {

    EditText titulo, descripcion,precio, calorias;
    Toolbar toolbar;
    Button guardar;
    Button capturarImagen;
    Button buscarGaleria;
    static final int CAMARA_REQUEST = 1;
    static final int GALERIA_REQUEST = 2;
    ImageView imgView;
    private StorageReference storageRef;
    private StorageReference platosImagesRef;
    private byte[] imagenAGuardar;
    Plato plato = new Plato();

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
        capturarImagen = (Button) findViewById(R.id.camara);
        imgView = (ImageView) findViewById(R.id.imageView);

        imgView.setVisibility(View.GONE);

        buscarGaleria = (Button) findViewById(R.id.galeria);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarDatos()) {
                    crearPlato();
                }
            }
        });
        capturarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              lanzarCamara();
            }
        });
        buscarGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });

        // Creamos una referencia a nuestro Storage
        storageRef = FirebaseStorage.getInstance().getReference();

        // Creamos una referencia a 'images/plato_id.jpg'
        StorageReference platosImagesRef = storageRef.child("images/plato_id.jpg");
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

        plato.setTitulo(titulo_str);
        plato.setDescripcion(descripcion_str);
        plato.setPrecio(precio_db);
        plato.setCalorias(calorias_int);

        platosImagesRef = storageRef.child("/images/"+titulo_str+".jpg");
        UploadTask uploadTask = platosImagesRef.putBytes(imagenAGuardar);

        // Registramos un listener para saber el resultado de la operación
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continuamos con la tarea para obtener la URL
                return platosImagesRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // URL de descarga del archivo
                    Uri downloadUri = task.getResult();
                    plato.setUrlFoto(downloadUri.toString());
                    System.out.println("URL: "+ plato.getUrlFoto() );
                } else {
                    // Fallo
                }
            }
        });

        Plato.lista_platos.add(plato);

        Toast.makeText(this, "¡Se ha registrado el plato con exito!", Toast.LENGTH_SHORT).show();

        titulo.setText(null);
        descripcion.setText(null);
        precio.setText(null);
        calorias.setText(null);
        imgView.setVisibility(View.GONE);
    }

    public boolean platoRepetido(String plato){

        plato = plato.toLowerCase();

        for(int i=0; i<Plato.lista_platos.size(); i++){

            String plato2 = Plato.lista_platos.get(i).getTitulo().toLowerCase();

            if(plato.equals(plato2)) return true;
        }

        return false;
    }

    private void lanzarCamara() {
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camaraIntent, CAMARA_REQUEST);
    }

    private void abrirGaleria() {
        Intent galeriaIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galeriaIntent, GALERIA_REQUEST);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == CAMARA_REQUEST || requestCode == GALERIA_REQUEST) && resultCode == RESULT_OK) {
            imgView.setVisibility(View.VISIBLE);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imgView.setImageBitmap(imageBitmap);
            imagenAGuardar = baos.toByteArray(); // Imagen en arreglo de bytes
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (requestCode == CAMARA_REQUEST) {

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imagenAGuardar = baos.toByteArray(); // Imagen en arreglo de bytes
                imgView.setVisibility(View.VISIBLE);
                imgView.setImageBitmap(imageBitmap);

            }
            else if(requestCode == GALERIA_REQUEST) {
                Uri selectedImage = data.getData();
                InputStream is;
                try {
                    is = getContentResolver().openInputStream(selectedImage);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    Bitmap imageBitmap = BitmapFactory.decodeStream(bis);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);


                    imagenAGuardar = baos.toByteArray(); // Imagen en arreglo de bytes

                    imgView.setImageBitmap(imageBitmap);
                    imgView.setVisibility(View.VISIBLE);


                } catch (FileNotFoundException e) {}
            }
        }

    }

}