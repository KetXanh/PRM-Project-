package com.example.nutigo_prm.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_items",
        foreignKeys = @ForeignKey(entity = Order.class,
                parentColumns = "id",
                childColumns = "order_id",
                onDelete = ForeignKey.CASCADE))
public class OrderItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "order_id", index = true)
    public int orderId;

    @ColumnInfo(name = "product_id")
    public int productId;

    @ColumnInfo(name = "product_name")
    public String productName;

    @ColumnInfo(name = "product_image")
    public String productImage;

    @ColumnInfo(name = "unit_price")
    public double unitPrice;

    @ColumnInfo(name = "quantity")
    public int quantity;

    public OrderItem(int orderId, int productId, String productName, String productImage, double unitPrice, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }
}

