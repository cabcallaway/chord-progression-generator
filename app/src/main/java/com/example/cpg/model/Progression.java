package com.example.cpg.model;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = CASCADE))

public class Progression {
    @PrimaryKey private int id;
    private int userId;
    private int length = 0;
    private String name;
    private String path;
    //private ArrayList<String> chords;
    private ArrayList<Chord> chords;

    public Progression copy(){
        Progression c = new Progression();
        c.userId = this.userId;
        c.length = this.length;
        c.name = this.name;
        c.path = this.path;
        c.chords = this.chords;
        return c;
    }

    public boolean sameProg(Progression p){
        return
                this.userId == p.userId &&
                this.length == p.length &&
                this.name.equals(p.name) &&
                this.path.equals(p.path) &&
                this.chords.equals(p.chords);
    }

    public int getId() {

        return id;

    }

    public void setId(int id){

        this.id = id;

    }

    public int getUserId() {

        return userId;

    }

    public void setUserId(int id){

        this.userId = id;

    }

    public int getLength(){
        return this.length;
    }

    public void setLength(int l){
        this.length = l;
    }

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public String getPath(){

        return path;

    }

    public void setPath(String path) {

        this.path = path;

    }

    /*
    public void addChord(String chordName){
        this.chords.add(chordName);
        this.length ++;
    }

    public void removeChord(String chord){
        this.chords.remove(chord);
        this.length --;
    }

    public void updateChord(int index, String chord){
        this.chords.set(index, chord);
    }
    */

    public void updateChord(Chord chord) {

        this.chords.add(chord);

    }

    public void setChords(ArrayList<Chord> chords){

        this.chords = chords;

    }

    public ArrayList<Chord> getChords() {

        return this.chords;

    }

}