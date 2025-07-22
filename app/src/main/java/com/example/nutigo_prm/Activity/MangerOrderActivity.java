package com.example.nutigo_prm.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutigo_prm.Adapter.ManagerOrder;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.OrderViewModel;

public class MangerOrderActivity extends AppCompatActivity {

    private OrderViewModel orderViewModel;
    private ManagerOrder managerOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manger_order);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        managerOrderAdapter = new ManagerOrder();
        recyclerView.setAdapter(managerOrderAdapter);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        managerOrderAdapter.setOnStatusChangeListener((order, newStatus) -> {
            order.status = newStatus; // cập nhật trạng thái trong object
            orderViewModel.updateOrder(order); // gọi ViewModel để update DB
        });

        orderViewModel.getAllOrders().observe(this, orders -> {
            managerOrderAdapter.setOrders(orders);
        });
    }
}
