package com.example.nutigo_prm.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.nutigo_prm.DAO.FeedbackDAO;
import com.example.nutigo_prm.DataHelper.AppDatabase;
import com.example.nutigo_prm.DataHelper.Constanst;
import com.example.nutigo_prm.Entity.Feedback;
import com.example.nutigo_prm.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedbackAdapter extends BaseAdapter {

    private Context context;
    private List<Feedback> feedbackList;
    private FeedbackDAO feedbackDAO;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public FeedbackAdapter(Context context, List<Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
        this.feedbackDAO = AppDatabase.getInstance(context).feedbackDAO();
    }

    public void setFeedbackList(List<Feedback> feedbackList) {
        this.feedbackList = feedbackList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return feedbackList != null ? feedbackList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return feedbackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return feedbackList.get(position).getId();
    }

    static class ViewHolder {
        TextView tvUsername;
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
            holder.tvUsername = convertView.findViewById(R.id.tvUsername);
            holder.ratingBar = convertView.findViewById(R.id.ratingBar);
            holder.tvComment = convertView.findViewById(R.id.tvComment);
            holder.btnEdit = convertView.findViewById(R.id.btnEdit);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvUsername.setText("Người đánh giá: " + feedback.getUsername());
        holder.ratingBar.setRating(feedback.getRating());
        holder.tvComment.setText(feedback.getComment());

        boolean isOwner = feedback.getUsername().equals(Constanst.user);
        boolean isAdmin = Constanst.user.equalsIgnoreCase("admin@gmail.com");

        if (isOwner || isAdmin) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            holder.btnEdit.setOnClickListener(v -> showEditDialog(feedback));
            holder.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xoá")
                        .setMessage("Bạn có chắc chắn muốn xoá đánh giá này?")
                        .setPositiveButton("Xoá", (dialog, which) -> {
                            executorService.execute(() -> {
                                feedbackDAO.delete(feedback);
                                ((android.app.Activity) context).runOnUiThread(() -> {
                                    feedbackList.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Đã xoá đánh giá", Toast.LENGTH_SHORT).show();
                                });
                            });
                        })
                        .setNegativeButton("Huỷ", null)
                        .show();
            });
        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }

        return convertView;
    }

    private void showEditDialog(Feedback feedback) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_feedback, null);
        EditText edtComment = view.findViewById(R.id.edtComment);
        RatingBar ratingBar = view.findViewById(R.id.ratingInput);

        edtComment.setText(feedback.getComment());
        ratingBar.setRating(feedback.getRating());

        new AlertDialog.Builder(context)
                .setTitle("Sửa đánh giá")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String comment = edtComment.getText().toString().trim();
                    int rating = Math.round(ratingBar.getRating());

                    feedback.setComment(comment);
                    feedback.setRating(rating);
                    feedback.setUsername(Constanst.user);

                    executorService.execute(() -> feedbackDAO.update(feedback));

                    notifyDataSetChanged();
                    Toast.makeText(context, "Đã cập nhật đánh giá", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}
