package com.example.dreamjournal.models;

import java.util.Date;

public class Dream {

    private long id;
    private Date date;
    private String description;
    private String category;

    public Dream(long id, String category, String description, Date date) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public Dream(String category, String description, Date date) {
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Override
    public String toString(){
        // TODO: make toString
        return null;
    }

    public boolean isValid(){
        // TODO: validation
        return true;
    }
}

