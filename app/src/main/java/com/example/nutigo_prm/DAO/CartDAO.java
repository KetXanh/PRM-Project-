package com.example.nutigo_prm.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.nutigo_prm.Entity.CartItem;

import java.util.List;

@Dao
public interface CartDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCartItem(CartItem item);

    @Query("SELECT * FROM cart_items")
    List<CartItem> getAllCartItems();

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    CartItem getCartItemByProductId(int productId);

    @Update
    void updateCartItem(CartItem item);

    @Delete
    void deleteCartItem(CartItem item);

    @Query("DELETE FROM cart_items")
    void clearCart();
    @Update
    void update(CartItem item);

    @Delete
    void delete(CartItem item);

    @Query("SELECT SUM(quantity * price) FROM cart_items")
    double getCartTotal();

}
