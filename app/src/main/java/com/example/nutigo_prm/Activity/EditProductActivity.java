package com.example.nutigo_prm.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nutigo_prm.DataHelper.DataHelper;
import com.example.nutigo_prm.Entity.Product;
import com.example.nutigo_prm.R;

public class EditProductActivity extends AppCompatActivity {

    private EditText etCategory, etName, etDescription, etImage, etPrice, etStock;
    private Button btnSave;
    private DataHelper dataHelper;

    private int productId;
    private Product product;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        etCategory = findViewById(R.id.etCategory);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etImage = findViewById(R.id.etImage);
        etPrice = findViewById(R.id.etPrice);
        etStock = findViewById(R.id.etStock);
        btnSave = findViewById(R.id.btnSave);

        dataHelper = new DataHelper(this);

        productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            Toast.makeText(this, "Invalid product ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadProductData();

        btnSave.setOnClickListener(v -> {
            if (validateInput()) {
                saveProduct();
            }
        });
    }

    private void loadProductData() {
        var cursor = dataHelper.getProductById(productId);
        if (cursor.moveToFirst()) {
            String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"));

            product = new Product(productId, category, name, description, image, price, stock);

            etCategory.setText(category);
            etName.setText(name);
            etDescription.setText(description);
            etImage.setText(image);
            etPrice.setText(String.valueOf(price));
            etStock.setText(String.valueOf(stock));
        }
        cursor.close();
    }

    private boolean validateInput() {
        if (etCategory.getText().toString().trim().isEmpty()) {
            etCategory.setError("Category is required");
            return false;
        }
        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Name is required");
            return false;
        }
        if (etPrice.getText().toString().trim().isEmpty()) {
            etPrice.setError("Price is required");
            return false;
        }
        if (etStock.getText().toString().trim().isEmpty()) {
            etStock.setError("Stock is required");
            return false;
        }
        return true;
    }

    private void saveProduct() {
        String category = etCategory.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String image = etImage.getText().toString().trim();
        double price = Double.parseDouble(etPrice.getText().toString().trim());
        int stock = Integer.parseInt(etStock.getText().toString().trim());

        int rowsAffected = dataHelper.updateProduct(productId, category, name, description, image, price, stock);
        if (rowsAffected > 0) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
