package com.eldisprojectexpert.sqlitemasterclass.viewmodel;


import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.eldisprojectexpert.sqlitemasterclass.model.TodoModel;
import com.eldisprojectexpert.sqlitemasterclass.repository.TodoRepository;
import com.eldisprojectexpert.sqlitemasterclass.repository.TodoRepositoryImplementation;

import java.util.ArrayList;
import java.util.List;

public class CommonViewModelImplement extends ViewModel implements CommonViewModel {
    private static final String TAG = "CommonViewModelImplemen";
    private TodoRepository todoRepository;

    private MutableLiveData<List<TodoModel>> mutableLiveDataTodoItems;
    private MutableLiveData<TodoModel> mutableLiveDataTodoModel;
    private MutableLiveData<String> mutableLiveDataErrorMessage;


    //inizializing all member variable
    public CommonViewModelImplement() {
        todoRepository = TodoRepositoryImplementation.getInstance();
        mutableLiveDataTodoItems = new MutableLiveData<>();
        mutableLiveDataErrorMessage = new MutableLiveData<>();
        mutableLiveDataTodoModel = new MutableLiveData<>();

        try{
            mutableLiveDataTodoItems = todoRepository.getAll();
        } catch (Exception e){
            mutableLiveDataTodoItems.setValue(new ArrayList<TodoModel>());
        }
    }

    //expose the data for Views to get rendered in Views as well
    @Override
    public LiveData<List<TodoModel>> getAll() {
        return mutableLiveDataTodoItems;
    }

    @Override
    public LiveData<TodoModel> getTodo(long id) {
        return mutableLiveDataTodoModel;
    }

    @Override
    public LiveData<String> getErrorStatus() {
        return mutableLiveDataErrorMessage;
    }

    @Override
    public void insert(String title, String content) {
        try {
            todoRepository.insert(title, content);
            mutableLiveDataTodoItems.setValue(todoRepository.getAll().getValue());
        }catch (Exception e){
            mutableLiveDataErrorMessage.setValue(e.getMessage());
        }
    }

    @Override
    public void update(int id, String newTitle) {
        try{
            todoRepository.update(id, newTitle);
            mutableLiveDataTodoModel = todoRepository.getToDo(id);  //after updating data, we expose the newest TodoModel
        }catch (Exception e){
            mutableLiveDataErrorMessage.setValue(e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        try {
            todoRepository.delete(id);
            mutableLiveDataTodoModel.setValue(null); //set to null because this is deleted
        }catch (Exception e){
            mutableLiveDataErrorMessage.setValue(e.getMessage());
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    @Override
    public void refreshData() {
        try{
            mutableLiveDataTodoItems.setValue(todoRepository.getAll().getValue());
        }catch (Exception e){
            mutableLiveDataErrorMessage.setValue(e.getMessage());
        }
    }
}
