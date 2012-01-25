package com.welpenapp.model;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ModelHelper extends SQLiteOpenHelper {

    // Database definition
    private static final String DATABASE_NAME = "WelpenApp";
    private static final int DATABASE_VERSION = 1;

    // Column references
    private static final List<Class<? extends DbObject>> TABLES = new LinkedList<Class<? extends DbObject>>();
    static {
        TABLES.add(Person.class);
    }

    public ModelHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // For all tables, create them
        for (Class<? extends DbObject> clazz : TABLES) {
            DbObject instance = getDbObject(clazz);
            instance.onCreate(arg0);
        }
    }

    /**
     * <p>Helper method to hide Exceptions...</p>
     * 
     * @param clazz
     * @return instantiated {@link DbObject}
     */
    private DbObject getDbObject(Class<? extends DbObject> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        for (Class<? extends DbObject> clazz : TABLES) {
            DbObject instance = getDbObject(clazz);
            instance.onUpgrade(arg0, arg1, arg2);
        }
    }

}
