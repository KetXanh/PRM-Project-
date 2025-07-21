package com.example.nutigo_prm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nutigo_prm.Activity.EditProductActivity;
import com.example.nutigo_prm.Entity.Category;
import com.example.nutigo_prm.Entity.Product;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.ViewModel.CategoryViewModel;
import com.example.nutigo_prm.ViewModel.ProductViewModel;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {

    private final Context context;
    private final List<Product> productList;
    private final ProductViewModel productViewModel;
    private final Map<Integer, String> categoryMap;

    public AdminProductAdapter(Context context, List<Product> productList, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.productList = productList;
        this.productViewModel = new ViewModelProvider((androidx.lifecycle.ViewModelStoreOwner) context).get(ProductViewModel.class);
        this.categoryMap = new HashMap<>();

        // Load category names
        CategoryViewModel categoryViewModel = new ViewModelProvider((androidx.lifecycle.ViewModelStoreOwner) context).get(CategoryViewModel.class);
        categoryViewModel.getAllCategories().observe(lifecycleOwner, categories -> {
            categoryMap.clear();
            for (Category category : categories) {
                categoryMap.put(category.getId(), category.getName());
            }
            notifyDataSetChanged();
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvCategory;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvCategory = itemView.findViewById(R.id.tvProductCategory);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Product product) {
            tvName.setText(product.getName());

            // Hiển thị giá theo kiểu VNĐ
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            formatter.setMaximumFractionDigits(0); // Không có phần thập phân
            tvPrice.setText(formatter.format(product.getPrice())); // ví dụ: 150.000 ₫

            String categoryName = categoryMap.getOrDefault(product.getCategoryId(), "Không rõ");
            tvCategory.setText(categoryName);

            // Load hình ảnh bằng Glide
            if (product.getImage() != null && !product.getImage().isEmpty()) {
                Glide.with(context)
                        .load(product.getImage())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(imgProduct);
            } else {
                imgProduct.setImageResource(R.drawable.ic_launcher_background);
            }

            // Xử lý nút xóa
            btnDelete.setOnClickListener(v -> {
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Xóa sản phẩm")
                        .setMessage("Bạn có chắc muốn xóa sản phẩm \"" + product.getName() + "\" không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            int position = getAdapterPosition();
                            if (position == RecyclerView.NO_POSITION) {
                                Toast.makeText(context, "Vị trí không hợp lệ", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                productViewModel.delete(product);
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            });

            // Click item để chỉnh sửa
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditProductActivity.class);
                intent.putExtra("product_id", product.getId());
                context.startActivity(intent);
            });
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}