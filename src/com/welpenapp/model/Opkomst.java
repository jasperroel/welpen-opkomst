package com.welpenapp.model;

import java.text.DateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class Opkomst extends DbHelper<Opkomst> implements DbObject {

    public static final String tableName = "Opkomst";

    private static final String colId = BaseColumns._ID;
    private static final String colDate = "Date";

    private SQLiteOpenHelper db;

    // actual values for Opkomst
    private int id;
    private Date date;

    /**
     * <p>Should only be called by {@link ModelHelper}.</p>
     */
    protected Opkomst() {

    }

    /**
     * <p>Default constructor.</p>
     * 
     * @param db
     */
    public Opkomst(SQLiteOpenHelper db) {
        if (null == db) {
            throw new NullPointerException("db cannot be null");
        }

        this.db = db;
    }

    private static final String[] columns = new String[] {
        colId,
        colDate
    };

    private static final String[] columnsCreate = new String[] {
        colId + " INTEGER PRIMARY KEY AUTOINCREMENT",
        colDate + " DATE"
    };

    /**
     * {@inheritDoc}
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getColumnsCreate() {
        return columnsCreate;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDb) {
        super.onCreate(sqLiteDb);

        // Init sample data
        ContentValues cv = new ContentValues();
        cv.put(colDate, new Date(2012, 01, 28).getTime());
        sqLiteDb.insert(getTableName(), colId, cv);

        cv = new ContentValues();
        cv.put(colDate, new Date(2012, 02, 04).getTime());
        sqLiteDb.insert(getTableName(), colId, cv);
    }

    public int getId() {
        return id;
    }

    public String[] getColumns() {
        return columns;
    }

    @Override
    public Opkomst getFromCursor(Cursor c) {
        Opkomst o = new Opkomst(db);
        o.id = c.getInt(c.getColumnIndex(colId));
        o.date = new Date(c.getLong(c.getColumnIndex(colDate)));
        return o;
    }

    @Override
    protected SQLiteOpenHelper getDb() {
        return db;
    }
    
    public Date getDate() {
        return this.date;
    }
}
