package com.eldisprojectexpert.sqlitemasterclass.database;

import android.provider.BaseColumns;

//use surrogate key
public class Schema {
    public static class TodoColumns implements BaseColumns{
        public static final String TABLE_NAME = "todo_table";
        public static final String TITLE_COLUMN = "title";
        public static final String CONTENT_COLUMN = "content";


    }
}
