package com.welpenapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * <p>Weak table, holding {@link Person} and {@link Opkomst} together.</p>
 * 
 * @author Jasper Roel
 * 
 */
public class Aanwezigheid extends DbHelper<Aanwezigheid> implements DbObject {

    public enum Status {
        UNKNOWN("Onbekend"),
        AWAY_KNOWN("Afwezig (met kennisgeving"),
        AWAY_UNKNOWN("Afwezig (zonder kennisgeving"),
        PRESENT("Aanwezig");

        private String niceName;

        Status(String niceName) {
            this.niceName = niceName;
        }

        public static Status getByValue(String value) {
            for (Status s : Status.values()) {
                if (s.niceName.equals(value)) {
                    return s;
                }
            }
            return null;
        }

        public String toString() {
            return niceName;
        }
    }

    public static final String tableName = "Aanwezigheid";

    private static final String colId = BaseColumns._ID;

    private static final String colPerson = Person.tableName;
    private static final String colOpkomst = Opkomst.tableName;
    private static final String colStatus = "Status";

    private static final String[] columns = new String[] {
        colId,
        colPerson,
        colOpkomst,
        colStatus
    };

    private static final String[] columnsCreate = new String[] {
        colId + " INTEGER PRIMARY KEY AUTOINCREMENT",
        colPerson + " INTEGER CONSTRAINT fk_" + tableName + "_" + colPerson + "_id " +
            "REFERENCES " + colPerson + "(" + colId + ")",
        colOpkomst + " INTEGER CONSTRAINT fk_" + tableName + "_" + colOpkomst + "_id " +
            "REFERENCES " + colOpkomst + "(" + colId + ")",
        colStatus + " TEXT default '" + Status.UNKNOWN + "'"
    };

    private SQLiteOpenHelper db;

    // actual values for Opkomst
    private int id;
    private Person person;
    private Opkomst opkomst;
    private Status status;

    /**
     * <p>Should only be called by {@link ModelHelper}.</p>
     */
    protected Aanwezigheid() {

    }

    /**
     * <p>Default constructor.</p>
     * 
     * @param db
     */
    public Aanwezigheid(SQLiteOpenHelper db) {
        if (null == db) {
            throw new NullPointerException("db cannot be null");
        }

        this.db = db;
    }

    public String getTableName() {
        return tableName;
    }

    public String[] getColumnsCreate() {
        return columnsCreate;
    }

    public String[] getColumns() {
        return columns;
    }

    public void onCreate(SQLiteDatabase sqLiteDb) {
        super.onCreate(sqLiteDb);
    }

    public static CharSequence[] getStatusValues() {
        Status[] statusses = Status.values();

        CharSequence[] result = new CharSequence[statusses.length];
        int i = 0;
        for (Status s : statusses) {
            result[i++] = s.toString();
        }
        return result;
    }

    public Aanwezigheid get(Person p, Opkomst o) {
        Cursor c = getAsCursorByWhereClause(colPerson + " = " + p.getId() + " AND " + colOpkomst + " = " + o.getId());
        return getFromCursor(c);
    }

    public void add(Person p, Opkomst o, Status s) {
        SQLiteDatabase wdb = db.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(colPerson, p.getId());
        cv.put(colOpkomst, o.getId());
        cv.put(colStatus, s.name());

        wdb.insert(getTableName(), null, cv);
    }

    @Override
    public Aanwezigheid getFromCursor(Cursor c) {
        Aanwezigheid a = new Aanwezigheid(db);
        a.id = c.getInt(c.getColumnIndex(colId));
        a.person = new Person(db).get(c.getInt(c.getColumnIndex(colPerson)));
        a.opkomst = new Opkomst(db).get(c.getInt(c.getColumnIndex(colOpkomst)));
        a.status = Status.valueOf(c.getString(c.getColumnIndex(colStatus)));
        return a;
    }

    @Override
    protected SQLiteOpenHelper getDb() {
        return db;
    }

    public Status getStatus() {
        return this.status;
    }
}
