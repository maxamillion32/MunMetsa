package com.example.asus.munmestsa0_1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.munmestsa0_1.DB.DatabaseHelper;
import com.example.asus.munmestsa0_1.model.Metsa;
import com.example.asus.munmestsa0_1.model.Note;
import com.example.asus.munmestsa0_1.model.Receipt;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MetsaAddActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Metsa metsa = null;

    private FirebaseDatabase database;
    private DatabaseReference metsat;

    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metsa_add);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myDb = new DatabaseHelper(this);

        database = FirebaseDatabase.getInstance();
        metsat = database.getReference();
        metsa= new Metsa();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng finland = new LatLng(60, 25);
        mMap.addMarker(new MarkerOptions().position(finland).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(finland));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(4));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                metsa.setLatitude(latLng.latitude);
                metsa.setLongitude(latLng.longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title("Koodinaatit"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });


    }


    public void onSearch(View v) throws IOException {
        mMap.clear();
        EditText location_tf = (EditText) findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<android.location.Address> addressList = null;
        if(mMap!=null || !location.equals("")){
            Geocoder geocoder = new Geocoder(this);
            addressList = geocoder.getFromLocationName(location,1);
        }
        android.location.Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

    }

    public void changeType(View view){
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL){
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }else{
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    public void metsaReady(View view) {
        EditText title = (EditText) findViewById(R.id.fTitle);
        EditText size = (EditText) findViewById(R.id.fSize);


        if (metsa.getLatitude() != 0 && !title.getText().toString().matches("") && size.getText().toString().trim().length() > 0) {
            metsa.setTitle(title.getText().toString());
            metsa.setSize(Integer.parseInt(size.getText().toString()));
            metsa.setDate(new Date());
            metsa.setDescription("-");
            metsa.setNotes(new HashMap<String, Note>());
            metsa.setReceipts(new HashMap<String, Receipt>());

            String key = metsat.child("metsat").push().getKey();
            metsa.setId(key);

            metsat.child("metsat").child(key).setValue(metsa);

            boolean isInserted = myDb.insertData(metsa.getId());

            Toast.makeText(getApplicationContext(), "Metsä lisätty avaimella " + metsa.getId(), Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, MainController.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Täytä nimi ja koko kenttä sekä valitse kartalta sijainti. ", Toast.LENGTH_LONG).show();
        }
    }

}
