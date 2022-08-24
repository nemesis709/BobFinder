package edu.skku.testapp.bobfinder;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;


public class openmap extends AppCompatActivity implements OnMapReadyCallback {

    String result_loc;
    String result_name;
    double Lat;
    double Lng;
    double MYLAT;
    double MYLNG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_openmap);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            Intent value=getIntent();
            result_loc=value.getStringExtra("LOC");
            result_name=value.getStringExtra("Store");
            MYLAT=value.getDoubleExtra("MYLAT",0);
            MYLNG=value.getDoubleExtra("MYLNG",0);
            Log.d("location",result_loc);
            try {
                JSONObject json = new JSONObject(result_loc);
                Lat=(double) json.get("lat");
                Lng=(double) json.get("lng");
                Log.d("lat", String.valueOf(Lat));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        LatLng Restaurant = new LatLng(Lat, Lng);
        LatLng MyLOC = new LatLng(MYLAT, MYLNG);
        Log.d("res loc", String.valueOf(Lat));
        Log.d("myloc", String.valueOf(MYLAT));

        MarkerOptions Food = new MarkerOptions().position(Restaurant).title(result_name);
        MarkerOptions Iam = new MarkerOptions().position(MyLOC).title("내위치");

        googleMap.addMarker(Food);
        googleMap.addMarker(Iam);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Restaurant, 15));

    }

}

