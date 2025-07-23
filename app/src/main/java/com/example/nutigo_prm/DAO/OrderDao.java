package com.example.nutigo_prm.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nutigo_prm.Entity.Order;
import com.example.nutigo_prm.Entity.OrderItem;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    long insertOrder(Order order);

    @Query("SELECT * FROM `Order` WHERE user = :user ORDER BY createdAt DESC")
    List<Order> getOrdersByUser(String user);

    @Query("SELECT * FROM `Order` ORDER BY createdAt DESC")
    List<Order> getAllOrders();

    @Query("SELECT * FROM `Order` WHERE id = :orderId")
    Order getOrderById(int orderId);

    @Query("SELECT * FROM `Order` WHERE id = :orderId")
    LiveData<Order> getOrderByIdLive(int orderId);

    @Query("SELECT * FROM order_items WHERE order_id = :orderId")
    List<OrderItem> getOrderItems(int orderId);

    @Query("DELETE FROM `Order`")
    void deleteAllOrders();

    @Update
    void updateOrder(Order order);

    @Query("SELECT * FROM `Order` ORDER BY createdAt DESC")
    LiveData<List<Order>> AdminGetAllOrders();
}