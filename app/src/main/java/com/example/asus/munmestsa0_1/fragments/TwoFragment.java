package com.example.asus.munmestsa0_1.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.munmestsa0_1.R;
import com.example.asus.munmestsa0_1.adapter.CustomNoteAdapter;
import com.example.asus.munmestsa0_1.model.Note;

import java.util.ArrayList;
import java.util.Date;

public class TwoFragment extends Fragment {

    private View fView;

    private ListView listView;
    private TextView emptyNote;

    ListAdapter listAdapter;

    public TwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fView = inflater.inflate(R.layout.fragment_two, container, false);
        listView = (ListView) fView.findViewById(R.id.noteView);
        emptyNote = (TextView) fView.findViewById(R.id.emptyNoteText);

       return fView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void viewNotes(ArrayList<Note> notes){

        if(listView!=null) {
            emptyNote.setText("");
            ListAdapter noteAdapter = new CustomNoteAdapter(this.getContext(), notes);
            listView.setAdapter(noteAdapter);
        }
    }
    public void notesEmpty(){

    }






}
