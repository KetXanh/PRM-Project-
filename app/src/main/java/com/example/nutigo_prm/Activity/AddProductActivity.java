package com.example.nutigo_prm.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.nutigo_prm.Entity.Category;
import com.example.nutigo_prm.Entity.Product;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.CategoryViewModel;
import com.example.nutigo_prm.ViewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private EditText edtName, edtDescription, edtImage, edtPrice, edtStock;
    private Spinner spnCategory;
    private Button btnAddProduct;
    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    private List<Category> categoryList;
    private ArrayAdapter<String> categoryAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize views
        edtName = findViewById(R.id.edtName);
        spnCategory = findViewById(R.id.spnCategory);
        edtDescription = findViewById(R.id.edtDescription);
        edtImage = findViewById(R.id.edtImage);
        edtPrice = findViewById(R.id.edtPrice);
        edtStock = findViewById(R.id.edtStock);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        // Initialize ViewModels
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Initialize category list and adapter
        categoryList = new ArrayList<>();
        List<String> categoryNames = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(categoryAdapter);

        // Load categories into Spinner
        categoryViewModel.getAllCategories().observe(this, categories -> {
            categoryList.clear();
            categoryList.addAll(categories);
            categoryNames.clear();
            for (Category category : categories) {
                categoryNames.add(category.getName());
            }
            categoryAdapter.notifyDataSetChanged();
        });

        // Add product button click
        btnAddProduct.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String image = edtImage.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String stockStr = edtStock.getText().toString().trim();
            int categoryPosition = spnCategory.getSelectedItemPosition();

            // Validation
            if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (categoryPosition < 0 || categoryList.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn danh mục.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int stock = Integer.parseInt(stockStr);
                int categoryId = categoryList.get(categoryPosition).getId();

                // Create and insert product
                Product product = new Product(categoryId, name, description, image.isEmpty() ? "default.jpg" : image, price, stock);
                productViewModel.insert(product);
                Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá hoặc tồn kho không hợp lệ.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}