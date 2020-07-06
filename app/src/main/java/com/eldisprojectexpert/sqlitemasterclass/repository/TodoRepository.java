package com.eldisprojectexpert.sqlitemasterclass.repository;

import androidx.lifecycle.MutableLiveData;

import com.eldisprojectexpert.sqlitemasterclass.TodoModel;

import java.util.List;


public interface TodoRepository {
    MutableLiveData<List<TodoModel>> getAll() throws Exception;
    MutableLiveData<TodoModel> getToDoId(long id) throws Exception;
    void insert(String title, String content) throws Exception;
    void delete(int id) throws Exception;
    void update(int id, String newTitle) throws Exception;
}
