package com.example.nutigo_prm.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nutigo_prm.Adapter.ProductAdapter;
import com.example.nutigo_prm.DataHelper.DataHelper;
import com.example.nutigo_prm.Model.Product;
import com.example.nutigo_prm.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;
    private Spinner spinnerCategory;
    private DataHelper dbHelper;
    private List<Product> productList;
    private ProductAdapter adapter;
    private TextView tvEmpty;
    private BottomNavigationView bottomNavigationView;
    private String selectedCategory = "Tất cả";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // View mapping
        listView = findViewById(R.id.listViewProducts);
        searchView = findViewById(R.id.searchView);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvEmpty = findViewById(R.id.tvEmpty);
        listView.setEmptyView(tvEmpty);

        // Init
        dbHelper = new DataHelper(this);
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        listView.setAdapter(adapter);

        setupCategorySpinner();
        loadProducts();

        // Search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts();
                return true;
            }
        });

        // FAB add product
        FloatingActionButton fab = findViewById(R.id.fabAddProduct);
        fab.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AddProductActivity.class)));

        // Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_cart) {
                Toast.makeText(this, "Giỏ hàng đang phát triển", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_order) {
                Toast.makeText(this, "Đơn hàng đang phát triển", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Cài đặt đang phát triển", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

    }

    private void setupCategorySpinner() {
        List<String> categories = dbHelper.getAllCategories();
        categories.add(0, "Tất cả");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedCategory = categories.get(position);
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void filterProducts() {
        String keyword = searchView.getQuery().toString().trim();
        productList.clear();

        Cursor cursor;
        if (selectedCategory.equals("Tất cả")) {
            cursor = dbHelper.searchProductByName(keyword);
        } else {
            cursor = dbHelper.searchProductByCategoryAndName(selectedCategory, keyword);
        }

        while (cursor.moveToNext()) {
            productList.add(new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow("product_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("category")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("stock"))
            ));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void loadProducts() {
        productList.clear();
        Cursor cursor = dbHelper.getAllProducts();
        while (cursor.moveToNext()) {
            productList.add(new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow("product_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("category")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("stock"))
            ));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }
}
