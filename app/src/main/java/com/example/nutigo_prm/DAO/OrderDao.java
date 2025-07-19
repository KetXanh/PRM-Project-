package com.example.nutigo_prm.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.nutigo_prm.Entity.Order;

import java.util.List;
@Dao
public interface OrderDao {
    @Query("SELECT * FROM Orders WHERE username = :userEmail")
    LiveData<List<Order>> getOrdersByUserEmail(String userEmail);
}