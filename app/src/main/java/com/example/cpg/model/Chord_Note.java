package com.example.cpg.model;

public class Chord_Note {

    //uniquely identifiable primary key
    private int id;

    //FOREIGN KEY to Chord
    private int chordId;

    //FOREIGN KEY to Note
    private int noteId;
}
