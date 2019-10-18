package com.example.cpg.model;

//This will be an intermediary table between Progression and Chord
public class Progression_Chord {

    //uniquely identifiable primary key
    private int id;

    //FOREIGN KEY to Progression
    private int progressionId;

    //FOREIGN KEY to Chord
    private int chordId;
}
