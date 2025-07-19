package com.example.nutigo_prm.DataHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nutigo_prm.Entity.Cart;

import java.util.ArrayList;
import java.util.List;

public class DataHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nutigo.db";
    private static final int DATABASE_VERSION = 1;

    // Table: Products
    private static final String TABLE_PRODUCTS = "products";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_STOCK = "stock";

    // Table: Feedbacks
    private static final String TABLE_FEEDBACKS = "feedback";
    private static final String COLUMN_FEEDBACK_ID = "feedback_id";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_COMMENT = "comment";
    private static final String COLUMN_FEEDBACK_PRODUCT_ID = "product_id";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Table: Cart
    private static final String CREATE_TABLE_CART =
            "CREATE TABLE IF NOT EXISTS Cart (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "product_id INTEGER, " +
                    "name TEXT, " +
                    "price REAL, " +
                    "quantity INTEGER, " +
                    "image TEXT)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Products table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " ("
                + COLUMN_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_IMAGE + " TEXT, "
                + COLUMN_PRICE + " REAL, "
                + COLUMN_STOCK + " INTEGER)";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Create Feedback table
        String CREATE_FEEDBACK_TABLE = "CREATE TABLE " + TABLE_FEEDBACKS + " ("
                + COLUMN_FEEDBACK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RATING + " INTEGER, "
                + COLUMN_COMMENT + " TEXT, "
                + COLUMN_FEEDBACK_PRODUCT_ID + " INTEGER, "
                + "FOREIGN KEY(" + COLUMN_FEEDBACK_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COLUMN_PRODUCT_ID + "))";
        db.execSQL(CREATE_FEEDBACK_TABLE);

        db.execSQL(CREATE_TABLE_CART);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public long insertProduct(String category, String name, String description, String image, double price, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_STOCK, stock);
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public long insertFeedback(int rating, String comment, int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_COMMENT, comment);
        values.put(COLUMN_FEEDBACK_PRODUCT_ID, productId);
        return db.insert(TABLE_FEEDBACKS, null, values);
    }
    public int updateFeedback(int feedbackId, int rating, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_COMMENT, comment);
        return db.update(TABLE_FEEDBACKS, values, COLUMN_FEEDBACK_ID + " = ?", new String[]{String.valueOf(feedbackId)});
    }
    public int deleteFeedback(int feedbackId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FEEDBACKS, COLUMN_FEEDBACK_ID + " = ?", new String[]{String.valueOf(feedbackId)});
    }


    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    public Cursor getProductById(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
    }

    public Cursor searchProductByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_NAME + " LIKE ?", new String[]{"%" + name + "%"});
    }

    public int updateProduct(int productId, String category, String name, String description, String image, double price, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_STOCK, stock);
        return db.update(TABLE_PRODUCTS, values, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
    }

    public int deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PRODUCTS, COLUMN_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
    }

    public Cursor getFeedbacksByProductId(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_FEEDBACKS + " WHERE " + COLUMN_FEEDBACK_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
    }

    // ✅ Sửa đúng tên bảng thành "products"
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT category FROM " + TABLE_PRODUCTS, null);
        while (cursor.moveToNext()) {
            categories.add(cursor.getString(0));
        }
        cursor.close();
        return categories;
    }

    public Cursor searchProductByCategoryAndName(String category, String keyword) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE category = ? AND name LIKE ?",
                new String[]{category, "%" + keyword + "%"});
    }
    public long insertOrUpdateCart(int productId, String name, double price, int quantity, String image) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra sản phẩm đã tồn tại trong giỏ chưa
        Cursor cursor = db.rawQuery("SELECT * FROM Cart WHERE product_id = ?", new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            // Nếu đã tồn tại thì cập nhật số lượng
            int existingQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            int newQuantity = existingQuantity + quantity;

            ContentValues values = new ContentValues();
            values.put("quantity", newQuantity);
            int rows = db.update("Cart", values, "product_id = ?", new String[]{String.valueOf(productId)});
            cursor.close();
            return rows;
        } else {
            // Nếu chưa thì thêm mới
            ContentValues values = new ContentValues();
            values.put("product_id", productId);
            values.put("name", name);
            values.put("price", price);
            values.put("quantity", quantity);
            values.put("image", image);
            cursor.close();
            return db.insert("Cart", null, values);
        }
    }

    public List<Cart> getAllCartItems() {
        List<Cart> cartList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Cart", null);

        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart();
                cart.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                cart.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow("product_id")));
                cart.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                cart.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                cart.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                cart.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                cartList.add(cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartList;
    }
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Cart", null, null);
    }




}
