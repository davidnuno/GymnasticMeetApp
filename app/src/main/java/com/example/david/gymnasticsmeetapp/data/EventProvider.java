package com.example.david.gymnasticsmeetapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by David on 2/14/2017.
 */

public class EventProvider extends ContentProvider {

    private static final String LOG_TAG = "Steps => " + EventProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the pets table
     */
    private static final int EVENTS = 100;

    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int EVENT_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // The content URI of the form "content://com.example.android.gymnasticsmeetapp/events" will map to the
        // integer code {@link #EVENTS}. This URI is used to provide access to MULTIPLE rows
        // of the pets table.
        sUriMatcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_EVENTS, EVENTS);
        // The content URI of the form "content://com.example.android.gymnasticsmeetapp/events/#" will map to the
        // integer code {@link #PET_ID}. This URI is used to provide access to ONE single row
        // of the pets table.
        //
        // In this case, the "#" wildcard is used where "#" can be substituted for an integer.
        // For example, "content://com.example.android.gymnasticsmeetapp/events/3" matches, but
        // "content://com.example.android.gymnasticsmeetapp/events" (without a number at the end) doesn't match.
        sUriMatcher.addURI(EventContract.CONTENT_AUTHORITY, EventContract.PATH_EVENTS + "/#", EVENT_ID);
    }

    /**
     * An {@link EventDbHelper} object to connect to the database.
     */
    private EventDbHelper mDbHelper;

    @Override
    public boolean onCreate() {

        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        mDbHelper = new EventDbHelper(getContext());
        Log.v(LOG_TAG, "Created a new EventDbHelper Object.");

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Log.v(LOG_TAG, "query()");
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);

        Log.v(LOG_TAG, "Checking the switch statement for single data or multiple.");
        switch (match) {

            case EVENTS:
                // For the EVENTS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(EventContract.EventEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case EVENT_ID:
                // For the EVENT_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.gymnasticsmeetapp/events/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = EventContract.EventEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(EventContract.EventEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query " + uri);
        }

        //Set notifications URI on the Cursor,
        //so we know what content URI the Cursor was created for.
        //If the data at this URI is changed, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        Log.v(LOG_TAG, "insert()");
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENTS:
                return insertEvent(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert an event into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertEvent(Uri uri, ContentValues values) {

        //String name = values.getAsString(EventContract.EventEntry.COLUMN_EVENT_NAME);

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        Log.v(LOG_TAG, "insertEvent(): Inserting event");
        // Insert the new pet with the given values
        long id = database.insert(EventContract.EventEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //Refresh after inserting data to database.
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted;

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case EVENTS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EVENT_ID:
                // Delete a single row given by the ID in the URI
                selection = EventContract.EventEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case EVENTS:
                return updateEvent(uri, contentValues, selection, selectionArgs);
            case EVENT_ID:
                selection = EventContract.EventEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateEvent(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updateEvent(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(EventContract.EventEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Returns the number of database rows affected by the update statement
        return rowsUpdated;
    }
}
