package com.example.nutigo_prm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutigo_prm.R;
import com.example.nutigo_prm.Adapter.CartAdapter;
import com.example.nutigo_prm.DataHelper.DataHelper;
import com.example.nutigo_prm.Entity.Cart;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartActionListener {

    private RecyclerView recyclerCart;
    private CartAdapter cartAdapter;
    private TextView textTotal;
    private Button btnCheckout;
    private DataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        recyclerCart = findViewById(R.id.recyclerCart);
        textTotal = findViewById(R.id.textTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        dataHelper = new DataHelper(this);

        loadCartItems();
        handleCheckout();
    }

    private void loadCartItems() {
        List<Cart> cartItemList = dataHelper.getAllCartItems();

        cartAdapter = new CartAdapter(this, cartItemList, this); // truyền context + listener
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(cartAdapter);

        updateTotal(cartItemList);
    }

    private void updateTotal(List<Cart> cartItems) {
        double total = 0;
        for (Cart item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        textTotal.setText("Tổng: " + total + " đ");
    }

    private void handleCheckout() {
        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onCartUpdated(List<Cart> updatedCartItems) {
        updateTotal(updatedCartItems);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }

}
