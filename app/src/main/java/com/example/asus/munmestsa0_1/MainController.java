package com.example.asus.munmestsa0_1;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.munmestsa0_1.DB.DatabaseHelper;
import com.example.asus.munmestsa0_1.adapter.CustomNoteAdapter;
import com.example.asus.munmestsa0_1.adapter.MetsaAdapter;
import com.example.asus.munmestsa0_1.adapter.ViewPagerAdapter;
import com.example.asus.munmestsa0_1.fragments.HomeFragment;
import com.example.asus.munmestsa0_1.fragments.ThreeFragment;
import com.example.asus.munmestsa0_1.fragments.TwoFragment;
import com.example.asus.munmestsa0_1.model.Metsa;
import com.example.asus.munmestsa0_1.model.Note;
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
import java.util.Date;
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
    private DatabaseHelper localDB;
    private HashMap<String, Metsa> metsaMap;
    private HashMap<String, List<String>> metsaKeyMap;
    private ExpandableListView expandableListView;
    private Metsa currentMetsa;
    private HashMap<String, DatabaseReference> FBreferences;

    private HomeFragment homeFragment;
    private TwoFragment noteFragment;
    private ThreeFragment receiptFragment;

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

        homeFragment = new HomeFragment();noteFragment = new TwoFragment();receiptFragment = new ThreeFragment();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(homeFragment, "Kartta");
        viewPagerAdapter.addFragments(noteFragment, "Muistio");
        viewPagerAdapter.addFragments(receiptFragment, "Kuitit");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        metsaMap = new HashMap<>();
        currentMetsa = null;

        expandableListView = (ExpandableListView) findViewById(R.id.metsaList);

        database = FirebaseDatabase.getInstance();
        localDB = new DatabaseHelper(this);

        metsaKeyMap = new HashMap<>();

        FBreferences = new HashMap<>();
        Cursor res = localDB.getAllData();
        if (res.getCount() == 0) {return;}
        while (res.moveToNext()) {
            FBreferences.put(res.getString(1), database.getReference("metsat/"+res.getString(1)));
        }

        for(DatabaseReference r : FBreferences.values()){
            r.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Metsa m = dataSnapshot.getValue(Metsa.class);
                    metsaMap.put(m.getTitle(), m);
                    currentMetsa = m;
                    refresh();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
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
        viewNotes();
    }
    public void viewNotes(){
       if(currentMetsa.getNotes()!=null) {
           ArrayList l = new ArrayList<Note>(currentMetsa.getNotes().values());
           noteFragment.viewNotes(l);
        }else{
           noteFragment.notesEmpty();
       }
    }

    public void childClicked(View v){
        String title = ((TextView)v.findViewById(R.id.child_item)).getText().toString();
        currentMetsa = metsaMap.get(title);
        refresh();
        Toast.makeText(getApplicationContext(),title + " valittu", Toast.LENGTH_SHORT).show();
        viewNotes();
    }

    public void removeCurrent(View v){
    }

    public void addNote(View v){
        EditText noteText = (EditText) findViewById(R.id.noteText);
        if(noteText.getText().toString().length()>1){

            String key = FBreferences.get(currentMetsa.getId()).child("notes").push().getKey();
            Note newNote = new Note(noteText.getText().toString(), new Date(), key, currentMetsa.getId(), "Default");

            FBreferences.get(currentMetsa.getId()).child("notes").child(key).setValue(newNote);
            Toast.makeText(getApplicationContext(), "Merkintä lisätty.", Toast.LENGTH_LONG).show();
            noteText.setText("");
        }else {
            Toast.makeText(getApplicationContext(), "Et voi lisätä alle 2 merkkiä pitkää merkintää.", Toast.LENGTH_LONG).show();
        }

    }

}
