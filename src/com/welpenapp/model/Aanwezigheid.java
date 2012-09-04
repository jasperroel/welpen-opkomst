package com.welpenapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.welpenapp.db.generic.DbHelper;

/**
 * <p>Weak table, holding {@link Person} and {@link Opkomst} together.</p>
 * 
 * @author Jasper Roel
 * 
 */
public class Aanwezigheid extends DbHelper<Aanwezigheid> {

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

    public static final String TABLE_NAME = "Aanwezigheid";

    private static final String COL_ID = BaseColumns._ID;
    private static final String COL_PERSON = Person.tableName;
    private static final String COL_OPKOMST = Opkomst.TABLE_NAME;
    private static final String COL_STATUS = "Status";

    private static final String[] COLUMNS = new String[] {
        COL_ID,
        COL_PERSON,
        COL_OPKOMST,
        COL_STATUS
    };

    private static final String[] COLUMNS_CREATE = new String[] {
        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT",
        COL_PERSON + " INTEGER CONSTRAINT fk_" + TABLE_NAME + "_" + COL_PERSON + "_id " +
            "REFERENCES " + COL_PERSON + "(" + COL_ID + ")",
        COL_OPKOMST + " INTEGER CONSTRAINT fk_" + TABLE_NAME + "_" + COL_OPKOMST + "_id " +
            "REFERENCES " + COL_OPKOMST + "(" + COL_ID + ")",
        COL_STATUS + " TEXT default '" + Status.UNKNOWN + "'"
    };

    // actual values for Opkomst
    private int id;
    private Person person;
    private Opkomst opkomst;
    private Status status;

    public Aanwezigheid() {
        super();
    }

    /**
     * <p>Default constructor.</p>
     * 
     * @param db
     */
    public Aanwezigheid(SQLiteOpenHelper db) {
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
        Cursor c = getAsCursorByWhereClause(COL_PERSON + " = " + p.getId() + " AND " + COL_OPKOMST + " = " + o.getId());
        return getFromCursor(c);
    }

    public void add(Person p, Opkomst o, Status s) {
        SQLiteDatabase wdb = getDb().getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_PERSON, p.getId());
        cv.put(COL_OPKOMST, o.getId());
        cv.put(COL_STATUS, s.name());

        wdb.insert(getTableName(), null, cv);
    }

    @Override
    public Aanwezigheid getFromCursor(Cursor c) {
        Aanwezigheid a = new Aanwezigheid(getDb());
        a.id = c.getInt(c.getColumnIndex(COL_ID));
        a.person = new Person(getDb()).get(c.getInt(c.getColumnIndex(COL_PERSON)));
        a.opkomst = new Opkomst(getDb()).get(c.getInt(c.getColumnIndex(COL_OPKOMST)));
        a.status = Status.valueOf(c.getString(c.getColumnIndex(COL_STATUS)));
        return a;
    }

    public int getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Opkomst getOpkomst() {
        return opkomst;
    }

    public Status getStatus() {
        return this.status;
    }
}
