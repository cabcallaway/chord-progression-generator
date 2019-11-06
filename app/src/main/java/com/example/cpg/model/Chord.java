package com.example.cpg.model;

public class Chord {

    private String name;
    private int lengthInBars;
    private String type;


    public Chord(String name, int lengthInBars){
        this.name = name;
        this.lengthInBars = lengthInBars;
        if(name.contains("m7")){
            this.type = "min7";
        } else if(name.contains("m")){
            this.type = "min";
        } else if(name.contains("7")){
            this.type = "maj7";
        } else{
            this.type = "maj";
        }
    }

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
