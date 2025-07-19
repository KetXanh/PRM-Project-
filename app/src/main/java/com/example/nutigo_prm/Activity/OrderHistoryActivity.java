package com.example.nutigo_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nutigo_prm.DataHelper.Constanst;
import com.example.nutigo_prm.Entity.Order;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.OrderViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderHistoryActivity extends AppCompatActivity {

    private OrderViewModel orderViewModel;
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize OrderViewModel
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        // Get user email from Constanst.user
        String userEmail = Constanst.user;
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize RecyclerView
        ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(new ArrayList<>());
        ordersRecyclerView.setAdapter(orderAdapter);
        Log.d("OrderHistory", "User email: " + userEmail);
        // Fetch orders from ViewModel
        orderViewModel.getOrdersByUserEmail(userEmail).observe(this, orders -> {
            Log.d("OrderHistory", "ỏdorder"+orders);

            if (orders != null && !orders.isEmpty()) {
                orderAdapter.updateOrders(orders);
            } else {
                Toast.makeText(this, "Không có đơn hàng nào", Toast.LENGTH_SHORT).show();
                orderAdapter.updateOrders(new ArrayList<>());
            }
        });
    }

    // OrderAdapter and OrderViewHolder remain unchanged
    private static class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
        private List<Order> orders;

        public OrderAdapter(List<Order> orders) {
            this.orders = orders;
        }

        public void updateOrders(List<Order> newOrders) {
            this.orders = newOrders;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order, parent, false);
            return new OrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            Order order = orders.get(position);
            holder.orderIdText.setText("Mã đơn hàng: " + order.id);
            holder.statusText.setText("Trạng thái: " + order.status);
            holder.totalPriceText.setText(String.format("Tổng tiền: %.2f VNĐ", order.total));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.createdAtText.setText("Ngày tạo: " + dateFormat.format(new java.util.Date(order.createdAt)));

            // Customize status color
            switch (order.status) {
                case "Đang xử lý":
                    holder.statusText.setTextColor(0xFFFF9800); // Yellow
                    break;
                case "Đã giao":
                    holder.statusText.setTextColor(0xFF4CAF50); // Green
                    break;
                case "Đã hủy":
                    holder.statusText.setTextColor(0xFFF44336); // Red
                    break;
                default:
                    holder.statusText.setTextColor(0xFF000000); // Black
            }

            // Handle item click
            holder.itemView.setOnClickListener(v -> {
                Intent detailIntent = new Intent(holder.itemView.getContext(), OrderDetailActivity.class);
                detailIntent.putExtra("ORDER_ID", order.id);
                holder.itemView.getContext().startActivity(detailIntent);
            });
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }

        static class OrderViewHolder extends RecyclerView.ViewHolder {
            TextView orderIdText, statusText, totalPriceText, createdAtText;

            public OrderViewHolder(@NonNull android.view.View itemView) {
                super(itemView);
                orderIdText = itemView.findViewById(R.id.order_id_text);
                statusText = itemView.findViewById(R.id.status_text);
                totalPriceText = itemView.findViewById(R.id.total_price_text);
                createdAtText = itemView.findViewById(R.id.created_at_text);
            }
        }
    }
}