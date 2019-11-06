package com.example.cpg.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cpg.model.Chord;
import com.example.cpg.model.Progression;

import java.util.ArrayList;

@Dao
public interface ProgressionDao {

    @Insert
    public void insert(Progression progression);

    @Update
    public void update(Progression progression);

    @Delete
    public void delete(Progression progression);

    //@Query("SELECT chords FROM Progression WHERE id = :id")
    //public ArrayList<Chord> getChordsById(int id);

}
