package com.example.nutigo_prm.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nutigo_prm.DataHelper.DataHelper;
import com.example.nutigo_prm.R;

public class AddProductActivity extends AppCompatActivity {

    EditText edtName, edtCategory, edtDescription, edtImage, edtPrice, edtStock;
    Button btnAddProduct;
    DataHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edtName = findViewById(R.id.edtName);
        edtCategory = findViewById(R.id.edtCategory);
        edtDescription = findViewById(R.id.edtDescription);
        edtImage = findViewById(R.id.edtImage);
        edtPrice = findViewById(R.id.edtPrice);
        edtStock = findViewById(R.id.edtStock);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        dbHelper = new DataHelper(this);

        btnAddProduct.setOnClickListener(view -> {
            String name = edtName.getText().toString().trim();
            String category = edtCategory.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String image = edtImage.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String stockStr = edtStock.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                int stock = Integer.parseInt(stockStr);

                long result = dbHelper.insertProduct(category, name, description, image, price, stock);
                if (result != -1) {
                    Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn hình trước
                } else {
                    Toast.makeText(this, "Thêm sản phẩm thất bại.", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá hoặc tồn kho không hợp lệ.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
