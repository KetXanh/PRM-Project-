package com.example.nutigo_prm.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.nutigo_prm.Entity.User;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.UserViewModel;

public class AuthsActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button registerButton, loginButton;
    private TextView errorTextView;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auths);

        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
        errorTextView = findViewById(R.id.errorTextView);

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateInput(username, password, true)) {
                checkAndRegisterUser(username, password);
            }
        });

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateInput(username, password, false)) {
                checkAndLoginUser(username, password);
            }
        });
    }

    private boolean validateInput(String username, String password, boolean isRegister) {
        if (username.isEmpty()) {
            errorTextView.setText("Username cannot be empty");
            errorTextView.setVisibility(View.VISIBLE);
            return false;
        }
        if (password.isEmpty()) {
            errorTextView.setText("Password cannot be empty");
            errorTextView.setVisibility(View.VISIBLE);
            return false;
        }
        if (isRegister && password.length() < 6) {
            errorTextView.setText("Password must be at least 6 characters");
            errorTextView.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private void checkAndRegisterUser(String username, String password) {
        userViewModel.getUserByUsernameAndPassword(username, password).observe(this, user -> {
            if (user != null) {
                errorTextView.setText("Username already exists");
                errorTextView.setVisibility(View.VISIBLE);
            } else {
                User newUser = new User(username, password);
                userViewModel.insert(newUser);
                Toast.makeText(AuthsActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                // Clear fields after successful registration
                usernameEditText.setText("");
                passwordEditText.setText("");
                errorTextView.setVisibility(View.GONE);
            }
        });
    }

    private void checkAndLoginUser(String username, String password) {
        userViewModel.getUserByUsernameAndPassword(username, password).observe(this, user -> {
            if (user != null) {
                Toast.makeText(AuthsActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                // TODO: Navigate to main activity or dashboard
                // Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                // startActivity(intent);
                // finish();
            } else {
                errorTextView.setText("Invalid username or password");
                errorTextView.setVisibility(View.VISIBLE);
            }
        });
    }
}