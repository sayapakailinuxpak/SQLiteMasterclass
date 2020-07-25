package com.eldisprojectexpert.sqlitemasterclass.viewmodel;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.OnLifecycleEvent;

import com.eldisprojectexpert.sqlitemasterclass.model.TodoModel;

import java.util.List;

public interface CommonViewModel extends LifecycleObserver {
    LiveData<List<TodoModel>> getAll(); //return all data from Repository
    LiveData<TodoModel> getTodo(long id); //return all data from Repository based on id this will help if we use Recycler or something that need id of all items
    LiveData<String> getErrorStatus();

    void insert(String title, String content);
    void update(int id, String newTitle);
    void delete(int id);

    //observe the view
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void refreshData();
}
