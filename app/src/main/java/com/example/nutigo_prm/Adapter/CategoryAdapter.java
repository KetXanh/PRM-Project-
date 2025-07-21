package com.example.nutigo_prm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nutigo_prm.Activity.EditCategoryActivity;
import com.example.nutigo_prm.Entity.Category;
import com.example.nutigo_prm.R;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private final Context context;
    private final List<Category> categories;
    private final OnCategoryLongClickListener longClickListener;

    public interface OnCategoryLongClickListener {
        void onCategoryLongClick(Category category);
    }

    public CategoryAdapter(Context context, List<Category> categories, OnCategoryLongClickListener longClickListener) {
        super(context, 0, categories);
        this.context = context;
        this.categories = categories;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Category category = categories.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category_admin, parent, false);
        }

        TextView tvCategoryName = convertView.findViewById(R.id.tvCategoryName);
        TextView tvCategoryDescription = convertView.findViewById(R.id.tvCategoryDescription);
        Button btnDeleteCategory = convertView.findViewById(R.id.btnDeleteCategory);

        tvCategoryName.setText(category.getName());
        tvCategoryDescription.setText(category.getDescription());

        // Click để chỉnh sửa
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditCategoryActivity.class);
            intent.putExtra("category_id", category.getId());
            context.startActivity(intent);
        });

        // Click để xóa
        btnDeleteCategory.setOnClickListener(v -> longClickListener.onCategoryLongClick(category));

        return convertView;
    }

}