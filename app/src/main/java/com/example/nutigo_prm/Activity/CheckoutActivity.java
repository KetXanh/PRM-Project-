package com.example.nutigo_prm.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nutigo_prm.DataHelper.AppDatabase;
import com.example.nutigo_prm.Entity.CartItem;
import com.example.nutigo_prm.Entity.Order;
import com.example.nutigo_prm.Entity.OrderItem;
import com.example.nutigo_prm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckoutActivity extends AppCompatActivity {

    private EditText etFullName, etPhoneNumber, etShippingAddress, etShippingNote;
    private Button btnCheckout;

    private AppDatabase database;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Khởi tạo RoomDB
        database = AppDatabase.getInstance(this);

        // Ánh xạ view
        etFullName = findViewById(R.id.et_full_name);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etShippingAddress = findViewById(R.id.et_shipping_address);
        etShippingNote = findViewById(R.id.et_shipping_note);
        btnCheckout = findViewById(R.id.btn_checkout);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etFullName.getText().toString().trim();
                String phone = etPhoneNumber.getText().toString().trim();
                String address = etShippingAddress.getText().toString().trim();
                String note = etShippingNote.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    Toast.makeText(CheckoutActivity.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!phone.matches("^\\d{10}$")) {
                    Toast.makeText(CheckoutActivity.this, "Số điện thoại phải đúng 10 chữ số.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lấy tổng giá đơn hàng và insert vào DB
                executor.execute(() -> {
                    double totalAmount = database.cartDao().getCartTotal();

                    Order order = new Order();
                    order.username = name;
                    order.phone = phone;
                    order.address = address;
                    order.note = note;
                    order.status = "Pending";
                    order.createdAt = System.currentTimeMillis();
                    order.totalAmount = totalAmount;

                    long orderId = database.orderDao().insertOrder(order);

                    if (orderId > 0) {
                        List<CartItem> cartItems = database.cartDao().getAllCartItems();

                        List<OrderItem> orderItems = new ArrayList<>();
                        for (CartItem cartItem : cartItems) {
                            OrderItem item = new OrderItem(
                                    (int) orderId,
                                    cartItem.productId,
                                    cartItem.name,
                                    cartItem.imageUrl,
                                    cartItem.price,
                                    cartItem.quantity
                            );
                            orderItems.add(item);
                        }

                        // 🟢 Thêm vào bảng order_items
                        database.orderItemDao().insertAll(orderItems); // cần khai báo insertAll trong DAO

                        database.cartDao().clearCart();

                        runOnUiThread(() -> {
                            Toast.makeText(CheckoutActivity.this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK);
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(CheckoutActivity.this, "Đặt hàng thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        });
                    }
                });

            }
        });
    }
}
