package com.example.nutigo_prm.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.nutigo_prm.Adapter.CategoryAdapter;
import com.example.nutigo_prm.Entity.Category;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class ManagerCategoryActivity extends AppCompatActivity {

    private ListView lvCategories;
    private CategoryViewModel categoryViewModel;
    private List<Category> categories;
    private CategoryAdapter categoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_category);

        // Initialize UI components
        lvCategories = findViewById(R.id.lvCategories);

        // Initialize ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Initialize category list and adapter
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categories, this::showDeleteConfirmationDialog);
        lvCategories.setAdapter(categoryAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fabAddCategory = findViewById(R.id.fabAddCategory);
        fabAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(ManagerCategoryActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });

        // Load categories
        loadCategories();
        onResume();
    }

    private void loadCategories() {
        categoryViewModel.getAllCategories().observe(this, categoryList -> {
            categories.clear();
            if (categoryList == null || categoryList.isEmpty()) {
                Toast.makeText(this, "No categories available", Toast.LENGTH_SHORT).show();
            } else {
                categories.addAll(categoryList);
            }
            categoryAdapter.notifyDataSetChanged();
        });
    }

    private void showDeleteConfirmationDialog(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa Category")
                .setMessage("Bạn có muốn xóa danh mục'" + category.getName() + "'? ")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    categoryViewModel.delete(category);
                    Toast.makeText(this, "Xóa thành công !!!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_product_management) {
            Toast.makeText(this, "Quản lý sản phẩm được chọn", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_category_management) {
            Intent intent = new Intent(this, ManagerCategoryActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_user_management) {
            Intent intent = new Intent(this, ManagerUserActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_order_management) {
            Toast.makeText(this, "Quản lý đơn hàng được chọn", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}