package com.example.nutigo_prm.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nutigo_prm.DAO.ProductDAO;
import com.example.nutigo_prm.DataHelper.AppDatabase;
import com.example.nutigo_prm.Entity.Product;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductViewModel extends AndroidViewModel {
    private final ProductDAO productDAO;
    private final LiveData<List<Product>> allProducts;
    private final ExecutorService executorService;


    public ProductViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        productDAO = db.productDao();
        allProducts = productDAO.getAllProducts();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public LiveData<Product> getProductById(int id) {
        return productDAO.getProductById(id);
    }

    public LiveData<List<Product>> getProductsByCategoryId(int categoryId) {
        return productDAO.getProductsByCategoryId(categoryId);
    }

    public LiveData<List<Product>> searchProductsByName(String name) {
        return productDAO.searchProductsByName("%" + name + "%");
    }

    public void insert(Product product) {
        executorService.execute(() -> productDAO.insert(product));
    }

    public void update(Product product) {
        executorService.execute(() -> productDAO.update(product));
    }

    public void delete(Product product) {
        executorService.execute(() -> productDAO.delete(product));
    }

    public void updateStock(int id, int newStock) {
        executorService.execute(() -> productDAO.updateStockById(id, newStock));
    }
}