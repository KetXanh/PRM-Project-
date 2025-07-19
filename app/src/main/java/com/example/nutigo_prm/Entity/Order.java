package com.example.nutigo_prm.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String orderId;
    public String userEmail;
    public String status;
    public long createdAt;  // thời gian tạo (milisecond)
    public double totalPrice;

    // Constructor có tham số
    public Order(String orderId, String userEmail, String status, long createdAt, double totalPrice) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.status = status;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
    }

    // Constructor mặc định (bắt buộc với Room nếu dùng)
    public Order() {}
}
