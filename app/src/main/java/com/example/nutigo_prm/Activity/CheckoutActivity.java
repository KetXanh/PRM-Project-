package com.example.nutigo_prm.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nutigo_prm.DataHelper.DataHelper;
import com.example.nutigo_prm.R;

public class CheckoutActivity extends AppCompatActivity {

    private EditText etFullName, etPhoneNumber, etShippingAddress, etShippingNote;
    private Button btnCheckout;

    private DataHelper dataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout); // Đảm bảo layout đúng tên

        // Khởi tạo
        dataHelper = new DataHelper(this);

        // Ánh xạ view
        etFullName = findViewById(R.id.et_full_name);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etShippingAddress = findViewById(R.id.et_shipping_address);
        etShippingNote = findViewById(R.id.et_shipping_note);
        btnCheckout = findViewById(R.id.btn_checkout);

        // Xử lý click
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etFullName.getText().toString().trim();
                String phone = etPhoneNumber.getText().toString().trim();
                String address = etShippingAddress.getText().toString().trim();
                String note = etShippingNote.getText().toString().trim();

                // Kiểm tra dữ liệu
                if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    Toast.makeText(CheckoutActivity.this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!phone.matches("^\\d{10}$")) {
                    Toast.makeText(CheckoutActivity.this, "Số điện thoại phải đúng 10 chữ số.", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean success = dataHelper.createOrder(name, phone, address, note);

                if (success) {
                    dataHelper.clearCart();
                    Toast.makeText(CheckoutActivity.this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(CheckoutActivity.this, "Có lỗi xảy ra. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
