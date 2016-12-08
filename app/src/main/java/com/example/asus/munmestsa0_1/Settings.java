package com.example.asus.munmestsa0_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.munmestsa0_1.DB.DatabaseHelper;

public class Settings extends AppCompatActivity {

    private EditText nickField;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        db = new DatabaseHelper(this);
        nickField = (EditText) findViewById(R.id.editNick);
    }

    public void changeNick(View v){
        Toast.makeText(getApplicationContext(), "Nimimerkki muutettu", Toast.LENGTH_LONG).show();
        db.insertUsername(nickField.getText().toString());
    }

}
