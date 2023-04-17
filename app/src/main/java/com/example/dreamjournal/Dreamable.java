package com.example.dreamjournal;

import com.example.dreamjournal.models.Dream;

import java.util.ArrayList;

public interface Dreamable {

    public ArrayList<Dream> getAllDreams();

    public Dream getDreamById(long id);

    public Dream insertDream(Dream d) throws Exception ;

    public Dream updateDream(Dream d) throws Exception ;

    public int deleteDream(Dream d);

}
