package com.example.nutigo_prm.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nutigo_prm.Entity.OrderItem;

import java.util.List;

@Dao
public interface OrderItemDao {
    @Query("SELECT * FROM order_items WHERE order_id = :orderId")
    LiveData<List<OrderItem>> getItemsByOrderId(int orderId);

    @Insert
    void insert(OrderItem orderItem);

    @Insert
    void insertAll(List<OrderItem> orderItems);
}