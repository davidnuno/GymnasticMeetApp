package com.example.david.gymnasticsmeetapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David on 2/13/2017.
 */

public class EventDbHelper extends SQLiteOpenHelper {

    /** The database name. */
    private final static String DATABASE_NAME = "events.db";

    /** Database version. If you change the database schema, you must increment the database version. */
    private static final int DATABASE_VERSION = 1;

    public EventDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + EventContract.EventEntry.TABLE_NAME + " ("
                + EventContract.EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EventContract.EventEntry.COLUMN_EVENT_NAME + " TEXT NOT NULL, "
                + EventContract.EventEntry.COLUMN_EVENT_TYPE + " TEXT, "
                + EventContract.EventEntry.COLUMN_EVENT_DETAILS + " TEXT);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
