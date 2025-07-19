package com.example.nutigo_prm.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.nutigo_prm.DataHelper.AppDatabase;
import com.example.nutigo_prm.DAO.OrderDao;
import com.example.nutigo_prm.Entity.Order;

import java.util.List;

public class OrderViewModel extends AndroidViewModel {
    private OrderDao orderDao;

    public OrderViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        orderDao = db.orderDao();
    }

    public LiveData<List<Order>> getOrdersByUserEmail(String userEmail) {
        return orderDao.getOrdersByUserEmail(userEmail);
    }
}