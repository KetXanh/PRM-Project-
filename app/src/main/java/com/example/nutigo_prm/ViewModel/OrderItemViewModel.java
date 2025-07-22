package com.example.nutigo_prm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nutigo_prm.DAO.OrderItemDao;
import com.example.nutigo_prm.DataHelper.AppDatabase;
import com.example.nutigo_prm.Entity.OrderItem;

import java.util.List;

public class OrderItemViewModel extends AndroidViewModel {
    private final OrderItemDao orderItemDao;

    public OrderItemViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        orderItemDao = db.orderItemDao();
    }

    public LiveData<List<OrderItem>> getOrderItems(int orderId) {
        return orderItemDao.getItemsByOrderId(orderId);
    }
}