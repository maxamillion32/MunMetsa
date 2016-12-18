package com.example.asus.munmestsa0_1.model;

/**
 * Created by Asus on 18.12.2016.
 */

public class MapMarker {

    private double latitude;
    private double longitude;

    private String info;

    public MapMarker() {
    }

    public MapMarker(String info, double latitude, double longitude) {
        this.info = info;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
