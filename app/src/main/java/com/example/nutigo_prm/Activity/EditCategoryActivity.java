package com.example.nutigo_prm.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.nutigo_prm.Entity.Category;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.CategoryViewModel;

public class EditCategoryActivity extends AppCompatActivity {

    private EditText edtCategoryName, edtCategoryDescription;
    private Button btnUpdateCategory;
    private CategoryViewModel categoryViewModel;
    private int categoryId;
    private Category currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category_admin);

        // Khởi tạo view
        edtCategoryName = findViewById(R.id.edtCategoryName);
        edtCategoryDescription = findViewById(R.id.edtCategoryDescription);
        btnUpdateCategory = findViewById(R.id.btnUpdateCategory);

        // ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Lấy ID
        categoryId = getIntent().getIntExtra("category_id", -1);
        if (categoryId == -1) {
            Toast.makeText(this, "Không tìm thấy danh mục", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Lấy thông tin danh mục
        categoryViewModel.getCategoryById(categoryId).observe(this, category -> {
            if (category != null) {
                currentCategory = category;
                edtCategoryName.setText(category.getName());
                edtCategoryDescription.setText(category.getDescription());
            }
        });

        // Cập nhật
        btnUpdateCategory.setOnClickListener(v -> {
            String newName = edtCategoryName.getText().toString().trim();
            String newDescription = edtCategoryDescription.getText().toString().trim();

            if (newName.isEmpty()) {
                Toast.makeText(this, "Tên danh mục không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            currentCategory.setName(newName);
            currentCategory.setDescription(newDescription.isEmpty() ? "Không có mô tả" : newDescription);

            categoryViewModel.update(currentCategory);
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
