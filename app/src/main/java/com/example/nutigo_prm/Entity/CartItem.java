package com.example.nutigo_prm.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "productId")
    public int productId;

    @ColumnInfo(name = "quantity")
    public int quantity;

    public CartItem(String username, int productId, int quantity) {
        this.username = username;
        this.productId = productId;
        this.quantity = quantity;
    }
}
