package com.example.nutigo_prm.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;


import com.example.nutigo_prm.DAO.UserDao;
import com.example.nutigo_prm.DataHelper.AppDatabase;
import com.example.nutigo_prm.Entity.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserViewModel extends AndroidViewModel {

    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<User> getUserByEmailAndPassword(String email, String password) {
        return userDao.getAccountByEmail(email, password);
    }

    public LiveData<User> getProfile(String email) {
        return userDao.getProfileByEmail(email);
    }

    public void insert(User user) {
        executorService.execute(() -> userDao.insert(user));
    }

    public void update(User user) {
        executorService.execute(() -> userDao.update(user));
    }

    public void updateUsernameByEmail(String email, String newUsername) {
        executorService.execute(() -> userDao.updateUsernameByEmail(email, newUsername));
    }

    public void delete(User user) {
        executorService.execute(() -> userDao.delete(user));
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUserByEmail(String email) {return  userDao.getUserByEmail(email);}
}
