package com.example.nutigo_prm.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nutigo_prm.DataHelper.DataHelper;
import com.example.nutigo_prm.Entity.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    private SQLiteDatabase db;

    public CartDAO(Context context) {
        DataHelper dataHelper = new DataHelper(context);
        db = dataHelper.getWritableDatabase();
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addToCart(Cart cartItem) {
        // Nếu sản phẩm đã có => tăng số lượng
        Cart existing = getCartItemByProductId(cartItem.getProductId());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + cartItem.getQuantity());
            updateCart(existing);
            return;
        }

        ContentValues values = new ContentValues();
        values.put("product_id", cartItem.getProductId());
        values.put("name", cartItem.getName());
        values.put("price", cartItem.getPrice());
        values.put("quantity", cartItem.getQuantity());
        values.put("image", cartItem.getImage());

        db.insert("Cart", null, values);
    }

    // Lấy sản phẩm trong cart theo product_id
    public Cart getCartItemByProductId(int productId) {
        Cursor cursor = db.query("Cart", null, "product_id=?", new String[]{String.valueOf(productId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Cart cart = new Cart(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("product_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image"))
            );
            cursor.close();
            return cart;
        }
        return null;
    }

    // Lấy tất cả sản phẩm trong giỏ hàng
    public List<Cart> getAllCartItems() {
        List<Cart> cartList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Cart", null);
        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("product_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("image"))
                );
                cartList.add(cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartList;
    }

    // Cập nhật số lượng
    public void updateCart(Cart cartItem) {
        ContentValues values = new ContentValues();
        values.put("quantity", cartItem.getQuantity());
        db.update("Cart", values, "id=?", new String[]{String.valueOf(cartItem.getId())});
    }

    // Xóa 1 sản phẩm khỏi cart
    public void removeFromCart(int id) {
        db.delete("Cart", "id=?", new String[]{String.valueOf(id)});
    }

    // Xóa toàn bộ cart
    public void clearCart() {
        db.delete("Cart", null, null);
    }
}
