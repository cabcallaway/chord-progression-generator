package com.example.cpg.model;

public class Note {

    private String name;

    //use ints to tell which pitch/octave (3,4,5)
    private int octave;

    private int midiMapping;

    public String getNoteName(){

        return name;

    }

    public void setNoteName(String note){

        this.name = note;

    }

    public int getOctave(){

        return octave;
    }

    public void setOctave(int octave) {

        this.octave = octave;

    }

    public int getMidiMapping() {

        return midiMapping;

    }

    public void setMidiMapping(int midiMapping) {

        this.midiMapping = midiMapping;

    }

}
