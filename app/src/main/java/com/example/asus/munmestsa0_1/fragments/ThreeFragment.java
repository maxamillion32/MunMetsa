package com.example.asus.munmestsa0_1.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.munmestsa0_1.R;
import com.example.asus.munmestsa0_1.adapter.CustomReceiptAdapter;
import com.example.asus.munmestsa0_1.model.Receipt;

import java.util.ArrayList;
import java.util.Collections;

public class ThreeFragment extends Fragment {

    private View fView;

    private ListView listView;
    private TextView emptyReceipt;




    public ThreeFragment() {
        // Inflate the layout for this fragment

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fView = inflater.inflate(R.layout.fragment_three, container, false);
        listView = (ListView) fView.findViewById(R.id.receiptView);
        emptyReceipt = (TextView) fView.findViewById(R.id.emptyReceiptText);

        return fView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    public void viewReceipts(ArrayList<Receipt> receipts) {

        if (receipts != null){
            if (listView != null) {
                listView.setVisibility(View.VISIBLE);
                emptyReceipt.setText("");

                Collections.sort(receipts);
                ListAdapter noteAdapter = new CustomReceiptAdapter(this.getContext(), receipts);
                listView.setAdapter(noteAdapter);
                listView.setSelection(noteAdapter.getCount() - 1);
            }
        }else{
            if(emptyReceipt!=null) {
                emptyReceipt.setText("Ei kuitteja");
                listView.setVisibility(View.GONE);
            }
        }
    }



}
