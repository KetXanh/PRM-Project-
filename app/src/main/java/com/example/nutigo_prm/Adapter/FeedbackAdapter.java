package com.example.nutigo_prm.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.nutigo_prm.DataHelper.DataHelper;
import com.example.nutigo_prm.Entity.Feedback;
import com.example.nutigo_prm.R;

import java.util.List;

public class FeedbackAdapter extends BaseAdapter {

    private Context context;
    private List<Feedback> feedbackList;
    private DataHelper db;

    public FeedbackAdapter(Context context, List<Feedback> feedbackList, DataHelper db) {
        this.context = context;
        this.feedbackList = feedbackList;
        this.db = db;
    }

    @Override
    public int getCount() {
        return feedbackList.size();
    }

    @Override
    public Object getItem(int position) {
        return feedbackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return feedbackList.get(position).getId(); // Lấy ID thực
    }

    static class ViewHolder {
        RatingBar ratingBar;
        TextView tvComment;
        Button btnEdit, btnDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Feedback feedback = feedbackList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
            holder = new ViewHolder();
            holder.ratingBar = convertView.findViewById(R.id.ratingBar);
            holder.tvComment = convertView.findViewById(R.id.tvComment);
            holder.btnEdit = convertView.findViewById(R.id.btnEdit);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ratingBar.setRating(feedback.getRating());
        holder.tvComment.setText(feedback.getComment());

        // Xử lý sửa feedback
        holder.btnEdit.setOnClickListener(v -> showEditDialog(feedback, position));

        // Xử lý xoá feedback
        holder.btnDelete.setOnClickListener(v -> {
            db.deleteFeedback(feedback.getId());
            feedbackList.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Đã xoá đánh giá", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    private void showEditDialog(Feedback feedback, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null);
        EditText edtComment = view.findViewById(R.id.edtComment);
        RatingBar ratingBar = view.findViewById(R.id.ratingInput);

        edtComment.setText(feedback.getComment());
        ratingBar.setRating(feedback.getRating());

        new AlertDialog.Builder(context)
                .setTitle("Sửa đánh giá")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String comment = edtComment.getText().toString();
                    int rating = Math.round(ratingBar.getRating());

                    db.updateFeedback(feedback.getId(), rating, comment);
                    feedback.setComment(comment);
                    feedback.setRating(rating);
                    notifyDataSetChanged();
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}
