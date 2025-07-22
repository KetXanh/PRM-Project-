package com.example.nutigo_prm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nutigo_prm.Entity.OrderItem;
import com.example.nutigo_prm.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private Context context;
    private List<OrderItem> orderItemList;

    public OrderItemAdapter(Context context, List<OrderItem> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = orderItemList.get(position);
        holder.tvProductName.setText(item.productName);
        holder.tvQuantity.setText("x" + item.quantity);

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvUnitPrice.setText(format.format(item.unitPrice));
        double total = item.unitPrice * item.quantity;
        holder.tvTotal.setText(format.format(total));

        // Load product image with Glide
        if (item.productImage != null && !item.productImage.isEmpty()) {
            Glide.with(context)
                    .load(item.productImage)
                    .placeholder(R.drawable.ic_launcher_background) // Add a placeholder image in res/drawable
                    .into(holder.ivProductImage);
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return (orderItemList != null) ? orderItemList.size() : 0;
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvUnitPrice, tvTotal;
        ImageView ivProductImage;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvUnitPrice = itemView.findViewById(R.id.tv_unit_price);
            tvTotal = itemView.findViewById(R.id.tv_total);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
        }
    }
}