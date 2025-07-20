package com.example.nutigo_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutigo_prm.Adapter.UserAdapter;
import com.example.nutigo_prm.Entity.User;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class ManagerUserActivity extends AppCompatActivity {

    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_user);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        userList = new ArrayList<>();
        userViewModel = new UserViewModel(getApplication());
        userAdapter = new UserAdapter(this, userList, userViewModel);

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(userAdapter);

        // Quan sát LiveData để cập nhật danh sách người dùng
        userViewModel.getAllUsers().observe(this, users -> {
            userList.clear();
            if (users != null) {
                userList.addAll(users);
            }
            userAdapter.setUsers(userList);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_product_management) {
            Toast.makeText(this, "Quản lý sản phẩm được chọn", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_user_management) {
            Toast.makeText(this, "Quản lý người dùng được chọn", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_order_management) {
            Toast.makeText(this, "Quản lý đơn hàng được chọn", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(this, OrderManagementActivity.class);
            // startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}