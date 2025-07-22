package com.example.nutigo_prm.DataHelper;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.nutigo_prm.DAO.CartDAO;
import com.example.nutigo_prm.DAO.CategoryDAO;
import com.example.nutigo_prm.DAO.OrderDao;
import com.example.nutigo_prm.DAO.OrderItemDao;
import com.example.nutigo_prm.DAO.ProductDAO;
import com.example.nutigo_prm.DAO.UserDao;
import com.example.nutigo_prm.DAO.FeedbackDAO;
import com.example.nutigo_prm.Entity.CartItem;
import com.example.nutigo_prm.Entity.Category;
import com.example.nutigo_prm.Entity.Order;
import com.example.nutigo_prm.Entity.OrderItem;
import com.example.nutigo_prm.Entity.Product;
import com.example.nutigo_prm.Entity.User;
import com.example.nutigo_prm.Entity.Feedback;

@Database(entities = {User.class, Order.class, Product.class, Category.class, Feedback.class, CartItem.class, OrderItem.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract OrderDao orderDao();
    public abstract ProductDAO productDao();
    public abstract CategoryDAO categoryDAO();
    public abstract FeedbackDAO feedbackDAO();
    public abstract CartDAO cartDao();
    public abstract OrderItemDao orderItemDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "nutigo_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                    populateInitialData(INSTANCE);
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `Order` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`username` TEXT, " +
                            "`phone` TEXT, " +
                            "`address` TEXT, " +
                            "`note` TEXT, " +
                            "`createdAt` INTEGER NOT NULL, " +
                            "`totalAmount` REAL NOT NULL, " +
                            "`status` TEXT)"
            );

            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `order_items` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`order_id` INTEGER NOT NULL, " +
                            "`product_id` INTEGER NOT NULL, " +
                            "`product_name` TEXT, " +
                            "`product_image` TEXT, " +
                            "`unit_price` REAL NOT NULL, " +
                            "`quantity` INTEGER NOT NULL, " +
                            "FOREIGN KEY(`order_id`) REFERENCES `Order`(`id`) ON DELETE CASCADE)"
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