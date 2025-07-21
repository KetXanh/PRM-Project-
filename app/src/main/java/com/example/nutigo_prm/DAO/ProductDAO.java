package com.example.nutigo_prm.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nutigo_prm.Entity.Product;

import java.util.List;

@Dao
public interface ProductDAO {

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM Product WHERE id = :id LIMIT 1")
    LiveData<Product> getProductById(int id);

    @Query("SELECT * FROM Product")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM Product WHERE categoryId = :categoryId")
    LiveData<List<Product>> getProductsByCategoryId(int categoryId);

    @Query("UPDATE Product SET stock = :newStock WHERE id = :id")
    void updateStockById(int id, int newStock);

    @Query("SELECT * FROM Product WHERE name LIKE :name")
    LiveData<List<Product>> searchProductsByName(String name);
}