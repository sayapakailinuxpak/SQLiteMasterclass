package com.eldisprojectexpert.sqlitemasterclass.repository;

import androidx.lifecycle.MutableLiveData;

import com.eldisprojectexpert.sqlitemasterclass.model.TodoModel;

import java.util.List;


public interface TodoRepository {
    MutableLiveData<List<TodoModel>> getAll() throws Exception; //data exposed as MutableLiveData that we can observe and modify it
    MutableLiveData<TodoModel> getToDo(long id) throws Exception;
    void insert(String title, String content) throws Exception;
    void update(int id, String newTitle) throws Exception;
    void delete(int id) throws Exception;
}
