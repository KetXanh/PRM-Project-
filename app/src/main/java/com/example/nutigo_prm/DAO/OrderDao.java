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

    @Insert
    long insertOrder(Order order);

    @Query("SELECT * FROM `Order` ORDER BY createdAt DESC")
    List<Order> getAllOrders();

    @Query("SELECT * FROM `Order` WHERE id = :orderId")
    Order getOrderById(int orderId);

    @Query("DELETE FROM `Order`")
    void deleteAllOrders();

    @Update
    void updateOrder(Order order);
}
