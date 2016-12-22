package com.example.asus.munmestsa0_1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

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

     /*   File imgFile = new File("sdcard/Pictures/"+extra);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.bigReceiptImage);

            myImage.setImageBitmap(myBitmap);

        }
*/
    }
}
