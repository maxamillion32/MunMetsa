package com.example.asus.munmestsa0_1;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.asus.munmestsa0_1.DB.DatabaseHelper;
import com.example.asus.munmestsa0_1.model.Metsa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    private FirebaseDatabase database;
    private DatabaseReference fbRef;

    private DatabaseHelper localDB;

    private ArrayList<Metsa> munMetsat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        munMetsat = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        fbRef = database.getReference("metsat");
        localDB = new DatabaseHelper(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        fbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Cursor res = localDB.getAllData();if(res.getCount() == 0){return;}

                while(res.moveToNext()){
                    String metsaId = res.getString(1);
                    Metsa m = dataSnapshot.child(metsaId).getValue(Metsa.class);
                    munMetsat.add(m);
                    Toast.makeText(getApplicationContext(), "uh  " + m.getTitle(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

}
