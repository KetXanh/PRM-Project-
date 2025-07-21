package com.example.nutigo_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutigo_prm.Adapter.AdminProductAdapter;
import com.example.nutigo_prm.Entity.Product;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.ProductViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView rvProducts;
    private AdminProductAdapter adapter;
    private List<Product> productList;
    private ProductViewModel productViewModel;
    private FloatingActionButton fabAddProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvProducts = findViewById(R.id.recyclerViewProducts);
        fabAddProduct = findViewById(R.id.fabAddProduct);
        productList = new ArrayList<>();
        adapter = new AdminProductAdapter(this, productList, this); // Pass this as LifecycleOwner
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, products -> {
            productList.clear();
            productList.addAll(products);
            adapter.notifyDataSetChanged();
        });

        fabAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AddProductActivity.class);
            startActivity(intent);
        });
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