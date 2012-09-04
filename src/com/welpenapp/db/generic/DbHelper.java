package com.welpenapp.db.generic;

import java.util.LinkedList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * <p>All models should inherit this!</p>
 * 
 * @author Jasper Roel
 * 
 * @param <T> The implementing class (so <code>XYZ implements DbHelper&lt;XYZ&gt</code>)
 */
public abstract class DbHelper<T> implements DbObject {

    private SQLiteOpenHelper db;

    protected DbHelper() {

    }

    public DbHelper(SQLiteOpenHelper db) {
        if (null == db) {
            throw new NullPointerException("db cannot be null");
        }

        this.db = db;
    }

    public void onCreate(SQLiteDatabase sqLiteDb) {
        String tableName = getTableName();
        String[] columns = getColumnsCreate();

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(tableName);
        sb.append(" (");

        String sep = "";
        for (String column : columns) {
            sb.append(sep);
            sb.append(column);

            if (sep.length() == 0) {
                sep = ", ";
            }
        }

        sb.append(" );");

        sqLiteDb.execSQL(sb.toString());
    }

    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    public T get(int _id) {
        Cursor c = getAsCursor(_id);
        return getFromCursor(c);
    }

    public abstract T getFromCursor(Cursor c);

    protected SQLiteOpenHelper getDb() {
        return db;
    }

    public Cursor getAsCursor(int _id) {
        SQLiteDatabase rdb = getDb().getReadableDatabase();
        Cursor c = rdb.query(getTableName(), getColumns(), BaseColumns._ID + " = " + _id, null, null, null, null);
        try {
            return checkCursorForSingleItem(c);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while retrieve record id " + _id, e);
        }
    }

    protected boolean isCursorSingleItem(Cursor c) {
        if (c.getCount() == 1) {
            return true;
        }
        return false;
    }

    protected Cursor checkCursorForSingleItem(Cursor c) {
        int count = c.getCount();
        if (count == 0) {
            throw new RuntimeException("No records with found in Person");
        }
        if (count > 1) {
            throw new RuntimeException("Multiple records (" + count + ") found in Person");
        }
        c.moveToFirst();

        return c;
    }

    public List<T> getAll() {
        Cursor c = getAllAsCursor();
        return getAllFromCursor(c);
    }

    public Cursor getAllAsCursor() {
        SQLiteDatabase rdb = getDb().getReadableDatabase();
        return rdb.query(getTableName(), getColumns(), null, null, null, null, null);
    }

    public List<T> getAllFromCursor(Cursor c) {
        List<T> result = new LinkedList<T>();

        boolean recordsAvailable = c.moveToFirst();
        while (recordsAvailable) {
            result.add(getFromCursor(c));
            recordsAvailable = c.moveToNext();
        }

        return result;
    }

    public Cursor getAsCursorByColumnAndValue(String columnName, String value) {
        SQLiteDatabase rdb = getDb().getReadableDatabase();
        Cursor c = rdb.query(getTableName(), getColumns(), columnName + " = " + value, null, null, null, null);
        try {
            return checkCursorForSingleItem(c);
        } catch (RuntimeException e) {
            throw new RuntimeException(
                "Error while retrieve record using column " + columnName + " and value " + value, e);
        }
    }

    public Cursor getAsCursorByWhereClause(String whereClause) {
        SQLiteDatabase rdb = getDb().getReadableDatabase();
        Cursor c = rdb.query(getTableName(), getColumns(), whereClause, null, null, null, null);
        try {
            return checkCursorForSingleItem(c);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while retrieve record using whereClause " + whereClause, e);
        }
    }
}