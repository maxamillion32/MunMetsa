package com.example.asus.munmestsa0_1.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.munmestsa0_1.R;
import com.example.asus.munmestsa0_1.model.Note;
import com.example.asus.munmestsa0_1.model.Receipt;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Asus on 16.12.2016.
 */

public class CustomReceiptAdapter extends ArrayAdapter{

    // Create a storage reference from our app
    private StorageReference storageRef;

    public CustomReceiptAdapter(Context context, ArrayList<Receipt> notes) {
        super(context, R.layout.custom_note_row,notes);
        storageRef = FirebaseStorage.getInstance().getReference();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View customView = layoutInflater.inflate(R.layout.custom_receipt_row, parent, false);

        final Receipt singleItem = (Receipt) getItem(position);
        TextView contentText = (TextView) customView.findViewById(R.id.receiptTextView);
        TextView dateText = (TextView) customView.findViewById(R.id.receiptDateTextView);
        TextView priceText = (TextView) customView.findViewById(R.id.receiptPriceTextView);
        TextView taxText = (TextView) customView.findViewById(R.id.receiptTaxTextView);


        String date = new SimpleDateFormat("dd-MM-yyyy").format(singleItem.getDate());

        contentText.setText(singleItem.getDescription());
        dateText.setText(date);
        priceText.setText(String.valueOf(singleItem.getPrice()));
        taxText.setText(String.valueOf(singleItem.getTaxRate()));

        final ImageView imageView = (ImageView) customView.findViewById(R.id.showImage);



        File pfile = new File("sdcard/Pictures/"+singleItem.getId());
        if(pfile.exists()){
            Log.v("KUVAT ON","LÖYTYY KUVA");
            imageView.setImageDrawable(Drawable.createFromPath("sdcard/Pictures/"+singleItem.getId()));
        }else{
            StorageReference imageRef = storageRef.child("Receipts/" + singleItem.getId());
            final long ONE_MEGABYTE = 1024 * 1024;

            FileOutputStream out = null;
            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    imageView.setImageBitmap(bmp);

                    FileOutputStream out = null;
                    try{
                        out = new FileOutputStream("sdcard/Pictures/"+singleItem.getId());
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();}finally {try {if (out != null) {out.close();}} catch (IOException e) {e.printStackTrace();}}

                }
            });
        }


        return customView;
    }
}
