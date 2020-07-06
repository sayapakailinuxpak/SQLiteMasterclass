package com.eldisprojectexpert.sqlitemasterclass.repository;

import androidx.lifecycle.MutableLiveData;

import com.eldisprojectexpert.sqlitemasterclass.GlobalApplication;
import com.eldisprojectexpert.sqlitemasterclass.TodoModel;
import com.eldisprojectexpert.sqlitemasterclass.database.DatabaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class TodoRepositoryImplementation implements TodoRepository{
    private static final String TAG = "TodoRepositoryImplement";
    private MutableLiveData<List<TodoModel>>  mutableTodoItems;
    private MutableLiveData<TodoModel> mutableLiveDataTodoModel;
    private List<TodoModel> todoModelList;

    private DatabaseAdapter databaseAdapter;
    private static TodoRepository todoRepositoryInstance = null;

    public static TodoRepository getInstance(){
        if (todoRepositoryInstance == null){
            todoRepositoryInstance = new TodoRepositoryImplementation();
        }
        return todoRepositoryInstance;
    }

    private TodoRepositoryImplementation(){
        this.databaseAdapter = GlobalApplication.getDatabaseAdapter();
        this.mutableTodoItems = new MutableLiveData<>();
        this.mutableTodoItems.setValue(new ArrayList<TodoModel>());
    }

    @Override
    public MutableLiveData<List<TodoModel>> getAll() throws Exception {
        this.todoModelList = databaseAdapter.getAll();
        mutableTodoItems.setValue(this.todoModelList);
        return mutableTodoItems;
    }

    @Override
    public MutableLiveData<TodoModel> getToDoId(long id) throws Exception {
        TodoModel todoModel =  null;
        for (TodoModel todoModel1 : mutableTodoItems.getValue()){
            if (todoModel1.getId() == id){
                todoModel = todoModel1;
                break;
            }
        }

       if (todoModel == null){
           throw new Exception();
       }

       mutableLiveDataTodoModel = new MutableLiveData<>(todoModel);
       return this.mutableLiveDataTodoModel;
    }

    @Override
    public void insert(String title, String content) throws Exception {
        boolean addSuccess = databaseAdapter.insert(title, content);
        if (!addSuccess){
            throw new Exception("Something wrong, I can feel it");
        } else {
            todoModelList = databaseAdapter.getAll();
            mutableTodoItems.setValue(todoModelList);
        }
    }

    @Override
    public void delete(int id) throws Exception {
        boolean deleteSuccess = databaseAdapter.delete(id);
        if (!deleteSuccess){
            throw new Exception("Something wrong, I can feel it");
        } else {
            this.mutableTodoItems.setValue(null);
            this.todoModelList = databaseAdapter.getAll();
            this.mutableTodoItems.setValue(this.todoModelList);
        }
    }

    @Override
    public void update(int id, String newTitle) throws Exception {
        boolean updateSuccess = databaseAdapter.update(id, newTitle);
        if (!updateSuccess){
            throw new Exception("Something wrong, I can feel it");
        } else {
            this.mutableLiveDataTodoModel.setValue(databaseAdapter.getToDoId(id));
            this.mutableTodoItems.setValue(databaseAdapter.getAll());
        }
    }


}
