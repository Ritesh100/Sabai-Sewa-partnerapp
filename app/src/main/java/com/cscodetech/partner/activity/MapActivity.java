package com.cscodetech.partner.activity;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cscodetech.partner.R;
import com.cscodetech.partner.directionhelpers.FetchURL;
import com.cscodetech.partner.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private MarkerOptions place1;
    private MarkerOptions  place2;
    Button getDirection;
    private Polyline currentPolyline;
    private FusedLocationProviderClient fusedLocationProviderClient;
    Double lat=0.0;
    Double longs=0.0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Route For customer");
        fusedLocationProviderClient = getFusedLocationProviderClient(this);
        lat=getIntent().getDoubleExtra("lat",0.0);
        longs=getIntent().getDoubleExtra("longs",0.0);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        Log.d("mylog", "Added Markers");

            @SuppressLint("MissingPermission")
            Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
            lastLocation.addOnSuccessListener(this, location -> {
                if (location != null) {

                    Log.e("lat","-->"+location.getLatitude());
                    Log.e("lot","-->"+location.getLongitude());
                    place1 = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Location 1").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map));
                    place2 = new MarkerOptions().position(new LatLng(lat, longs)).title("Location 2");
                    new FetchURL(MapActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
                    LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 17);
                    mMap.animateCamera(yourLocation);
                    mMap.addMarker(place1);
                    mMap.addMarker(place2);

                } else {
                    //Gps not enabled if loc is null

                    Toast.makeText(MapActivity.this, "Location not Available", Toast.LENGTH_SHORT).show();

                }
            });
            lastLocation.addOnFailureListener(e -> Toast.makeText(MapActivity.this, "Location Not Availabe", Toast.LENGTH_SHORT).show());



    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String strDest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = strOrigin + "&" + strDest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.api_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }



}