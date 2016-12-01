package com.example.asus.munmestsa0_1.model;

import java.util.Date;

/**
 * Created by Asus on 1.12.2016.
 */

public class Receipt {

    private int id;
    private int metsaId;

    private String description;
    private Date date;

    private double price;
    private int taxRate;

    public Receipt(Date date, String description, int id, int metsaId, double price, int taxRate) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMetsaId() {
        return metsaId;
    }

    public void setMetsaId(int metsaId) {
        this.metsaId = metsaId;
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
}
