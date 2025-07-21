package com.example.nutigo_prm.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.nutigo_prm.Entity.Feedback;

import java.util.List;

@Dao
public interface FeedbackDAO {

    @Insert
    void insert(Feedback feedback);

    @Update
    void update(Feedback feedback);

    @Delete
    void delete(Feedback feedback);

    @Query("SELECT * FROM Feedback")
    LiveData<List<Feedback>> getAllFeedbacks();

    @Query("SELECT * FROM Feedback WHERE product_id = :productId")
    LiveData<List<Feedback>> getFeedbacksByProductId(int productId);

    @Query("SELECT * FROM Feedback WHERE user_name = :username")
    LiveData<List<Feedback>> getFeedbacksByUsername(String username);

    @Query("SELECT * FROM Feedback WHERE product_id = :productId AND user_name = :username LIMIT 1")
    LiveData<Feedback> getFeedbackByUserAndProduct(int productId, String username);
}
