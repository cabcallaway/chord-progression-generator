package com.example.cpg.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.cpg.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    public void insert(User user);

    @Update
    public void update(User user);

    @Delete
    public void delete(User user);

    @Query("SELECT * FROM User WHERE email = :email ")
    public User getUserByEmail(String email);

    //implicit conversion from int to boolean (table SHOULD have 0 or 1 users with that email)
    @Query("SELECT COUNT() FROM User WHERE email = :email ")
    public int checkUser(String email);

    @Query("SELECT COUNT() FROM User WHERE email = :email AND password = :password ")
    public int checkUser(String email, String password);

    @Query("SELECT * FROM User")
    public List<User> getAllUsers();

}
