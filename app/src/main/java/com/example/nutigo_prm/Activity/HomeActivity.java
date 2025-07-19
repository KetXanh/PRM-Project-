package com.example.nutigo_prm.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.nutigo_prm.Adapter.ProductAdapter;
import com.example.nutigo_prm.Adapter.SliderAdapter;
import com.example.nutigo_prm.DataHelper.DataHelper;
import com.example.nutigo_prm.Entity.Product;
import com.example.nutigo_prm.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class
HomeActivity extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;
    private Spinner spinnerCategory;
    private ViewPager2 sliderViewPager;
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
        sliderViewPager = findViewById(R.id.sliderViewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        tvEmpty = findViewById(R.id.tvEmpty);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        setSupportActionBar(toolbar);

        // Initialize
        dbHelper = new DataHelper(this);
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        listView.setAdapter(adapter);
        listView.setEmptyView(tvEmpty);

        setupCategorySpinner();
        setupSlider();
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


        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemReselectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Already on home, do nothing
            } else if (id == R.id.nav_cart) {
                Toast.makeText(this, "Giỏ hàng đang phát triển", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_order) {
                Toast.makeText(this, "Đơn hàng đang phát triển", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Cài đặt đang phát triển", Toast.LENGTH_SHORT).show();
            }
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories.get(position);
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSlider() {
        // Sample slider items (replace with actual data from your database or API)
        List<String> sliderImages = Arrays.asList(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwalHbYE8LDug5-uV_lVz82DKg4_S4u5uRYA&s",
                "https://huongvique.vn/wp-content/uploads/2023/06/rong-bien-kep-hat-dinh-duong-3-600x600.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRrLpfiVTgcKHNY87iJwioVNqSePfFY6uAfw&s"
        );
        SliderAdapter sliderAdapter = new SliderAdapter(sliderImages);
        sliderViewPager.setAdapter(sliderAdapter);
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