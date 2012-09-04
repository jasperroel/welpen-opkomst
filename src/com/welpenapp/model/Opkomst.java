package com.welpenapp.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.welpenapp.db.generic.DbHelper;

public class Opkomst extends DbHelper<Opkomst> {

    public static final String TABLE_NAME = "Opkomst";

    private static final String COL_ID = BaseColumns._ID;
    private static final String COL_SPELTAK = "Speltak";
    private static final String COL_DATE = "Date";

    private static final String[] COLUMNS_CREATE = new String[] {
        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT",
        COL_SPELTAK + " INTEGER CONSTRAINT fk_" + TABLE_NAME + "_" + COL_SPELTAK + "_id " +
            "REFERENCES " + COL_SPELTAK + "(" + COL_ID + ")",
        COL_DATE + " LONG"
    };

    private static final String[] COLUMNS = new String[] {
        COL_ID,
        COL_SPELTAK,
        COL_DATE
    };

    // actual values for Opkomst
    private int id;
    private Speltak speltak;
    private Calendar date;

    public Opkomst() {
        super();
    }

    public Opkomst(SQLiteOpenHelper db) {
        super(db);
    }

    /**
     * {@inheritDoc}
     */
    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getColumnsCreate() {
        return COLUMNS_CREATE;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDb) {
        super.onCreate(sqLiteDb);

        // Init sample data
        ContentValues cv = new ContentValues();
        cv.put(COL_SPELTAK, 1);
        cv.put(COL_DATE, new GregorianCalendar(2012, 01, 28).getTimeInMillis());
        sqLiteDb.insert(getTableName(), COL_ID, cv);

        cv = new ContentValues();
        cv.put(COL_SPELTAK, 1);
        cv.put(COL_DATE, new GregorianCalendar(2012, 02, 04).getTimeInMillis());
        sqLiteDb.insert(getTableName(), COL_ID, cv);
    }

    public String[] getColumns() {
        return COLUMNS;
    }

    @Override
    public Opkomst getFromCursor(Cursor c) {
        Opkomst o = new Opkomst(getDb());
        o.id = c.getInt(c.getColumnIndex(COL_ID));
        o.speltak = new Speltak(getDb()).get(c.getInt(c.getColumnIndex(COL_SPELTAK)));

        o.date = new GregorianCalendar();
        o.date.setTimeInMillis(c.getLong(c.getColumnIndex(COL_DATE)));

        return o;
    }

    public int getId() {
        return id;
    }

    public Speltak getSpeltak() {
        return speltak;
    }

    public Date getDate() {
        return this.date.getTime();
    }
}