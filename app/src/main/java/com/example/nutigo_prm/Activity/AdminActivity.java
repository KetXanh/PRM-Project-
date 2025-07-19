package com.example.nutigo_prm.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutigo_prm.Adapter.AdminProductAdapter;
import com.example.nutigo_prm.DataHelper.DataHelper;
import com.example.nutigo_prm.Entity.Product;
import com.example.nutigo_prm.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private AdminProductAdapter adapter;
    private List<Product> productList;
    private DataHelper dataHelper;

    private FloatingActionButton fabAddProduct; // phải khởi tạo trước khi dùng

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        rvProducts = findViewById(R.id.recyclerViewProducts);
        fabAddProduct = findViewById(R.id.fabAddProduct);  // *** KHỞI TẠO NÚT NÀY ***
        dataHelper = new DataHelper(this);

        productList = new ArrayList<>();
        adapter = new AdminProductAdapter(this, productList, dataHelper);

        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);

        loadProducts();

        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AddProductActivity.class);
            startActivity(intent);
        });
    }

    private void loadProducts() {
        productList.clear();

        Cursor cursor = dataHelper.getAllProducts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"));

                productList.add(new Product(id, category, name, description, image, price, stock));
            } while (cursor.moveToNext());

            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts(); // reload lại danh sách khi quay lại activity
    }
}
