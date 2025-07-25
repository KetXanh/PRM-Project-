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
import com.example.nutigo_prm.DataHelper.AppDatabase;
import com.example.nutigo_prm.Entity.OrderItem;
import com.example.nutigo_prm.Entity.Product;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.OrderViewModel;

import java.util.List;

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
            // Nếu trạng thái mới là Completed thì cập nhật tồn kho
            if ("Completed".equals(newStatus)) {
                new Thread(() -> {
                    AppDatabase db = AppDatabase.getInstance(MangerOrderActivity.this);
                    List<OrderItem> orderItems = db.orderItemDao().getOrderItems(order.id);

                    for (OrderItem item : orderItems) {
                        Product product = db.productDao().getProductByIdSync(item.productId);
                        if (product != null) {
                            int newStock = product.getStock() - item.quantity;
                            if (newStock < 0) newStock = 0;
                            db.productDao().updateStockById(item.productId, newStock);
                        }
                    }
                }).start();
            }
        });

        orderViewModel.getAllOrders().observe(this, orders -> {
            managerOrderAdapter.setOrders(orders);
        });
    }
}
