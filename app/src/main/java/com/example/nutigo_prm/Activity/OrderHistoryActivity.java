package com.example.nutigo_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.nutigo_prm.ViewModel.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderHistoryActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
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

        // Khởi tạo UserViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Lấy email người dùng từ Constanst.user
        String userEmail = Constanst.user;
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo RecyclerView
        ordersRecyclerView = findViewById(R.id.orders_recycler_view);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(new ArrayList<>());
        ordersRecyclerView.setAdapter(orderAdapter);

        // Lấy danh sách đơn hàng từ ViewModel
//        userViewModel.getOrdersByUserEmail(userEmail).observe(this, orders -> {
//            if (orders != null && !orders.isEmpty()) {
//                orderAdapter.updateOrders(orders);
//            } else {
                // Dữ liệu cứng để hiển thị mẫu nếu không có đơn hàng
                List<Order> sampleOrders = new ArrayList<>();
                sampleOrders.add(new Order("ORDER001", userEmail, "Đang xử lý", System.currentTimeMillis() - 86400000, 150000));
                sampleOrders.add(new Order("ORDER002", userEmail, "Đã giao", System.currentTimeMillis() - 2 * 86400000, 250000));
                sampleOrders.add(new Order("ORDER003", userEmail, "Đã hủy", System.currentTimeMillis() - 3 * 86400000, 100000));
                orderAdapter.updateOrders(sampleOrders);
                Toast.makeText(this, "Không có đơn hàng thực tế, hiển thị dữ liệu mẫu", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    // Adapter cho RecyclerView
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
            holder.orderIdText.setText("Mã đơn hàng: " + order.orderId);
            holder.statusText.setText("Trạng thái: " + order.status);
            holder.totalPriceText.setText(String.format("Tổng tiền: %.2f VNĐ", order.totalPrice));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.createdAtText.setText("Ngày tạo: " + dateFormat.format(new java.util.Date(order.createdAt)));

            // Tùy chỉnh màu sắc theo trạng thái
            switch (order.status) {
                case "Đang xử lý":
                    holder.statusText.setTextColor(0xFFFF9800); // Vàng
                    break;
                case "Đã giao":
                    holder.statusText.setTextColor(0xFF4CAF50); // Xanh lá
                    break;
                case "Đã hủy":
                    holder.statusText.setTextColor(0xFFF44336); // Đỏ
                    break;
                default:
                    holder.statusText.setTextColor(0xFF000000); // Đen
            }

            // Xử lý sự kiện nhấn vào mục đơn hàng
            holder.itemView.setOnClickListener(v -> {
//                Intent detailIntent = new Intent(holder.itemView.getContext(), OrderDetailActivity.class);
//                detailIntent.putExtra("ORDER_ID", order.orderId);
//                holder.itemView.getContext().startActivity(detailIntent);
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