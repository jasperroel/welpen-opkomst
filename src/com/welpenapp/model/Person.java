package com.welpenapp.model;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * <p>A person.</p>
 * 
 * @author Jasper Roel
 * @author Sander Roebers
 * 
 */
public class Person extends DbHelper<Person> implements DbObject {

    public static final String tableName = "Person";

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

    protected SQLiteOpenHelper getDb() {
        return db;
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

    @Override
    public void onCreate(SQLiteDatabase sqLiteDb) {
        super.onCreate(sqLiteDb);

        // Init sample data
        ContentValues cv = new ContentValues();
        cv.put(colFirstName, "Jasper");
        cv.put(colLastName, "Roel");
        cv.put(colActive, 1);
        cv.put(colFunctie, "leiding");
        // cv.put(colOvergevolgen, ???);
        sqLiteDb.insert(getTableName(), colId, cv);

        cv = new ContentValues();
        cv.put(colFirstName, "Sander");
        cv.put(colLastName, "Roebers");
        cv.put(colActive, 1);
        cv.put(colFunctie, "leiding");
        // cv.put(colOvergevolgen, ???);
        sqLiteDb.insert(getTableName(), colId, cv);

        cv = new ContentValues();
        cv.put(colFirstName, "Peter");
        cv.put(colLastName, "van Drunen");
        cv.put(colActive, 1);
        cv.put(colFunctie, "leiding");
        // cv.put(colOvergevolgen, ???);
        sqLiteDb.insert(getTableName(), colId, cv);
    }

    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    /**
     * <p>Returns a new Person by his full name.</p>
     * 
     * @param _id
     * @return
     */
    public Person get(String name) {
        Cursor c = getAsCursorByWhereClause(colFirstName + " || ' ' || " + colLastName + " = '" + name + "'");
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

    public int getId() {
        return id;
    }

    /**
     * <p>Return {@link #firstName} and {@link #lastName} with a " " added
     * in between.</p>
     * 
     * <p>Will return just {@link #firstName} is {@link #lastName} is empty.</p>
     * 
     * @return
     */
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