package com.eldisprojectexpert.sqlitemasterclass.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.eldisprojectexpert.sqlitemasterclass.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    public static final String DATABASE_NAME = "user.db";
    public static final int DATABASE_VERSION = 2;

//    version 1
//    private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE " + Schema.TodoColumns.TABLE_NAME + "(" +
//            Schema.TodoColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Schema.TodoColumns.TITLE_COLUMN + " TEXT NOT NULL)";

    private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE " + Schema.TodoColumns.TABLE_NAME + "(" +
            Schema.TodoColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Schema.TodoColumns.TITLE_COLUMN + " TEXT NOT NULL, " +
            Schema.TodoColumns.CONTENT_COLUMN + " TEXT NOT NULL);";

    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private static DatabaseAdapter databaseAdapterInstance;


    //create singleton design pattern
    private DatabaseAdapter(Context context){
        this.context = context;
        sqLiteDatabase = new DatabaseHelper(this.context, DATABASE_NAME, null, DATABASE_VERSION).getWritableDatabase();
    }

    public static DatabaseAdapter getInstance(Context context){
        if (databaseAdapterInstance == null){
            databaseAdapterInstance = new DatabaseAdapter(context);
        }

        return databaseAdapterInstance;
    }


    public boolean insert(String title){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.TodoColumns.TITLE_COLUMN, title);

        return sqLiteDatabase.insert(Schema.TodoColumns.TABLE_NAME, null, contentValues) > 0; //return this if successful

    }

    public boolean insert(String name, String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.TodoColumns.TITLE_COLUMN, name);
        contentValues.put(Schema.TodoColumns.CONTENT_COLUMN, content);

        return sqLiteDatabase.insert(Schema.TodoColumns.TABLE_NAME, null, contentValues) > 0;
    }

    public boolean update(int id, String newTitle){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.TodoColumns.TITLE_COLUMN, newTitle);

        return sqLiteDatabase.update(Schema.TodoColumns.TABLE_NAME, contentValues, Schema.TodoColumns._ID + "=" + id, null) > 0;
    }

    public boolean delete(int id){
        return sqLiteDatabase.delete(Schema.TodoColumns.TABLE_NAME, Schema.TodoColumns._ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<TodoModel> getAll(){
        List<TodoModel> todoModelList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(Schema.TodoColumns.TABLE_NAME,
                null, //we can implicitly use all columns by type null
                null,
                null,
                null,
                null,
                Schema.TodoColumns._ID + " ASC",
                null);

        if (cursor!=null & cursor.getCount() > 0){ //if cursor have value because getCount > 0
            while (cursor.moveToNext()){
                TodoModel todoModel = new TodoModel(cursor.getLong(0), cursor.getString(1), cursor.getString(2)); //because columnIndex of id is 0 and return long, columrIndex title is 1 and return String
                todoModelList.add(todoModel);
            }
        }

            cursor.close();


        return todoModelList;
    }

    public TodoModel getToDoId(long id) throws Exception{
        TodoModel todoModel = null;
        Cursor cursor = sqLiteDatabase.query(Schema.TodoColumns.TABLE_NAME,
                new String[]{Schema.TodoColumns._ID, Schema.TodoColumns.TITLE_COLUMN, Schema.TodoColumns.CONTENT_COLUMN},
                Schema.TodoColumns._ID + "=?",
                new String[]{Long.toString(id)},
                null,
                null,
                Schema.TodoColumns._ID + " ASC",
                null);

        if (cursor != null & cursor.getCount() > 0) {
            cursor.moveToFirst();
            todoModel = new TodoModel(cursor.getLong(0), cursor.getString(1), cursor.getString(2));
        } else {
            throw new Exception("No Task Found");
        }

        return todoModel;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_STATEMENT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            db.execSQL("DROP TABLE IF EXISTS " + Schema.TodoColumns.TABLE_NAME);
//            onCreate(db);
            switch (newVersion){
                case 2 : db.execSQL("ALTER TABLE " + Schema.TodoColumns.TABLE_NAME + " ADD COLUMN " + Schema.TodoColumns.CONTENT_COLUMN + " TEXT");
                break;
            }
        }
    }
}
