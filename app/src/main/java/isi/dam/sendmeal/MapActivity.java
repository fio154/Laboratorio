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
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import model.CuentaBancaria;
import model.Tarjeta;
import model.Usuario;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LocationManager locationManager;
    Button confirmar_ubicacion;
    TextView latitud, longitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                String latitud_str =  "";
                String longitud_str =  "";
                intent.putExtra("latitud", latitud_str);
                intent.putExtra("longitud", longitud_str);
                //fijarse si vamos a mandar latitud y longitud, o directamente armamos la dirección
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

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                MarkerOptions marcador = new MarkerOptions()
                        .alpha(0.5f)
                        .position(latLng)
                        .draggable(true)
                        .title("Mi ubicación")
                        .snippet("Subtitulo")
                        .icon(BitmapDescriptorFactory .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mMap.addMarker(marcador);
            }
        });

        System.out.println("LLEGO MAPA");
    }



}


