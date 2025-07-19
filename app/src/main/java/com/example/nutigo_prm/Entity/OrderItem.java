package com.example.nutigo_prm.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class OrderItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int orderId;
    public int productId;
    public int quantity;
    public double price;

    public OrderItem(int orderId, int productId, int quantity, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }
}
