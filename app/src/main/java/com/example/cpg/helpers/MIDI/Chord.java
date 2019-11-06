package com.example.cpg.helpers.MIDI;

public enum Chord{
    A("A"),
    Am("Am"),
    A7("A7"),
    Am7("Am7"),
    B("B"),
    Bm("Bm"),
    B7("B7"),
    Bm7("Bm7"),
    C("C"),
    Cm("Cm"),
    C7("C7"),
    Cm7("Cm7"),
    D("D"),
    Dm("Dm"),
    D7("D7"),
    Dm7("Dm7"),
    E("E"),
    Em("Em"),
    E7("E7"),
    Em7("Em7"),
    F("F"),
    Fm("Fm"),
    F7("F7"),
    Fm7("Fm7"),
    G("G"),
    Gm("Gm"),
    G7("G7"),
    Gm7("Gm7");

    private String chord;

    private Chord(String s){
        this.chord = chord;
    }

    public String getMidiNote(){
        return this.chord;
    }
}
