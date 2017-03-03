package com.example.david.gymnasticsmeetapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.david.gymnasticsmeetapp.data.EventContract;

import static android.R.attr.duration;
import static android.R.attr.id;

public class MeetCatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * The log tag.
     */
    private static final String LOG_TAG = "Steps => " + MeetCatalogActivity.class.getSimpleName();

    /**
     * Identifier for the event data loader.
     */
    private static final int EVENT_LOADER = 0;

    /**
     * Adapter for the ListView.
     */
    EventCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_catalog);
        Log.v(LOG_TAG, "onCreate");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MeetCatalogActivity.this, EditorActivity.class);
                Uri currentEventUri = ContentUris.withAppendedId(EventContract.EventEntry.CONTENT_URI, id);

                intent.setData(currentEventUri);
                Log.v(LOG_TAG, "Launching EditorActivity intent");
                startActivity(intent);
            }
        });

        //Find the ListVew to populate event data.
        ListView eventListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        eventListView.setEmptyView(emptyView);

        Log.v(LOG_TAG, "Creating CursosrAdapter");
        mCursorAdapter = new EventCursorAdapter(this, null);

        Log.v(LOG_TAG, "Setting the adapter to the ListView");
        eventListView.setAdapter(mCursorAdapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MeetCatalogActivity.this, EditorActivity.class);
                Log.v(LOG_TAG, "Launching EditorActivity intent");
                startActivity(intent);
            }
        });

        Log.v(LOG_TAG, "Kicking off loader.");
        //Kick off the loader
        getLoaderManager().initLoader(EVENT_LOADER, null, this);
    }

    private void insertEvent() {

        ContentValues values = new ContentValues();

        values.put(EventContract.EventEntry.COLUMN_EVENT_NAME, "Pole Vault");
        values.put(EventContract.EventEntry.COLUMN_EVENT_TYPE, 1);
        values.put(EventContract.EventEntry.COLUMN_EVENT_DATE, "The event details");

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link EventEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access new Events data in the future.
        Log.v(LOG_TAG, "Launching ContentResolver().insert()");
        Uri uri = getContentResolver().insert(EventContract.EventEntry.CONTENT_URI, values);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meet_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            //noinspection SimplifiableIfStatement
            case R.id.action_insert_new_event:
                insertEvent();
                return true;
            case R.id.action_remove_all_events:
                deleteAllEvents();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper method to delete all pets in the database.
     */
    private void deleteAllEvents() {
        int rowsDeleted = getContentResolver().delete(EventContract.EventEntry.CONTENT_URI, null, null);

        Toast.makeText(this, "Removed all events.", Toast.LENGTH_SHORT).show();

        Log.v("CatalogActivity", rowsDeleted + " rows deleted from events database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        Log.v(LOG_TAG, "onCreateLoader");
        // This loader will execute the ContentProvider's query method on a background thread
        String[] projection = {
                EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_EVENT_NAME,
                EventContract.EventEntry.COLUMN_EVENT_TYPE,
                EventContract.EventEntry.COLUMN_EVENT_DATE};

        return new CursorLoader(this,
                EventContract.EventEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        Log.v(LOG_TAG, "onLoadFinished");
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCursorAdapter.swapCursor(null);
    }
}
