package com.example.nutigo_prm.Activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.nutigo_prm.Entity.Category;
import com.example.nutigo_prm.Entity.Product;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.CategoryViewModel;
import com.example.nutigo_prm.ViewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditProductActivity extends AppCompatActivity {

    private EditText edtName, edtDescription, edtImage, edtPrice, edtStock;
    private Spinner spnCategory;
    private Button btnAddProduct;
    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    private int productId = -1;
    private Product product;
    private List<Category> categories;
    private ArrayAdapter<Category> categoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        // Initialize UI components
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

        // Initialize category spinner
        setupCategorySpinner();

        // Check if editing or adding a product
        productId = getIntent().getIntExtra("product_id", -1);
        if (productId != -1) {
            // Editing mode
            btnAddProduct.setText("Save Product");
            loadProductData();
        } else {
            // Adding mode
            btnAddProduct.setText("Add Product");
        }

        // Set save/add button listener
        btnAddProduct.setOnClickListener(v -> {
            if (validateInput()) {
                saveProduct();
            }
        });
    }

    private void setupCategorySpinner() {
        categories = new ArrayList<>();
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(categoryAdapter);

        // Load categories from CategoryViewModel
        categoryViewModel.getAllCategories().observe(this, categoryList -> {
            categories.clear();
            if (categoryList == null || categoryList.isEmpty()) {
                Toast.makeText(this, "No categories available", Toast.LENGTH_SHORT).show();
                btnAddProduct.setEnabled(false);
            } else {
                categories.addAll(categoryList);
                btnAddProduct.setEnabled(true);
            }
            categoryAdapter.notifyDataSetChanged();

            // If editing, reselect the product's category
            if (productId != -1 && product != null) {
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getId() == product.getCategoryId()) {
                        spnCategory.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    private void loadProductData() {
        // Observe product data by ID
        productViewModel.getProductById(productId).observe(this, product -> {
            if (product != null) {
                this.product = product;
                edtName.setText(product.getName());
                edtDescription.setText(product.getDescription());
                edtImage.setText(product.getImage());
                edtPrice.setText(String.valueOf(product.getPrice()));
                edtStock.setText(String.valueOf(product.getStock()));
                // Category selection is handled in setupCategorySpinner after categories load
            } else {
                Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean validateInput() {
        if (edtName.getText().toString().trim().isEmpty()) {
            edtName.setError("Product name is required");
            return false;
        }
        if (spnCategory.getSelectedItem() == null) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtPrice.getText().toString().trim().isEmpty()) {
            edtPrice.setError("Price is required");
            return false;
        }
        if (edtStock.getText().toString().trim().isEmpty()) {
            edtStock.setError("Stock is required");
            return false;
        }
        try {
            Double.parseDouble(edtPrice.getText().toString().trim());
            Integer.parseInt(edtStock.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format for price or stock", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveProduct() {
        Category selectedCategory = (Category) spnCategory.getSelectedItem();
        int categoryId = selectedCategory.getId();
        String name = edtName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String image = edtImage.getText().toString().trim();
        double price = Double.parseDouble(edtPrice.getText().toString().trim());
        int stock = Integer.parseInt(edtStock.getText().toString().trim());

        if (productId != -1) {
            // Update existing product
            product.setCategoryId(categoryId);
            product.setName(name);
            product.setDescription(description);
            product.setImage(image);
            product.setPrice(price);
            product.setStock(stock);
            productViewModel.update(product);
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Add new product
            Product newProduct = new Product(categoryId, name, description, image, price, stock);
            productViewModel.insert(newProduct);
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}