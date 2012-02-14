package com.welpenapp.db.sqlite;

import java.util.LinkedList;
import java.util.List;

import com.welpenapp.db.generic.DbObject;
import com.welpenapp.model.Aanwezigheid;
import com.welpenapp.model.Opkomst;
import com.welpenapp.model.Person;
import com.welpenapp.model.Speltak;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>This is a singleton class, allowing an easy and extendible way
 * for all {@link DbObject}s to interacts with the SQLite datastore.</p>
 * 
 * @author Jasper Roel
 * 
 */
public class ModelHelper extends SQLiteOpenHelper {

    // Database definition
    private static final String DATABASE_NAME = "WelpenApp";
    private static final int DATABASE_VERSION = 1;

    // Column references
    private static final List<Class<? extends DbObject>> TABLES = new LinkedList<Class<? extends DbObject>>();
    static {
        TABLES.add(Person.class);
        TABLES.add(Speltak.class);
        TABLES.add(Opkomst.class);
        TABLES.add(Aanwezigheid.class);
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

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        for (Class<? extends DbObject> clazz : TABLES) {
            DbObject instance = getDbObject(clazz);
            instance.onUpgrade(arg0, arg1, arg2);
        }
    }

    /**
     * <p>Helper method to hide Exceptions creating a new object.</p>
     * 
     * <p>If an exception does occur, this will throw a {@link RuntimeException}
     * with the original exception as the cause.</p>
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
}