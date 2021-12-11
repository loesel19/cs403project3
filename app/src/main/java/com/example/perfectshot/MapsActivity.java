package com.example.perfectshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.perfectshot.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapClickListener {

    final int REQUEST_LOCATION_PERMISSION = 1;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LatLng position;
    private String post_desc;
    private boolean inCreation;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intent = getIntent();
        float lat = intent.getFloatExtra("lat",43.4195f);
        float lon = intent.getFloatExtra("lon",-83.9508f);
        post_desc = intent.getStringExtra("desc");
        inCreation = intent.getBooleanExtra("create",false);
        position = new LatLng(lat,lon);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //if loading a post, create a marker
        if(post_desc != null){
            mMap.addMarker(new MarkerOptions().position(position).title(post_desc));
        }
        //otherwise just load on map on the position, (defaults to mi)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,7));
        //Enable location button
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        //set MapClickListener
        if(inCreation) {
            mMap.setOnMapClickListener(this);
        }
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Log.d("MapDebug", "Lat: " + latLng.latitude + "Lon: " + latLng.longitude);
        Marker postLocation = mMap.addMarker(new MarkerOptions().position(latLng));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Would you like to select this location for your post?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent data = new Intent();
                Log.d("MapDebug", "Lat: " + latLng.latitude + "Lon: " + latLng.longitude);
                data.putExtra("location",latLng);
                setResult(RESULT_OK,data);
                finish();
            }
        });
        alertDialog.setNegativeButton("No",(dialogInterface, id) -> {
            if (postLocation != null) {
                postLocation.remove();
            }
        });
        alertDialog.create().show();
    }
}