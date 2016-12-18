package com.example.asus.munmestsa0_1;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.munmestsa0_1.DB.DatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {

    private EditText nickField;
    private EditText addForestField;
    private EditText deleteForestField;
    private DatabaseHelper db;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DatabaseHelper(this);
        database = FirebaseDatabase.getInstance();

        nickField = (EditText) findViewById(R.id.editNick);
        addForestField = (EditText) findViewById(R.id.addForestId);
        deleteForestField = (EditText) findViewById(R.id.deleteForestName);

        if(db.getUsername()==null){
            nickField.setText("Default");
        }else {
            nickField.setText(db.getUsername());
        }
    }

    public void changeNick(View v){
        Toast.makeText(getApplicationContext(), "Nimimerkki muutettu", Toast.LENGTH_LONG).show();
        db.insertUsername(nickField.getText().toString());
    }
    public void addForest(View v){
        Cursor res = db.getAllData();
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                if (res.getString(1).equals(addForestField.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Olet jo lisännyt tämän metsän", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        database.getReference("metsat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(addForestField.getText().toString())){
                    db.insertData(addForestField.getText().toString());
                    Toast.makeText(getApplicationContext(), "Metsä lisätty onnistuneesti! Käynnistä sovellus uudestaan.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Metsää ei löydy tällä avaimella", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteForest(View v){
        Cursor res = db.getAllData();
        if (res.getCount() == 0) {return;}
        while (res.moveToNext()) {
            if(res.getString(1).equals(deleteForestField.getText().toString())){
                db.removeForest(res.getString(1));
                Toast.makeText(getApplicationContext(), "Metsä poistettu puhelimen muistista", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

}
