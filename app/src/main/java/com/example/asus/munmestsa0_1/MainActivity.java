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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    private FirebaseDatabase database;
    private DatabaseReference metsat;

    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        metsat = database.getReference();

        myDb = new DatabaseHelper(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewAll();

    }

    public void viewAll(){
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0){
            return;
        }
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<Metsa> metsat = new ArrayList<>();

        while(res.moveToNext()){
            int id = Integer.parseInt(res.getString(0));
            String metsaId = res.getString(1);
            keys.add(metsaId);
            metsat.add(metsat.get());
            //Toast.makeText(getApplicationContext(), metsaId, Toast.LENGTH_LONG).show();
        }
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
