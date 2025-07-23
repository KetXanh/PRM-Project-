
package com.example.nutigo_prm.Entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String user;

    public String username;
    public String phone;
    public String address;
    public String note;

    public long createdAt;
    public double totalAmount;
    public String status;
    public Order() {
    }

    public Order(String user, String username, String phone, String address, String note, double totalAmount, String status, long createdAt) {
        this.user = user;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.note = note;

        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }
}

