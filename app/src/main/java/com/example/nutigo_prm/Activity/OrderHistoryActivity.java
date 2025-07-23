package com.example.nutigo_prm.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutigo_prm.Adapter.OrderAdapter;
import com.example.nutigo_prm.DataHelper.AppDatabase;
import com.example.nutigo_prm.DataHelper.Constanst;
import com.example.nutigo_prm.Entity.Order;
import com.example.nutigo_prm.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private AppDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = AppDatabase.getInstance(this);


        executor.execute(() -> {
            List<Order> orders = db.orderDao().getOrdersByUser(Constanst.user);

            runOnUiThread(() -> {
                orderAdapter = new OrderAdapter(OrderHistoryActivity.this, orders);
                ordersRecyclerView.setAdapter(orderAdapter);
            });
        });
    }
}
