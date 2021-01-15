package isi.dam.sendmeal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import model.CuentaBancaria;
import model.Tarjeta;
import model.Usuario;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private ArrayList<Marker> marcadores ;
    private ArrayList<Polyline> recorridos ;
    private LatLng ubicacion;
    private LatLng ubicacionSendMeal;
    Button confirmar_ubicacion;
    TextView latitud, longitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        marcadores = new ArrayList<Marker>();
        recorridos = new ArrayList<Polyline>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        confirmar_ubicacion = (Button) findViewById(R.id.confirmar_ubicacion);
        latitud = (TextView) findViewById(R.id.Latitud);
        longitud = (TextView) findViewById(R.id.Longitud);

        confirmar_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("latitud", ubicacion.latitude);
                intent.putExtra("longitud", ubicacion.longitude);

                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    9999);
            return;
        }
        configurarMapa();
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==9999 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            configurarMapa();
            System.out.println("LLEGO MAPA");
        }

    }

    @SuppressLint("MissingPermission")
    public void configurarMapa(){
        mMap.setMyLocationEnabled(true);
        LatLng utn = new LatLng(-31.6168, -60.6752);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(utn,15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                latitud.setText("Latitud: "+marker.getPosition().latitude);
                longitud.setText("Longitud: "+marker.getPosition().longitude);
                ubicacion = marker.getPosition();
                return false;
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker)  {
                latitud.setText("Latitud: "+marker.getPosition().latitude);
                longitud.setText("Longitud: "+marker.getPosition().longitude);
                if (!recorridos.isEmpty()){
                    recorridos.get(0).remove();
                    recorridos.clear();
                }
                //Generamos el recorrido entre la ubicación Send Meal y la del cliente
                PolylineOptions polylineOptions = new PolylineOptions()
                        .color(0x66FF0000)
                        .add(ubicacionSendMeal)
                        .add(marker.getPosition());

                Polyline linea = mMap.addPolyline(polylineOptions);
                recorridos.add(linea);

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                latitud.setText("Latitud: "+marker.getPosition().latitude);
                longitud.setText("Longitud: "+marker.getPosition().longitude);
                ubicacion = marker.getPosition();

                if (!recorridos.isEmpty()){
                    recorridos.get(0).remove();
                    recorridos.clear();
                }
                //Generamos el recorrido entre la ubicación Send Meal y la del cliente
                PolylineOptions polylineOptions = new PolylineOptions()
                        .color(0x66FF0000)
                        .add(ubicacionSendMeal)
                        .add(ubicacion);

                Polyline linea = mMap.addPolyline(polylineOptions);
                recorridos.add(linea);
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
               if (!marcadores.isEmpty()){
                 marcadores.get(0).remove();
                 marcadores.clear();
                }
                if (!recorridos.isEmpty()){
                    recorridos.get(0).remove();
                    recorridos.clear();
                }
                latitud.setText("Latitud: "+latLng.latitude);
                longitud.setText("Longitud: "+latLng.longitude);
                ubicacion = latLng;
                MarkerOptions marcador = new MarkerOptions()
                        .alpha(0.5f)
                        .position(latLng)
                        .draggable(true)
                        .title("Mi ubicación")
                        .snippet("Subtitulo")
                        .icon(BitmapDescriptorFactory .defaultMarker(BitmapDescriptorFactory.HUE_RED));
               Marker M =  mMap.addMarker(marcador);
                marcadores.add(M);

                //Generamos el recorrido entre la ubicación Send Meal y la del cliente
                PolylineOptions polylineOptions = new PolylineOptions()
                        .color(0x66FF0000)
                        .add(ubicacionSendMeal)
                        .add(ubicacion);

                Polyline linea = mMap.addPolyline(polylineOptions);
                recorridos.add(linea);
            }
        });
        //Generamos punto aleatorio para el local, aproximado por defecto a la facultad

        LatLng posicionOriginal = new LatLng(-31.61280,-60.69581); //sobre calle aristobulo, cercano a la UTN
        Random r = new Random();

        // Una direccion aleatoria de 0 a 359 grados
        int direccionRandomEnGrados = r.nextInt(360);

        // Una distancia aleatoria de 100 a 1000 metros
        int distanciaMinima = 100;
        int distanciaMaxima = 1000;
        int distanciaRandomEnMetros = r.nextInt(distanciaMaxima - distanciaMinima) + distanciaMinima;

        ubicacionSendMeal = SphericalUtil.computeOffset(
                posicionOriginal,
                distanciaRandomEnMetros,
                direccionRandomEnGrados
        );
        MarkerOptions marcadorSendMeal = new MarkerOptions()
                .alpha(0.5f)
                .position(ubicacionSendMeal)
                .draggable(true)
                .title("SendMeal")
                .snippet("Comidas Rápidas")
                .icon(BitmapDescriptorFactory .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        mMap.addMarker(marcadorSendMeal);

    }


}


