package com.example.nutigo_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.UserViewModel;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private EditText emailInput, passwordInput;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Find views
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        Button loginButton = findViewById(R.id.login_button);
        TextView registerRedirect = findViewById(R.id.register_redirect);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!EMAIL_PATTERN.matcher(email).matches()) {
                Toast.makeText(this, "Vui lòng nhập email hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            userViewModel.getUserByEmailAndPassword(email, password).observe(this, user -> {
                if (user != null) {
                    if(user.role.equals("admin")){
//                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
//                        finish();
                        Toast.makeText(this, "Đăng nhập Admin thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    }
                    Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();


//
                } else {
                    Toast.makeText(this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            });
        });

        registerRedirect.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }
}