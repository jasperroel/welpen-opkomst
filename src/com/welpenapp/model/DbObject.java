package com.welpenapp.model;

import android.database.sqlite.SQLiteDatabase;

public interface DbObject {

    public abstract String getTableName();

    public String[] getColumns();

    public void onCreate(SQLiteDatabase arg0);

    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2);
}
