package com.example.cpg.model;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = CASCADE))

public class Progression {
    @PrimaryKey private int id;
    private int userId;
    private String name;
    private String path;
    private List<Chord> chords;

    //Constructor
    public Progression(){
        chords = new ArrayList<>();
    }


    public Progression copy(){
        Progression c = new Progression();
        c.userId = this.userId;
        c.name = this.name;
        c.path = this.path;
        c.chords = this.chords;
        return c;
    }

    public boolean sameProg(Progression p){
        return
                this.userId == p.userId &&
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

    public int getLength(){
        return this.chords.size();
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

    public void addChord(Chord chord) {
        this.chords.add(chord);
    }

    public void setChords(List<Chord> chords){
        this.chords = chords;

    }

    public List<Chord> getChords() {

        return this.chords;

    }

}