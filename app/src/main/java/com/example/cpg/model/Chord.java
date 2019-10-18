package com.example.cpg.model;

public class Chord {

    private String name;

    private int lengthInBars;

    public String getChordName(){

        return name;

    }

    public void setChordName(String name){

        this.name = name;

    }

    public int getLengthInBars() {

        return lengthInBars;

    }

    public void setLengthInBars(int len) {

        this.lengthInBars = len;

    }
}
