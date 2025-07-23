package com.example.nutigo_prm.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nutigo_prm.Entity.CartItem;
import com.example.nutigo_prm.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface CartItemListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemRemoved(CartItem item);
    }

    private List<CartItem> cartItems;
    private final CartItemListener listener;
    private String formatPrice(double price) {
        return String.format("%,.0f", price).replace(',', '.') + " đ";
    }

    public CartAdapter(List<CartItem> cartItems, CartItemListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }


    public void setCartItems(List<CartItem> items) {
        this.cartItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.name.setText(item.getName());
        holder.price.setText(formatPrice(item.getPrice()));

        holder.quantity.setText(String.valueOf(item.getQuantity()));

        Glide.with(holder.image.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_cart_foreground)
                .into(holder.image);

        // Nút tăng
        holder.btnIncrease.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;
            item.setQuantity(newQty);
            holder.quantity.setText(String.valueOf(newQty)); // 👈 update text
            listener.onQuantityChanged(item, newQty);
            notifyItemChanged(position); // 👈 update UI
        });

        // Nút giảm
        holder.btnDecrease.setOnClickListener(v -> {
            int newQty = item.getQuantity() - 1;
            if (newQty >= 1) {
                item.setQuantity(newQty);
                holder.quantity.setText(String.valueOf(newQty)); // 👈 update text
                listener.onQuantityChanged(item, newQty);
                notifyItemChanged(position); // 👈 update UI
            } else {
                listener.onItemRemoved(item);
                notifyItemRemoved(position); // 👈 xóa khỏi RecyclerView
            }
        });

        // Nút xóa
        holder.btnRemove.setOnClickListener(v -> {
            listener.onItemRemoved(item);
            notifyItemRemoved(position); // 👈 cần thiết khi xóa
        });
    }


    @Override
    public int getItemCount() {
        return cartItems == null ? 0 : cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        ImageView image;
        Button btnIncrease, btnDecrease;
        ImageButton btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textProductName);
            price = itemView.findViewById(R.id.textProductPrice);
            quantity = itemView.findViewById(R.id.textQuantity);
            image = itemView.findViewById(R.id.imageProduct);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
