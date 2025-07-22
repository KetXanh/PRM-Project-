package com.example.nutigo_prm.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.nutigo_prm.DAO.OrderDao;
import com.example.nutigo_prm.DataHelper.AppDatabase;
import com.example.nutigo_prm.Entity.Order;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class OrderViewModel extends AndroidViewModel {
        private final OrderDao orderDao;
        private final ExecutorService executorService;

        public OrderViewModel(Application application) {
            super(application);
            AppDatabase db = AppDatabase.getInstance(application);
            orderDao = db.orderDao();
            executorService = Executors.newSingleThreadExecutor(); // giống ProductViewModel
        }

        public LiveData<List<Order>> getAllOrders() {
            return orderDao.AdminGetAllOrders();
        }

        public void updateOrder(Order order) {
            executorService.execute(() -> orderDao.updateOrder(order));
        }
    }


