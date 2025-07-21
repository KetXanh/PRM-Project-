package com.example.nutigo_prm.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nutigo_prm.Entity.Category;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("SELECT * FROM Category WHERE id = :id LIMIT 1")
    LiveData<Category> getCategoryById(int id);

    @Query("SELECT * FROM Category")
    LiveData<List<Category>> getAllCategories();

    @Query("UPDATE Category SET name = :newName WHERE id = :id")
    void updateNameById(int id, String newName);

    @Query("SELECT * FROM Category WHERE name LIKE :name")
    LiveData<List<Category>> searchCategoriesByName(String name);
}