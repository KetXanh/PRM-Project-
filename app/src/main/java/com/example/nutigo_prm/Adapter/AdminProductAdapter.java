package com.example.nutigo_prm.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutigo_prm.Activity.EditProductActivity;
import com.example.nutigo_prm.DataHelper.DataHelper;
import com.example.nutigo_prm.Entity.Product;
import com.example.nutigo_prm.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;
    private DataHelper dataHelper;

    public AdminProductAdapter(Context context, List<Product> productList, DataHelper dataHelper) {
        this.context = context;
        this.productList = productList;
        this.dataHelper = dataHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Product product) {
            tvName.setText(product.getName());
            tvPrice.setText("$" + product.getPrice());

            // Load ảnh với Picasso (bỏ qua nếu image là local path)
            if (product.getImage() != null && !product.getImage().isEmpty()) {
                Picasso.get()
                        .load(product.getImage())
                        .placeholder(R.drawable.ic_launcher_background) // bạn cần tạo 1 file ảnh placeholder
                        .error(R.drawable.ic_launcher_foreground) // ảnh lỗi load
                        .into(imgProduct);
            } else {
                imgProduct.setImageResource(R.drawable.ic_launcher_background);
            }

            // Xử lý xóa an toàn
            btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xóa sản phẩm")
                        .setMessage("Bạn có chắc muốn xóa sản phẩm \"" + product.getName() + "\" không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            int position = getAdapterPosition();
                            if (position == RecyclerView.NO_POSITION) {
                                Toast.makeText(context, "Vị trí không hợp lệ", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            try {
                                int deleted = dataHelper.deleteProduct(product.getId());
                                if (deleted > 0) {
                                    productList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, productList.size());
                                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Lỗi khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            });

            // Click item mở EditProductActivity
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditProductActivity.class);
                intent.putExtra("product_id", product.getId());
                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public AdminProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductAdapter.ViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
