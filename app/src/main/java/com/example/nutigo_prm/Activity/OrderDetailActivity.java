package com.example.nutigo_prm.Activity;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutigo_prm.Adapter.OrderItemAdapter;
import com.example.nutigo_prm.Entity.Order;
import com.example.nutigo_prm.Entity.OrderItem;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.OrderItemViewModel;
import com.example.nutigo_prm.ViewModel.OrderViewModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView tvOrderId, tvOrderDate, tvCustomerName, tvCustomerPhone, tvCustomerAddress, tvCustomerNote, tvTotalAmount;
    private RecyclerView rvOrderItems;
    private Button btnAction;
    private OrderViewModel orderViewModel;
    private OrderItemViewModel orderItemViewModel;
    private OrderItemAdapter orderItemAdapter;
    private List<OrderItem> orderItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);
        // Initialize views
        tvOrderId = findViewById(R.id.tv_order_id);
        tvOrderDate = findViewById(R.id.tv_order_date);
        tvCustomerName = findViewById(R.id.tv_customer_name);
        tvCustomerPhone = findViewById(R.id.tv_customer_phone);
        tvCustomerAddress = findViewById(R.id.tv_customer_address);
        tvCustomerNote = findViewById(R.id.tv_customer_note);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        rvOrderItems = findViewById(R.id.rv_order_items);
        btnAction = findViewById(R.id.btn_action);

        // Initialize ViewModels
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderItemViewModel = new ViewModelProvider(this).get(OrderItemViewModel.class);

        // Initialize RecyclerView
        orderItemList = new ArrayList<>();
        orderItemAdapter = new OrderItemAdapter(this, orderItemList);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rvOrderItems.setAdapter(orderItemAdapter);

        // Get order ID from intent
        int orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderId != -1) {
            loadOrderDetails(orderId);
        } else {
            Toast.makeText(this, "Không tìm thấy ID đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set up back button
        btnAction.setOnClickListener(v -> finish());
    }

    private void loadOrderDetails(int orderId) {
        // Load order details
        orderViewModel.getOrderById(orderId).observe(this, order -> {
            if (order != null) {
                // Set order information
                tvOrderId.setText("Mã đơn hàng: #" + order.id);
                String dateStr = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        .format(new Date(order.createdAt));
                tvOrderDate.setText("Ngày đặt: " + dateStr);
                tvCustomerName.setText("Họ và tên: " + order.username);
                tvCustomerPhone.setText("Số điện thoại: " + order.phone);
                tvCustomerAddress.setText("Địa chỉ: " + order.address);
                tvCustomerNote.setText("Ghi chú: " + (order.note != null ? order.note : ""));

                // Format total amount with VND currency
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                tvTotalAmount.setText("Tổng tiền: " + format.format(order.totalAmount));
            } else {
                Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Load order items
        orderItemViewModel.getOrderItems(orderId).observe(this, orderItems -> {
            if (orderItems != null && !orderItems.isEmpty()) {
                orderItemList.clear();
                orderItemList.addAll(orderItems);
                orderItemAdapter.notifyDataSetChanged();
            } else {
                orderItemList.clear();
                orderItemAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Không tìm thấy sản phẩm trong đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}