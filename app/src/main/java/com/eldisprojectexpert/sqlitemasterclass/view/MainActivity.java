package com.eldisprojectexpert.sqlitemasterclass.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.eldisprojectexpert.sqlitemasterclass.R;
import com.eldisprojectexpert.sqlitemasterclass.database.DatabaseAdapter;
import com.eldisprojectexpert.sqlitemasterclass.model.TodoModel;
import com.eldisprojectexpert.sqlitemasterclass.viewmodel.CommonViewModel;
import com.eldisprojectexpert.sqlitemasterclass.viewmodel.CommonViewModelImplement;

import java.util.List;

//PRO TIPS : Spend enough time on Database Design and Database Adapter, it makes wiring up UI code easy and quick

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextNewToDoString, editTextPlace, editTextToDoId, editTextNewToDo, editTextNewContent;
    Button buttonAddToDo, buttonRemoveToDo, buttonUpdateToDo;
    TextView textViewToDos;
    DatabaseAdapter databaseAdapter;
    List<TodoModel> todoModelList;

    //ViewModel
    CommonViewModel commonViewModel;

    TodoModel todoModel = new TodoModel();
    long id = todoModel.getId();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseAdapter = DatabaseAdapter.getInstance(getApplicationContext()); //Initialization should be happen only once in onCreate() of MainActivity
        todoModelList = databaseAdapter.getAll(); //fetch all data
        initUI();

        commonViewModel = new ViewModelProvider(this).get(CommonViewModelImplement.class);
        getLifecycle().addObserver(commonViewModel);
        commonViewModel.getAll().observe(this, new Observer<List<TodoModel>>() {
            @Override
            public void onChanged(List<TodoModel> todoModels) {
                setNewList();
            }
        });


        commonViewModel.getErrorStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(MainActivity.this, "Error message " + s, Toast.LENGTH_LONG).show();
            }
        });

        commonViewModel.getTodo(id).observe(this, new Observer<TodoModel>() {
            @Override
            public void onChanged(TodoModel todoModel) {
                if (todoModel == null){
                    updateViewOnRemove();
                }else {
                    setNewList();
                }
            }
        });
    }

    private void initUI(){
        editTextNewToDoString = findViewById(R.id.editTextNewToDoString);
        editTextPlace = findViewById(R.id.editTextPlace);
        editTextToDoId = findViewById(R.id.editTextToDoId);
        editTextNewToDo = findViewById(R.id.editTextNewToDo);
        editTextNewContent = findViewById(R.id.editTextNewContent);
        buttonAddToDo = findViewById(R.id.buttonAddToDo);
        buttonRemoveToDo = findViewById(R.id.buttonRemoveToDo);
        buttonUpdateToDo = findViewById(R.id.buttonModifyToDo);
        textViewToDos = findViewById(R.id.textViewToDos);
        buttonAddToDo.setOnClickListener(this);
        buttonRemoveToDo.setOnClickListener(this);
        buttonUpdateToDo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buttonAddToDo:
                addNewToDo();
                break;
            case R.id.buttonRemoveToDo:
                deleteToDo();
                break;
            case R.id.buttonModifyToDo:
                updateToDo();
                break;
            default: break;
        }
    }

    private String getDataFromDatabaseAndConvertToStringRepresentation(){
//        todoModelList = databaseAdapter.getAll(); //always fetch data because we can get the newest data
//        if (todoModelList != null && todoModelList.size()>0){
//            StringBuilder stringBuilder = new StringBuilder("");
//            for (TodoModel todoModel : todoModelList){
//                stringBuilder.append(todoModel.getId() + ", " + todoModel.getTitle() + "\n");
//            }
//            return stringBuilder.toString();
//        } else {
//            return "No to items";
//        }

        todoModelList = databaseAdapter.getAll();
        if (todoModelList != null && todoModelList.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder("");
            for (TodoModel todoModel : todoModelList) {
                stringBuilder.append(todoModel.getId() + ", " + todoModel.getTitle() + ", " + todoModel.getContent() + "\n");
            }

            return stringBuilder.toString();
        }else {
            return "No items";
        }
    }

    private void addNewToDo(){
        String title = editTextNewToDoString.getText().toString().trim();
        String content = editTextNewContent.getText().toString().trim();
//        databaseAdapter.insert(title, content);
        commonViewModel.insert(title, content);
        setNewList(); //invoke newest data
    }

    private void deleteToDo(){
        String id = editTextToDoId.getText().toString().trim();
//        databaseAdapter.delete(Integer.parseInt(id));
        commonViewModel.delete(Integer.parseInt(id));

        setNewList();
    }

    private void updateToDo(){
        String id = editTextToDoId.getText().toString().trim();
        String newTitle = editTextNewToDo.getText().toString().trim();
//        databaseAdapter.update(Integer.parseInt(id), newTitle);
        commonViewModel.update(Integer.parseInt(id), newTitle);

        setNewList();
    }

    private void setNewList(){
        textViewToDos.setText(getDataFromDatabaseAndConvertToStringRepresentation());
    }

    private void updateViewOnRemove(){
        Toast.makeText(this, "Successfully remove ", Toast.LENGTH_SHORT).show();
        editTextNewToDo.setText("");
        editTextNewContent.setText("");
        editTextNewToDoString.setText("");
//        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNewList();
    }

    @Override
    protected void onDestroy() { //Remove observers manually
        super.onDestroy();
        commonViewModel.getAll().removeObservers(this);
        commonViewModel.getErrorStatus().removeObservers(this);
        getLifecycle().removeObserver(commonViewModel);
    }
}