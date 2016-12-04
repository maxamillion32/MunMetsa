package com.example.asus.munmestsa0_1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.munmestsa0_1.DB.DatabaseHelper;
import com.example.asus.munmestsa0_1.adapter.MetsaAdapter;
import com.example.asus.munmestsa0_1.adapter.ViewPagerAdapter;
import com.example.asus.munmestsa0_1.fragments.HomeFragment;
import com.example.asus.munmestsa0_1.fragments.ThreeFragment;
import com.example.asus.munmestsa0_1.fragments.TwoFragment;
import com.example.asus.munmestsa0_1.model.Metsa;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainController extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    private FirebaseDatabase database;
    private DatabaseReference fbRef;

    private DatabaseHelper localDB;

    private HashMap<String, Metsa> metsaMap;
    private HashMap<String, List<String>> metsaKeyMap;

    private ExpandableListView expandableListView;

    private Metsa currentMetsa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_controller);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new HomeFragment(), "Home");viewPagerAdapter.addFragments(new TwoFragment(), "Two");viewPagerAdapter.addFragments(new ThreeFragment(), "Three");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        metsaMap = new HashMap<>();
        currentMetsa = null;

        expandableListView = (ExpandableListView) findViewById(R.id.metsaList);

        database = FirebaseDatabase.getInstance();
        fbRef = database.getReference("metsat");
        localDB = new DatabaseHelper(this);

        metsaKeyMap = new HashMap<>();




        fbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cursor res = localDB.getAllData();
                if (res.getCount() == 0) {
                    return;
                }
                String metsaId = null;
                Metsa m = null;
                while (res.moveToNext()) {
                    metsaId = res.getString(1);
                    m = dataSnapshot.child(metsaId).getValue(Metsa.class);
                    metsaMap.put(m.getTitle(), m);
                }
                currentMetsa = metsaMap.get(m.getTitle());
                refresh();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (position == 0) {
                    showMap(true);
                } else {
                    showMap(false);
                }
            }
        });

    }

    public void showMap(boolean t) {
        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        if (t) {
            params.height = ViewPager.LayoutParams.MATCH_PARENT;
        } else {
            params.height = 0;
        }
        mapFragment.getView().setLayoutParams(params);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void listItems(){
        ArrayList<String> titles = new ArrayList<>();
        for(Metsa s : this.metsaMap.values()){
            if(s.getTitle()!=currentMetsa.getTitle())
                titles.add(s.getTitle());
        }
        metsaKeyMap.put(currentMetsa.getTitle(), titles);

        ArrayList<String> t = new ArrayList<>();
        t.add(currentMetsa.getTitle());
        ExpandableListAdapter metsaAdapter = new MetsaAdapter(this, t,metsaKeyMap, metsaMap);
        expandableListView.setAdapter(metsaAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int res_id = item.getItemId();
        if(res_id==R.id.action_add){
            Intent intent = new Intent(this, MetsaAddActivity.class);
            startActivity(intent);

        }else if(res_id==R.id.menu_main){

        }
        return true;
    }
    public void refresh(){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentMetsa.getLatitude(), currentMetsa.getLongitude()), 15));
        listItems();
    }

    public void childClicked(View v){
        String title = ((TextView)v.findViewById(R.id.child_item)).getText().toString();
        currentMetsa = metsaMap.get(title);
        refresh();
        Toast.makeText(getApplicationContext(),title + " valittu", Toast.LENGTH_SHORT).show();
    }
    public void removeCurrent(View v){

    }

}
