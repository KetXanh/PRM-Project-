package com.example.nutigo_prm.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.nutigo_prm.Entity.Category;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.CategoryViewModel;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText edtCategoryName, edtCategoryDescription;
    private Button btnAddCategory;
    private CategoryViewModel categoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_category);

        // Apply window insets for EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        edtCategoryName = findViewById(R.id.edtCategoryName);
        edtCategoryDescription = findViewById(R.id.edtCategoryDescription);
        btnAddCategory = findViewById(R.id.btnAddCategory);

        // Initialize ViewModel
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        // Add category button click
        btnAddCategory.setOnClickListener(v -> {
            String name = edtCategoryName.getText().toString().trim();
            String description = edtCategoryDescription.getText().toString().trim();

            // Validation
            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create and insert category
            Category category = new Category(name, description.isEmpty() ? "No description" : description);
            categoryViewModel.insert(category);
            Toast.makeText(this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}