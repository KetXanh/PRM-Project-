package com.example.nutigo_prm.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.nutigo_prm.Adapter.FeedbackAdapter;
import com.example.nutigo_prm.DataHelper.Constanst;
import com.example.nutigo_prm.Entity.Feedback;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.FeedbackViewModel;
import com.example.nutigo_prm.ViewModel.ProductViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvName, tvPrice, tvDescription, tvStock, tvEmptyFeedback;
    private ImageView imgProduct;
    private Button btnWriteFeedback, btnAddToCart;
    private ListView listViewFeedback;

    private FeedbackAdapter feedbackAdapter;
    private FeedbackViewModel feedbackViewModel;
    private ProductViewModel productViewModel;

    private int productId;
    private Feedback existingFeedback = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            Toast.makeText(this, "Lỗi: sản phẩm không tồn tại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Ánh xạ view
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvStock = findViewById(R.id.tvStock);
        imgProduct = findViewById(R.id.imgProduct);
        btnWriteFeedback = findViewById(R.id.btnWriteFeedback);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        listViewFeedback = findViewById(R.id.listViewFeedback);
        tvEmptyFeedback = findViewById(R.id.tvEmptyFeedback);

        // ViewModel
        feedbackViewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        // Adapter
        feedbackAdapter = new FeedbackAdapter(this, new ArrayList<>());
        listViewFeedback.setAdapter(feedbackAdapter);

        // Load sản phẩm
        productViewModel.getProductById(productId).observe(this, product -> {
            if (product != null) {
                tvName.setText(product.getName());

                // Hiển thị giá định dạng Việt Nam
                String formattedPrice = NumberFormat.getNumberInstance(new Locale("vi", "VN")).format(product.getPrice());
                tvPrice.setText("Giá: " + formattedPrice + "₫");

                // Hiển thị mô tả và tồn kho
                tvDescription.setText(product.getDescription());
                tvStock.setText("Tồn kho: " + product.getStock());

                // Hiển thị ảnh sản phẩm nếu có
                if (product.getImage() != null && !product.getImage().isEmpty()) {
                    Glide.with(this).load(product.getImage()).into(imgProduct);
                } else {
                    imgProduct.setImageResource(R.drawable.ic_launcher_background); // ảnh mặc định nếu thiếu
                }
            }
        });

        // Load feedback
        feedbackViewModel.getFeedbacksByProductId(productId).observe(this, feedbacks -> {
            feedbackAdapter.setFeedbackList(feedbacks);
            tvEmptyFeedback.setVisibility(feedbacks.isEmpty() ? View.VISIBLE : View.GONE);

            // Tìm feedback của user hiện tại
            for (Feedback fb : feedbacks) {
                if (fb.getUsername().equals(Constanst.user)) {
                    existingFeedback = fb;
                    break;
                }
            }
        });

        // Sự kiện đánh giá
        btnWriteFeedback.setOnClickListener(v -> showFeedbackDialog());
    }

    private void showFeedbackDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_feedback, null);
        EditText edtComment = dialogView.findViewById(R.id.edtComment);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingInput);

        if (existingFeedback != null) {
            edtComment.setText(existingFeedback.getComment());
            ratingBar.setRating(existingFeedback.getRating());
        }

        new AlertDialog.Builder(this)
                .setTitle(existingFeedback == null ? "Thêm đánh giá" : "Cập nhật đánh giá")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String comment = edtComment.getText().toString().trim();
                    int rating = Math.round(ratingBar.getRating());

                    if (comment.isEmpty() || rating == 0) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (existingFeedback == null) {
                        Feedback newFeedback = new Feedback(rating, comment, productId, Constanst.user);
                        feedbackViewModel.insert(newFeedback);
                        Toast.makeText(this, "Đã thêm đánh giá", Toast.LENGTH_SHORT).show();
                    } else {
                        existingFeedback.setComment(comment);
                        existingFeedback.setRating(rating);
                        feedbackViewModel.update(existingFeedback);
                        Toast.makeText(this, "Đã cập nhật đánh giá", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}
