package com.example.cpg.dao;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cpg.model.Chord;
import com.example.cpg.model.Progression;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface ProgressionDao {

    @Insert
    public void insert(Progression progression);

    @Update
    public void update(Progression progression);

    @Delete
    public void delete(Progression progression);

    @Query("SELECT * FROM Progression WHERE userId = :userId")
    public List<Progression> getAllProgs(int userId);

    @Query("SELECT name FROM Progression WHERE userId = :userId")
    public List<String> getAllProgNames(int userId);

    @Query("SELECT * FROM Progression WHERE name = :name AND userId = :userId")
    public Progression getProgressionByName(String name, int userId);

    @Query("SELECT COUNT() FROM Progression WHERE name= :name AND userId = :userId")
    public int checkProgression(String name, int userId);

    //@Query("SELECT chords FROM Progression WHERE id = :id")
    //public ArrayList<Chord> getChordsById(int id);

}
