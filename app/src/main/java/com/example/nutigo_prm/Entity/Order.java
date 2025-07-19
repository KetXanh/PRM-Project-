package com.example.nutigo_prm.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;
    public long createdAt;
    public double total;

    public Order(String username, long createdAt, double total) {
        this.username = username;
        this.createdAt = createdAt;
        this.total = total;
    }
}
