package com.example.david.gymnasticsmeetapp;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.david.gymnasticsmeetapp.data.EventContract;

public class MeetCatalogActivity extends AppCompatActivity {

    private static final String LOG_TAG = "Steps => " + MeetCatalogActivity.class.getSimpleName();

    EventCursorAdapter eventCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_catalog);
        Log.v(LOG_TAG, "onCreate");



       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Find the ListVew to populate event data.
        ListView eventList = (ListView) findViewById(R.id.list);

        Log.v(LOG_TAG, "Creating CursosrAdapter");
        eventCursorAdapter = new EventCursorAdapter(this, null);

        Log.v(LOG_TAG, "Setting the adapter to the ListView");
        eventList.setAdapter(eventCursorAdapter);
    }

    private void insertEvent() {

        ContentValues values = new ContentValues();

        values.put(EventContract.EventEntry.COLUMN_EVENT_NAME, "Pole Vault");
        values.put(EventContract.EventEntry.COLUMN_EVENT_DETAILS, "The event details");

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
