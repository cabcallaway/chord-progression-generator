package com.example.cpg.model;

public class Chord {

    private String name;
    private int lengthInBars;
    private String type;

    public String getChordName(){

        return name;

    }

    public void setChordName(String name){

        this.name = name;

    }

    public void setChordType(String type){

        this.type = type;

    }

    public String getChordType(){
        return this.type;
    }

    public int getLengthInBars() {

        return lengthInBars;

    }

    public void setLengthInBars(int len) {

        this.lengthInBars = len;

    }
}
