package com.example.asus.munmestsa0_1;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by Asus on 18.12.2016.
 */

public class ReceiptImage extends AppCompatActivity {

    private ImageView imageView;
    private String extra;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_receipt);
        imageView = (ImageView) findViewById(R.id.bigReceiptImage);

        //Get data from previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            extra = extras.getString("receiptId");
        }

        imageView.setImageDrawable(Drawable.createFromPath("sdcard/Pictures/"+extra));

    }
}
