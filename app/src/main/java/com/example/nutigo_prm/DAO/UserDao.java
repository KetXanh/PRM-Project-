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
    @Query("SELECT * FROM User WHERE username = :name and password =:pass")
    LiveData<User> getProductByName(String name, String pass);

    @Update
    void update(User user);


}



