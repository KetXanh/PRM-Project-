package com.example.nutigo_prm.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_items")
public class CartItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int productId;
    public String name;
    public String imageUrl;
    public double price;
    public int quantity;

    public CartItem(int productId, String name, String imageUrl, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
    }

    // Getter methods
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // Setter
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
