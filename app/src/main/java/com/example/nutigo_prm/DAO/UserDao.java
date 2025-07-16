package com.example.nutigo_prm.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nutigo_prm.Entity.User;


@Dao
public interface UserDao {
    @Insert
    void insert(User user);
    @Query("SELECT * FROM User WHERE email = :email and password =:pass")
    LiveData<User> getAccountByEmail(String email, String pass);

    @Update
    void update(User user);


}



