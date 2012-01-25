package com.welpenapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class Person implements DbObject {

    private static final String tableName = "Person";

    private static final String colId = BaseColumns._ID;
    private static final String colLastName = "LastName";
    private static final String colFirstName = "FirstName";
    private static final String colActive = "Active";
    private static final String colFunctie = "Functie";
    private static final String colOvergevolgen = "Overgevlogen";

    private static final String[] columnsCreate = new String[] {
        colId + " INTEGER PRIMARY KEY AUTOINCREMENT",
        colLastName + " TEXT",
        colFirstName + " TEXT NOT NULL",
        colActive + " INTEGER DEFAULT 1",
        colFunctie + " TEXT", // [welp, leiding]
        colOvergevolgen + " DATE"
    };

    private static final String[] columns = new String[] {
        colId,
        colLastName,
        colFirstName,
        colActive,
        colFunctie,
        colOvergevolgen
    };

    private SQLiteOpenHelper db;

    // actual values for person...
    private int id;
    private String lastName;
    private String firstName;
    private boolean active;
    private String functie;
    private String overgevlogen;

    /**
     * <p>Should only be called by {@link ModelHelper}.</p>
     */
    protected Person() {

    }

    /**
     * <p>Default constructor.</p>
     * 
     * @param db
     */
    public Person(SQLiteOpenHelper db) {
        if (null == db) {
            throw new NullPointerException("db cannot be null");
        }

        this.db = db;
    }

    public String getTableName() {
        return tableName;
    }

    public String[] getColumns() {
        return columnsCreate;
    }

    public String[] getColumnNames() {
        return columns;
    }

    public void onCreate(SQLiteDatabase sqLiteDb) {
        String tableName = getTableName();
        String[] columns = getColumns();

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

        // SQLiteDatabase wdb = sqLiteDb.getWritableDatabase();
        sqLiteDb.execSQL(sb.toString());

        // Init sample data
        ContentValues cv = new ContentValues();
        cv.put(colFirstName, "Jasper");
        cv.put(colLastName, "Roel");
        cv.put(colActive, 1);
        cv.put(colFunctie, "leiding");
        // cv.put(colOvergevolgen, ???);
        sqLiteDb.insert(getTableName(), colId, cv);
    }

    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    /**
     * <p>Returns a new Person.</p>
     * 
     * @param _id
     * @return
     */
    public Person get(int _id) {

        Cursor c = getAsCursor(_id);
        return getFromCursor(c);
    }

    /**
     * Returns a new person from the cursor
     * 
     * @param c
     * @return
     */
    public Person getFromCursor(Cursor c) {
        Person p = new Person(db);

        p.id = c.getInt(c.getColumnIndex(colId));
        p.lastName = c.getString(c.getColumnIndex(colLastName));
        p.firstName = c.getString(c.getColumnIndex(colFirstName));
        p.active = c.getInt(c.getColumnIndex(colActive)) == 1;
        p.functie = c.getString(c.getColumnIndex(colFunctie));
        p.overgevlogen = c.getString(c.getColumnIndex(colOvergevolgen));

        return p;
    }

    public Cursor getAsCursor(int _id) {
        SQLiteDatabase rdb = db.getReadableDatabase();
        Cursor c = rdb.query(getTableName(), columns, colId + " = " + _id, null, null, null, null);
        try {
        return checkCursor(c);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while retrieve record id " + _id, e);
        }
    }
    
    private Cursor checkCursor(Cursor c) {
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

    public int getId() {
        return id;
    }

    public String getName() {
        if (null != lastName && lastName.length() != 0) {
            return firstName + " " + lastName;
        }
        return firstName;
    }

    public boolean getActive() {
        return active;
    }

    public String getFunctie() {
        return functie;
    }

    public String getOvergevlogen() {
        return overgevlogen;
    }
}