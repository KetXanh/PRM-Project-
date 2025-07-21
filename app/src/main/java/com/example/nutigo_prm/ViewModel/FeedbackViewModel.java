package com.example.nutigo_prm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nutigo_prm.DAO.FeedbackDAO;
import com.example.nutigo_prm.DataHelper.AppDatabase;
import com.example.nutigo_prm.Entity.Feedback;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FeedbackViewModel extends AndroidViewModel {
    private final FeedbackDAO feedbackDAO;
    private final ExecutorService executorService;

    public FeedbackViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        feedbackDAO = db.feedbackDAO();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Feedback>> getAllFeedbacks() {
        return feedbackDAO.getAllFeedbacks();
    }

    public LiveData<List<Feedback>> getFeedbacksByProductId(int productId) {
        return feedbackDAO.getFeedbacksByProductId(productId);
    }

    public LiveData<List<Feedback>> getFeedbacksByUsername(String username) {
        return feedbackDAO.getFeedbacksByUsername(username);
    }

    public LiveData<Feedback> getFeedbackByUserAndProduct(int productId, String username) {
        return feedbackDAO.getFeedbackByUserAndProduct(productId, username);
    }

    public void insert(Feedback feedback) {
        executorService.execute(() -> feedbackDAO.insert(feedback));
    }

    public void update(Feedback feedback) {
        executorService.execute(() -> feedbackDAO.update(feedback));
    }

    public void delete(Feedback feedback) {
        executorService.execute(() -> feedbackDAO.delete(feedback));
    }
}
