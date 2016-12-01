package com.example.asus.munmestsa0_1.model;

import java.util.Date;

/**
 * Created by Asus on 1.12.2016.
 */

public class Note {

    private int id;
    private int metsaId;

    private String content;
    private Date date;
    private String poster;

    public Note(String content, Date date, int id, int metsaId, String poster) {
        this.content = content;
        this.date = date;
        this.id = id;
        this.metsaId = metsaId;
        this.poster = poster;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
