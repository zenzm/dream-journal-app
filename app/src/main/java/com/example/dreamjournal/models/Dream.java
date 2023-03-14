package com.example.dreamjournal.models;

import java.util.Date;

public class Dream {

    private int id;
    private Date date;
    private String description;

    public Dream(int id, Date date, String description) {
        this.id = id;
        this.date = date;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

