package com.example.nutigo_prm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nutigo_prm.Activity.ProductDetailActivity;
import com.example.nutigo_prm.R;
import com.example.nutigo_prm.Model.Product;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = products.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        }

        TextView text1 = convertView.findViewById(R.id.tvProductName);
        TextView text2 = convertView.findViewById(R.id.tvProductPrice);
        ImageView imageView = convertView.findViewById(R.id.imgProduct);

        text1.setText(product.name);
        text2.setText("Giá: " + product.price + "đ");

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product_id", product.id);
            context.startActivity(intent);
        });

        // Dùng Glide để load ảnh
        Glide.with(context)
                .load(product.image) // product.image là URL hoặc URI
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);

        return convertView;
    }


}
