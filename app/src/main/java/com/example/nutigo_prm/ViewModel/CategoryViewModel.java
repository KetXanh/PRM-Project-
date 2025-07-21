package com.example.nutigo_prm.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nutigo_prm.DAO.CategoryDAO;
import com.example.nutigo_prm.DataHelper.AppDatabase;
import com.example.nutigo_prm.Entity.Category;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryViewModel extends AndroidViewModel {
    private final CategoryDAO categoryDAO;
    private final LiveData<List<Category>> allCategories;
    private final ExecutorService executorService;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        categoryDAO = db.categoryDAO();
        allCategories = categoryDAO.getAllCategories();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<Category> getCategoryById(int id) {
        return categoryDAO.getCategoryById(id);
    }

    public LiveData<List<Category>> searchCategoriesByName(String name) {
        return categoryDAO.searchCategoriesByName("%" + name + "%");
    }

    public void insert(Category category) {
        executorService.execute(() -> categoryDAO.insert(category));
    }

    public void update(Category category) {
        executorService.execute(() -> categoryDAO.update(category));
    }

    public void delete(Category category) {
        executorService.execute(() -> categoryDAO.delete(category));
    }

    public void updateName(int id, String newName) {
        executorService.execute(() -> categoryDAO.updateNameById(id, newName));
    }
}