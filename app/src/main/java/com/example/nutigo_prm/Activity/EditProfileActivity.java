package com.example.nutigo_prm.Activity;

import android.content.Intent;
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

import com.example.nutigo_prm.Entity.User;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.UserViewModel;

public class EditProfileActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private User currentUser;
    private EditText usernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Lấy email từ Intent
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI components
        usernameInput = findViewById(R.id.username_input);
        Button saveButton = findViewById(R.id.save_button);

        // Lấy thông tin người dùng
        userViewModel.getProfile(userEmail).observe(this, user -> {
            if (user != null) {
                currentUser = user;
                usernameInput.setText(currentUser.username);
            } else {
                Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Lưu thông tin khi nhấn nút Save
        saveButton.setOnClickListener(v -> {
            String newUsername = usernameInput.getText().toString().trim();
            if (newUsername.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentUser != null) {
                userViewModel.updateUsernameByEmail(currentUser.email, newUsername);
                Toast.makeText(this, "Cập nhật hồ sơ thành công", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}