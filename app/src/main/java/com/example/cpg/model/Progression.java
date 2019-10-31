package com.example.cpg.model;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

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

}