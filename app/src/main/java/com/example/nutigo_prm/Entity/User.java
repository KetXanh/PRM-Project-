package com.example.nutigo_prm.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {



    @PrimaryKey
    @ColumnInfo(name = "username")
    @NonNull
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public User() {
        this.username = "username";
        this.password = "password";
    }

}
