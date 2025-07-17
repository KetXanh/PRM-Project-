package com.example.nutigo_prm.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.nutigo_prm.Entity.CartItem;
import com.example.nutigo_prm.Entity.CartProduct;

import java.util.List;

@Dao
public interface CartItemDao {
    @Insert
    void insert(CartItem cartItem);

    @Query("SELECT * FROM CartItem")
    LiveData<List<CartItem>> getAllCartItem();

    @Query("SELECT * FROM CartItem WHERE username = :uname")
    LiveData<List<CartItem>> getCartItemByName(String uname);

    @Query("SELECT * FROM CartItem WHERE username = :uname AND productId = :productId")
    LiveData<CartItem> getCartItem(String uname, int productId);
    @Query("SELECT * FROM CartItem WHERE username = :uname AND productId = :productId LIMIT 1")
    CartItem getCartItemSync(String uname, int productId);

    @Transaction
    @Query("SELECT Product.id as id, username, name, Product.price as price, CartItem.quantity as quantity, description, image " +
            "FROM Product INNER JOIN CartItem ON Product.id = CartItem.productId WHERE username = :user")
    LiveData<List<CartProduct>> getCartProduct(String user);

    @Delete
    void delete(CartItem cartItem);

    @Query("DELETE FROM CartItem WHERE productId = :productId AND username = :user")
    void deleteById(int productId, String user);

    @Update
    void update(CartItem cartItem);

    @Query("UPDATE CartItem SET quantity = :quantity WHERE productId = :id AND username = :user")
    void updateProductFields(int id, int quantity, String user);

    @Query("DELETE FROM CartItem WHERE username = :userName")
    void deleteByUser(String userName);
}