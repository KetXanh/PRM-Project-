package com.example.nutigo_prm.DataHelper;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.nutigo_prm.DAO.UserDao;
import com.example.nutigo_prm.Entity.User;


@Database(entities = { User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

//    public abstract ProductDao productDao();
    public abstract UserDao userDao();
//    public abstract CartItemDao cartItemDao();
//    public abstract OrderDao orderDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "shopping.sqlite")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                    populateInitialData(INSTANCE);
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `orders` (" +
                            "`id`          INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`email`    TEXT, " +
                    "`created_at`  INTEGER NOT NULL, " +
                            "`total_price` REAL    NOT NULL)"
            );
        }
    };

    private static void populateInitialData(AppDatabase db) {
        new Thread(() -> {
            UserDao userDao = db.userDao();
            User admin = userDao.getUserByEmail("admin@gmail.com");
            if (admin == null) {
                User newAdmin = new User("admin", "admin123", "admin@gmail.com", "admin");
                userDao.insert(newAdmin);
            }
        }).start();
    }

}