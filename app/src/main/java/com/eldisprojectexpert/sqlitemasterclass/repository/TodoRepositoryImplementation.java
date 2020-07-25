package com.eldisprojectexpert.sqlitemasterclass.repository;

import androidx.lifecycle.MutableLiveData;

import com.eldisprojectexpert.sqlitemasterclass.GlobalApplication;
import com.eldisprojectexpert.sqlitemasterclass.database.DatabaseAdapter;
import com.eldisprojectexpert.sqlitemasterclass.model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class TodoRepositoryImplementation implements TodoRepository{
    private static final String TAG = "TodoRepositoryImplement";
    private DatabaseAdapter databaseAdapter;
    private List<TodoModel> todoModelList;
    private MutableLiveData<List<TodoModel>> mutableLiveDataTodoItems;
    private MutableLiveData<TodoModel> mutableLiveDataTodoModel;

    private static TodoRepository todoRepositoryInstance = null;

    public static TodoRepository getInstance(){
        if (todoRepositoryInstance == null){
            todoRepositoryInstance = new TodoRepositoryImplementation();
        }
        return todoRepositoryInstance;
    }

    private TodoRepositoryImplementation(){
        this.databaseAdapter = GlobalApplication.getDatabaseAdapter();
        this.mutableLiveDataTodoItems = new MutableLiveData<>();
        this.mutableLiveDataTodoItems.setValue(new ArrayList<TodoModel>());
    }

    @Override
    public MutableLiveData<List<TodoModel>> getAll() throws Exception {
        this.todoModelList = new ArrayList<>();
        mutableLiveDataTodoItems.setValue(this.todoModelList);
        return mutableLiveDataTodoItems;
    }

    @Override
    public MutableLiveData<TodoModel> getToDo(long id) throws Exception {
        TodoModel todoModel = null;
        for (TodoModel model : mutableLiveDataTodoItems.getValue()){
            if (model.getId() == id){
                todoModel = model;
                break;
            }
        }

        if (todoModel == null){
            throw new Exception("No Task Found");
        }
        mutableLiveDataTodoModel = new MutableLiveData<>(todoModel);
        return this.mutableLiveDataTodoModel;
    }

    @Override
    public void insert(String title, String content) throws Exception {
        boolean addSuccess = databaseAdapter.insert(title, content);
        if (!addSuccess){
            throw new Exception("Something wrong, i can feel it");
        } else {
            todoModelList = databaseAdapter.getAll(); //get the newest data
            mutableLiveDataTodoItems.setValue(todoModelList);
        }
    }

    @Override
    public void update(int id, String newTitle) throws Exception {
        boolean updateSuccess = databaseAdapter.update(id, newTitle);
        if (!updateSuccess){
            throw new Exception("id is wrong");
        } else {
            this.mutableLiveDataTodoModel.setValue(databaseAdapter.getTodo(id));
            this.mutableLiveDataTodoItems.setValue(databaseAdapter.getAll());
        }
    }

    @Override
    public void delete(int id) throws Exception {
        boolean deleteSuccess = databaseAdapter.delete(id);
        if (!deleteSuccess){
            throw new Exception("id is wrong");
        } else {
            this.mutableLiveDataTodoModel.setValue(null);
            this.todoModelList = databaseAdapter.getAll();
            this.mutableLiveDataTodoItems.setValue(this.todoModelList);
        }
    }
}
