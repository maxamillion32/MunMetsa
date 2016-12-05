package com.example.asus.munmestsa0_1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.munmestsa0_1.R;
import com.example.asus.munmestsa0_1.model.Note;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Asus on 4.12.2016.
 */

public class CustomNoteAdapter extends ArrayAdapter {
    public CustomNoteAdapter(Context context, ArrayList<Note> notes) {
        super(context, R.layout.custom_note_row,notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_note_row, parent, false);

        Log.v("blah", "ARAAAAAAAAPTERI");


        Note singleItem = (Note) getItem(position);
        TextView contentText = (TextView) customView.findViewById(R.id.noteTextView);
        TextView dateText = (TextView) customView.findViewById(R.id.noteDate);

        contentText.setText(singleItem.getContent());
        dateText.setText(singleItem.getDate().toString());
        return customView;
    }
}
