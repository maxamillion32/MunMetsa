package com.example.asus.munmestsa0_1.model;

import java.util.Date;

/**
 * Created by Asus on 1.12.2016.
 */

public class Note implements Comparable<Note>{

    private String id;
    private String metsaId;

    private String content;
    private Date date;
    private String poster;

    public Note() {

    }

    public Note(String content, Date date, String id, String metsaId, String poster) {
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public int compareTo(Note note) {
        return getDate().compareTo(note.getDate());
    }
}
