package com.eldisprojectexpert.sqlitemasterclass;

import android.app.Application;

import com.eldisprojectexpert.sqlitemasterclass.database.DatabaseAdapter;

public class GlobalApplication extends Application {

    static DatabaseAdapter databaseAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseAdapter = DatabaseAdapter.getInstance(this);
    }

    public static DatabaseAdapter getDatabaseAdapter(){
        return databaseAdapter;
    }
}
