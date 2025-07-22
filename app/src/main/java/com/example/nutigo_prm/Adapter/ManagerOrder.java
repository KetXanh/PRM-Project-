package com.example.nutigo_prm.Adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutigo_prm.Entity.Order;
import com.example.nutigo_prm.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManagerOrder extends RecyclerView.Adapter<ManagerOrder.OrderViewHolder> {

    public interface OnStatusChangeListener {
        void onStatusChange(Order order, String newStatus);
    }

    private OnStatusChangeListener listener;

    public void setOnStatusChangeListener(OnStatusChangeListener listener) {
        this.listener = listener;
    }

    private List<Order> orders = new ArrayList<>();

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.tvUsername.setText(order.username);
        holder.tvPhone.setText(order.phone);
        holder.tvAddress.setText(order.address);
        holder.tvNote.setText(order.note);
        holder.tvTotalAmount.setText(String.format(Locale.getDefault(), "%.2f", order.totalAmount));
        holder.tvStatus.setText(order.status);

        Date date = new Date(order.createdAt);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvCreatedAt.setText(sdf.format(date));

        // Click vào trạng thái để đổi trạng thái
        holder.tvStatus.setOnClickListener(v -> {
            if (listener != null) {
                String[] statuses = {"Pending", "Processing", "Completed", "Cancelled"};

                new AlertDialog.Builder(v.getContext())
                        .setTitle("Chọn trạng thái")
                        .setItems(statuses, (dialog, which) -> {
                            String selectedStatus = statuses[which];
                            listener.onStatusChange(order, selectedStatus);
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvPhone, tvAddress, tvNote, tvTotalAmount, tvStatus, tvCreatedAt;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvNote = itemView.findViewById(R.id.tvNote);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
        }
    }
}
