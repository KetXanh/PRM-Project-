package com.example.nutigo_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.nutigo_prm.Adapter.ProductAdapter;
import com.example.nutigo_prm.Adapter.SliderAdapter;
import com.example.nutigo_prm.Entity.Category;
import com.example.nutigo_prm.Entity.Product;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.CategoryViewModel;
import com.example.nutigo_prm.ViewModel.ProductViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private List<String> categoryNameList = new ArrayList<>();
    private Spinner spinnerCategory;
    private SearchView searchView;
    private ViewPager2 sliderViewPager;
    private TextView tvEmpty;
    private BottomNavigationView bottomNavigationView;

    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;

    private String selectedCategory = "Tất cả";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ánh xạ
        recyclerView = findViewById(R.id.recyclerViewProducts);
        searchView = findViewById(R.id.searchView);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        sliderViewPager = findViewById(R.id.sliderViewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        tvEmpty = findViewById(R.id.tvEmpty);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        setSupportActionBar(toolbar);

        // Cài đặt RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        // ViewModel
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Slider banner
        setupSlider();

        // Quan sát danh sách sản phẩm và danh mục
        observeData();

        // Tìm kiếm
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

        // Bottom Navigation
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            else if (id == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("USER_EMAIL", "user@example.com"); // Thay bằng biến nếu cần
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void setupSlider() {
        List<String> sliderImages = Arrays.asList(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwalHbYE8LDug5-uV_lVz82DKg4_S4u5uRYA&s",
                "https://huongvique.vn/wp-content/uploads/2023/06/rong-bien-kep-hat-dinh-duong-3-600x600.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSRrLpfiVTgcKHNY87iJwioVNqSePfFY6uAfw&s"
        );
        SliderAdapter sliderAdapter = new SliderAdapter(sliderImages);
        sliderViewPager.setAdapter(sliderAdapter);
    }

    private void observeData() {
        // Lấy danh mục
        categoryViewModel.getAllCategories().observe(this, categories -> {
            if (categories != null) {
                categoryList.clear();
                categoryList.addAll(categories);

                categoryNameList.clear();
                categoryNameList.add("Tất cả");
                categoryNameList.addAll(
                        categories.stream().map(Category::getName).collect(Collectors.toList())
                );

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        categoryNameList
                );
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(spinnerAdapter);

                spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedCategory = categoryNameList.get(position);
                        filterProducts();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
            }
        });

        // Lấy danh sách sản phẩm
        productViewModel.getAllProducts().observe(this, products -> {
            if (products != null) {
                productList.clear();
                productList.addAll(products);
                filterProducts();
            }
        });
    }

    private void filterProducts() {
        String keyword = searchView.getQuery().toString().trim().toLowerCase();

        List<Product> filtered = productList.stream()
                .filter(p -> p.name.toLowerCase().contains(keyword))
                .filter(p -> selectedCategory.equals("Tất cả") ||
                        getCategoryNameById(p.categoryId).equals(selectedCategory))
                .collect(Collectors.toList());

        productAdapter = new ProductAdapter(this, filtered);
        recyclerView.setAdapter(productAdapter);

        tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private String getCategoryNameById(int id) {
        for (Category c : categoryList) {
            if (c.getId() == id) return c.getName();
        }
        return "";
    }
}
