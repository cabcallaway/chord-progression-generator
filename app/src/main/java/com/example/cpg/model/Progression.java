package com.example.cpg.model;

public class Progression {

    private int id;

    private int lengthInBars;

    private String musicalKey;

    private int tempo;

    private String instrument;

    private String beat;

    //this is a FOREIGN KEY to the User table
    private int userId;

    public int getProgId() {

        return id;

    }

    public void setProgId(int id) {

        this.id = id;

    }

    public int getLengthInBars() {

        return lengthInBars;

    }

    public void setLengthInBars(int len) {

        this.lengthInBars = len;

    }

    public String getMusicalKey() {

        return musicalKey;

    }

    public void setMusicalKey(String key) {

        this.musicalKey = key;

    }

    public int getTempo() {

        return tempo;

    }

    public void setTempo(int tempo) {

        this.tempo = tempo;

    }

    public String getInstrument() {

        return instrument;

    }

    public void setInstrument(String instrument) {

        this.instrument = instrument;

    }

    public String getBeat(){

        return beat;

    }

    public void setBeat(String beat) {

        this.beat = beat;

    }

    public int getUserId(){

        return userId;
    }

    public void setUserId(int userId){

        this.userId = userId;

    }
}
