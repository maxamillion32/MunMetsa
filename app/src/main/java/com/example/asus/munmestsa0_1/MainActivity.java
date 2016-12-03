package com.example.asus.munmestsa0_1;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class MainActivity extends AppCompatActivity{


    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    private FirebaseDatabase database;
    private DatabaseReference fbRef;

    private DatabaseHelper localDB;

    private ArrayList<Metsa> munMetsat;
    private HashMap<String, List<String>> metsaMap;

    private ExpandableListView expandableListView;

    private Metsa currentMetsa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new HomeFragment(), "Home");
        viewPagerAdapter.addFragments(new TwoFragment(), "Two");
        viewPagerAdapter.addFragments(new ThreeFragment(), "Three");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        munMetsat = new ArrayList<>();
        currentMetsa = null;

        expandableListView = (ExpandableListView) findViewById(R.id.metsaList);

        database = FirebaseDatabase.getInstance();
        fbRef = database.getReference("metsat");
        localDB = new DatabaseHelper(this);

        metsaMap = new HashMap<>();

        listItems();


        fbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cursor res = localDB.getAllData();if(res.getCount() == 0){return;}

                while(res.moveToNext()){
                    String metsaId = res.getString(1);
                    Metsa m = dataSnapshot.child(metsaId).getValue(Metsa.class);
                    munMetsat.add(m);
                }
                currentMetsa = munMetsat.get(0);
                showMetsa(currentMetsa);
                listItems();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void showMetsa(Metsa m){

    }

    public void listItems(){
        ArrayList<String> titles = new ArrayList<>();
        for(Metsa s : this.munMetsat){
            titles.add(s.getTitle());

        }
        Log.d("myTag", "MUNKKI");
        metsaMap.put("Näytä metsät", titles);

        //listAdapter = new ArrayAdapter<Metsa>(this,android.R.layout.simple_list_item_1, munMetsat);
        //metsaList.setAdapter(listAdapter);
        ArrayList<String> t = new ArrayList<>();
        t.add("Näytä metsät");
        ExpandableListAdapter metsaAdapter = new MetsaAdapter(this, t,metsaMap);
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

            Toast.makeText(getApplicationContext(), "Painoit addia", Toast.LENGTH_LONG).show();
        }else if(res_id==R.id.menu_main){

        }
        return true;
    }

    public void childClicked(View v){
        String metsaName = ((TextView)v.findViewById(R.id.child_item)).getText().toString();
        Toast.makeText(getApplicationContext(), "Painoit mettaa " + metsaName, Toast.LENGTH_LONG).show();
    }


}
