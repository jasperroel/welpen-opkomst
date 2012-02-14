package com.welpenapp.db.generic;

import android.database.sqlite.SQLiteDatabase;

/**
 * <p>Interface for a single DBObject. This provides the blueprint
 * for any object that offers access to the SQLite database.</p>
 * 
 * @author Jasper Roel
 *
 */
public interface DbObject {

    /**
     * <p>Returns the tableName used for SQLite</p>
     * @return
     */
    public abstract String getTableName();

    /**
     * <p>Returns the {@link String}s necessary to construct the table
     * the first time.</p> 
     * @return String[]
     */
    public String[] getColumnsCreate();
    
    public String[] getColumns();
    
    public void onCreate(SQLiteDatabase arg0);

    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2);
}
