package com.example.david.gymnasticsmeetapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.david.gymnasticsmeetapp.MeetCatalogActivity;

/**
 * Created by David on 2/13/2017.
 */

public class EventDbHelper extends SQLiteOpenHelper {

    private static final String LOT_TAG = "Steps => " + EventDbHelper.class.getSimpleName();

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
                + EventContract.EventEntry.COLUMN_EVENT_TYPE + " INTEGER, "
                + EventContract.EventEntry.COLUMN_EVENT_DATE + " TEXT);";

        Log.v(LOT_TAG, "Created the event.db table.");
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
        Log.v(LOT_TAG, "Executed SQL statement.");
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
