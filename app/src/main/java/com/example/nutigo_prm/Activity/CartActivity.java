package com.example.nutigo_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutigo_prm.Adapter.CartAdapter;
import com.example.nutigo_prm.DataHelper.AppDatabase;
import com.example.nutigo_prm.Entity.CartItem;
import com.example.nutigo_prm.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView txtTotal;
    private Button btnCheckout;
    private List<CartItem> cartItems;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        txtTotal = findViewById(R.id.textTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        loadCartItems();

        btnCheckout.setOnClickListener(v -> {
            if (cartItems == null || cartItems.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivityForResult(intent, 1);

            }
        });
    }

    private void loadCartItems() {
        executor.execute(() -> {
            cartItems = AppDatabase.getInstance(CartActivity.this).cartDao().getAllCartItems();

            runOnUiThread(() -> {
                cartAdapter = new CartAdapter(cartItems, new CartAdapter.CartItemListener() {
                    @Override
                    public void onQuantityChanged(CartItem item, int newQuantity) {
                        item.setQuantity(newQuantity);
                        executor.execute(() -> {
                            AppDatabase.getInstance(CartActivity.this).cartDao().updateCartItem(item);
                            runOnUiThread(() -> updateTotalPrice());
                        });
                    }

                    @Override
                    public void onItemRemoved(CartItem item) {
                        executor.execute(() -> {
                            AppDatabase.getInstance(CartActivity.this).cartDao().deleteCartItem(item);
                            runOnUiThread(() -> loadCartItems());
                        });
                    }
                });

                recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                recyclerView.setAdapter(cartAdapter);
                updateTotalPrice();
            });
        });
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.price * item.quantity;
        }
        txtTotal.setText(String.format("Tổng: %.0fđ", total));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadCartItems();
        }
    }

}
