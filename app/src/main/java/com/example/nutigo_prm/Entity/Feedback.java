package com.example.nutigo_prm.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Feedback {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "rating")
    private int rating;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "product_id")
    private int productId;

    @ColumnInfo(name = "user_name")
    private String username;

    public Feedback() {}

    public Feedback(int id, int rating, String comment, int productId, String username) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.productId = productId;
        this.username = username;
    }

    public Feedback(int rating, String comment, int productId, String username) {
        this.rating = rating;
        this.comment = comment;
        this.productId = productId;
        this.username = username;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
