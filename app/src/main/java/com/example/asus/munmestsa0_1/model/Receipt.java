package com.example.asus.munmestsa0_1.model;

import java.util.Date;

/**
 * Created by Asus on 1.12.2016.
 */

public class Receipt implements Comparable<Receipt>{

    private String id;
    private String metsaId;

    private String description;
    private Date date;

    private double price;
    private int taxRate;

    public Receipt() {
    }

    public Receipt(Date date, String description, String id, String metsaId, double price, int taxRate) {
        this.date = date;
        this.description = description;
        this.id = id;
        this.metsaId = metsaId;
        this.price = price;
        this.taxRate = taxRate;
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

    public String getMetsaId() {
        return metsaId;
    }

    public void setMetsaId(String metsaId) {
        this.metsaId = metsaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public int compareTo(Receipt receipt) {
        return getDate().compareTo(receipt.getDate());
    }
}
