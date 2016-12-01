package com.example.asus.munmestsa0_1.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Asus on 1.12.2016.
 */

public class Metsa {

    private int id;
    private String title;
    private String description;
    private int size;
    private Date date;

    private List<Note> notes;
    private List<Receipt> receipts;

    private int longitude;
    private int latitude;

    public Metsa(Date date, String description, int id, int latitude, int longitude, List<Note> notes, List<Receipt> receipts, int size, String title) {
        this.date = date;
        this.description = description;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
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
