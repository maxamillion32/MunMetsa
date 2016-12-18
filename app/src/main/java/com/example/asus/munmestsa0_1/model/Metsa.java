package com.example.asus.munmestsa0_1.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Asus on 1.12.2016.
 */

public class Metsa implements Comparable<Metsa>{

    private String id;
    private String title;
    private String description;
    private int size;
    private Date date;

    private double latitude;
    private double longitude;

    private HashMap<String, Note> notes;
    private HashMap<String, Receipt> receipts;
    private HashMap<String, MapMarker> mapMarkers;

    public Metsa(){

    }

    public Metsa(Date date, String description, String id, double latitude, double longitude, HashMap<String, Note> notes, HashMap<String, Receipt> receipts,HashMap<String, MapMarker> mapMarkers, int size, String title) {
        this.date = date;
        this.description = description;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.notes = notes;
        this.receipts = receipts;
        this.size = size;
        this.title = title;
        this.mapMarkers = mapMarkers;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public HashMap<String, Note> getNotes() {
        return notes;
    }

    public void setNotes(HashMap<String, Note> notes) {
        this.notes = notes;
    }

    public HashMap<String, Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(HashMap<String, Receipt> receipts) {
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

    public HashMap<String, MapMarker> getMapMarkers() {
        return mapMarkers;
    }

    public void setMapMarkers(HashMap<String, MapMarker> mapMarkers) {
        this.mapMarkers = mapMarkers;
    }

    @Override
    public String toString() {
        return "Metsa{" +
                "title='" + title + '\'' +
                '}';
    }

    @Override
    public int compareTo(Metsa metsa) {
        return getDate().compareTo(metsa.getDate());
    }
}
