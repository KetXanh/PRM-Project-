package com.example.nutigo_prm.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.nutigo_prm.Adapter.FeedbackAdapter;
import com.example.nutigo_prm.DataHelper.DataHelper;
import com.example.nutigo_prm.Model.Feedback;
import com.example.nutigo_prm.R;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvName, tvCategory, tvDescription, tvPrice, tvStock, tvEmptyFeedback;
    private ImageView imgProduct;
    private ListView listViewFeedback;
    private Button btnAddFeedback;

    private DataHelper dbHelper;
    private int productId;
    private List<Feedback> feedbacks;
    private FeedbackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Khởi tạo view
        tvName = findViewById(R.id.tvName);
        tvCategory = findViewById(R.id.tvCategory);
        tvDescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        tvStock = findViewById(R.id.tvStock);
        imgProduct = findViewById(R.id.imgProduct);
        listViewFeedback = findViewById(R.id.listViewFeedback);
        tvEmptyFeedback = findViewById(R.id.tvEmptyFeedback);

        dbHelper = new DataHelper(this);

        // Lấy product ID
        if (getIntent().hasExtra("product_id")) {
            productId = getIntent().getIntExtra("product_id", -1);
            loadProductDetail(productId);
            loadFeedbacks(productId);
        } else {
            Toast.makeText(this, "Không có thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Nhấn vào để thêm feedback mới
        listViewFeedback.setOnItemClickListener(null); // tránh lỗi click feedback

        listViewFeedback.setOnItemLongClickListener((parent, view, position, id) -> {
            // Tùy chọn sửa nhanh nếu muốn thêm
            return true;
        });

        // Nhấn vào phần trống để thêm mới feedback
        tvEmptyFeedback.setOnClickListener(v -> showAddFeedbackDialog());


        Button btnWriteFeedback = findViewById(R.id.btnWriteFeedback);
        btnWriteFeedback.setOnClickListener(v -> showAddFeedbackDialog());

    }

    private void loadProductDetail(int id) {
        Cursor cursor = dbHelper.getProductById(id);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"));

            tvName.setText(name);
            tvCategory.setText("Danh mục: " + category);
            tvDescription.setText(description);
            tvPrice.setText("Giá: " + price + "đ");
            tvStock.setText("Tồn kho: " + stock);

            Glide.with(this)
                    .load(image)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imgProduct);
        }
        cursor.close();
    }

    private void loadFeedbacks(int productId) {
        feedbacks = new ArrayList<>();
        Cursor feedbackCursor = dbHelper.getFeedbacksByProductId(productId);
        while (feedbackCursor.moveToNext()) {
            int id = feedbackCursor.getInt(feedbackCursor.getColumnIndexOrThrow("feedback_id"));
            int rating = feedbackCursor.getInt(feedbackCursor.getColumnIndexOrThrow("rating"));
            String comment = feedbackCursor.getString(feedbackCursor.getColumnIndexOrThrow("comment"));
            feedbacks.add(new Feedback(id, rating, comment, productId));
        }
        feedbackCursor.close();

        if (feedbacks.isEmpty()) {
            tvEmptyFeedback.setVisibility(View.VISIBLE);
            listViewFeedback.setVisibility(View.GONE);
        } else {
            tvEmptyFeedback.setVisibility(View.GONE);
            listViewFeedback.setVisibility(View.VISIBLE);
        }

        adapter = new FeedbackAdapter(this, feedbacks, dbHelper);
        listViewFeedback.setAdapter(adapter);
    }

    private void showAddFeedbackDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_feedback, null);
        EditText edtComment = view.findViewById(R.id.edtComment);
        RatingBar ratingInput = view.findViewById(R.id.ratingInput);

        new AlertDialog.Builder(this)
                .setTitle("Thêm đánh giá mới")
                .setView(view)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String comment = edtComment.getText().toString();
                    int rating = Math.round(ratingInput.getRating());

                    if (comment.isEmpty() || rating == 0) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long id = dbHelper.insertFeedback(rating, comment, productId);
                    if (id != -1) {
                        feedbacks.add(new Feedback((int) id, rating, comment, productId));
                        adapter.notifyDataSetChanged();
                        tvEmptyFeedback.setVisibility(View.GONE);
                        listViewFeedback.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Đã thêm đánh giá", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Lỗi khi thêm", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}
