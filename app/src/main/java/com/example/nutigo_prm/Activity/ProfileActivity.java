package com.example.nutigo_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.nutigo_prm.DataHelper.Constanst;
import com.example.nutigo_prm.Entity.User;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.UserViewModel;

public class ProfileActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Lấy email người dùng từ Constanst.user
        String userEmail = Constanst.user;
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
//            Intent loginIntent = new Intent(this, LoginActivity.class);
//            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(loginIntent);
//            finish();
            return;
        }

        // Initialize UI components
        TextView profileName = findViewById(R.id.profile_name);
        TextView profileEmail = findViewById(R.id.profile_email);
        Button editProfileButton = findViewById(R.id.edit_profile_button);
        Button orderHistoryButton = findViewById(R.id.order_history_button);
        Button logoutButton = findViewById(R.id.logout_button);

        // Lấy thông tin người dùng từ database qua ViewModel
        userViewModel.getProfile(userEmail).observe(this, user -> {
            if (user != null) {
                currentUser = user;
                profileName.setText(currentUser.username);
                profileEmail.setText(currentUser.email);
            } else {
                Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();
            }
        });

        // Set button click listeners
        editProfileButton.setOnClickListener(v -> {
            if (currentUser != null) {
                Intent editProfileIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                editProfileIntent.putExtra("USER_EMAIL", currentUser.email);
                startActivityForResult(editProfileIntent, 1);
            } else {
                Toast.makeText(this, "Không thể sửa hồ sơ", Toast.LENGTH_SHORT).show();
            }
        });

        orderHistoryButton.setOnClickListener(v -> {
            Intent orderHistoryIntent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
            orderHistoryIntent.putExtra("USER_EMAIL", userEmail);
            startActivity(orderHistoryIntent);
        });

        logoutButton.setOnClickListener(v -> {
            // Xóa thông tin đăng nhập
            Constanst.user = "";
            getSharedPreferences("user_prefs", MODE_PRIVATE).edit().clear().apply();
            Intent loginIntent = new Intent(ProfileActivity.this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Cập nhật lại thông tin người dùng sau khi sửa hồ sơ
            userViewModel.getUserByEmailAndPassword(Constanst.user, "").observe(this, user -> {
                if (user != null) {
                    currentUser = user;
                    TextView profileName = findViewById(R.id.profile_name);
                    TextView profileEmail = findViewById(R.id.profile_email);
                    profileName.setText(currentUser.username);
                    profileEmail.setText(currentUser.email);
                }
            });
        }
    }
}