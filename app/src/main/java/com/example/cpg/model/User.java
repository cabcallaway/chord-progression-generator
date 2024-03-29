package com.example.cpg.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate=true) private int id;
    private String name;
    private String email;
    private String password;
    private String spotifyId;

    public int getId() {

        return id;

    }

    public void setId(int id) {

        this.id = id;

    }

    public String getName() {

        return name;

    }

    public void setName(String name) {

        this.name = name;

    }

    public String getEmail() {

        return email;

    }

    public void setEmail(String email) {

        this.email = email;

    }

    public String getPassword() {

        return password;

    }

    public void setPassword(String password) {

        this.password = password;

    }

    public String getSpotifyId(){
        return spotifyId;
    }

    public void setSpotifyId(String spotifyId){
        this.spotifyId = spotifyId;
    }

}