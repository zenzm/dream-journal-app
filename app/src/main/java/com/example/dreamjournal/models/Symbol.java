package com.example.dreamjournal.models;

public class Symbol {

    private long id;
    private String name;
    private String description;

    public Symbol(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Symbol(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        return String.format("ID: %d NAME: %s DESC: %s", id, name, description);
    }

    public boolean isValid(){
        if(this.name.isEmpty()){
            return false;
        }
        if(this.description.isEmpty()){
            return false;
        }
        return true;
    }
}

