package com.welpenapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.welpenapp.db.generic.DbHelper;

public class Speltak extends DbHelper<Speltak> {

    private static final String TABLE_NAME = "speltak";

    private static final String COL_ID = BaseColumns._ID;
    private static final String COL_NAME = "Name";

    private static final String[] COLUMNS_CREATE = new String[] {
        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT",
        COL_NAME + " TEXT"
    };

    private static final String[] COLUMNS = new String[] {
        COL_ID,
        COL_NAME
    };

    private int id;
    private String name;

    public Speltak() {
        super();
    }

    public Speltak(SQLiteOpenHelper db) {
        super(db);
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String[] getColumnsCreate() {
        return COLUMNS_CREATE;
    }

    public String[] getColumns() {
        return COLUMNS;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDb) {
        super.onCreate(sqLiteDb);

        // Init sample data
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, "Bevers");
        sqLiteDb.insert(getTableName(), COL_ID, cv);

        cv = new ContentValues();
        cv.put(COL_NAME, "Welpen");
        sqLiteDb.insert(getTableName(), COL_ID, cv);
    }

    @Override
    public Speltak getFromCursor(Cursor c) {
        Speltak speltak = new Speltak(getDb());

        speltak.id = c.getInt(c.getColumnIndex(COL_ID));
        speltak.name = c.getString(c.getColumnIndex(COL_NAME));

        return speltak;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}