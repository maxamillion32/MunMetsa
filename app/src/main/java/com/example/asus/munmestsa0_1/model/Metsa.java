package com.example.asus.munmestsa0_1.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

/**
 * Created by Asus on 1.12.2016.
 */

public class Metsa {

    private String id;
    private String title;
    private String description;
    private int size;
    private Date date;



    private LatLng latLng;

    private List<Note> notes;
    private List<Receipt> receipts;


    public Metsa() {

    }

    public Metsa(Date date, String description, String id, LatLng latLng, List<Note> notes, List<Receipt> receipts, int size, String title) {
        this.date = date;
        this.description = description;
        this.id = id;
        this.latLng = latLng;
        this.notes = notes;
        this.receipts = receipts;
        this.size = size;
        this.title = title;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
