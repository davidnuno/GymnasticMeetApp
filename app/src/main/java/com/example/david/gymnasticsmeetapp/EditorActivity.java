package com.example.david.gymnasticsmeetapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.LoaderManager;

import com.example.david.gymnasticsmeetapp.data.EventContract;

/**
 * Created by David on 2/22/2017.
 */


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the existing event data
     */
    private final int EXISTING_EVENT = 0;

    /** The save {@link Button} */
    Button saveButton;

    /** EditText field to enter the events name */
    EditText mNameEditText;

    /** EditText field to enter the events details */
    EditText mDetailsEditText;

    /** Content URI for the existing event (null if it's a new event) */
    private Uri mCurrentEventUri;

    /** EditText field to enter the event type */
    private Spinner mEventSpinner;



    @Override
    protected void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentEventUri = intent.getData();

        if (mCurrentEventUri == null) {

            setTitle(R.string.editor_activity_title_new_event);
        } else {

            setTitle(R.string.editor_activity_title_edit_event);

            getLoaderManager().initLoader(EXISTING_EVENT, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText       = (EditText) findViewById(R.id.event_name);
        mDetailsEditText    = (EditText) findViewById(R.id.event_details);

        saveButton = (Button) findViewById(R.id.button_save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(EditorActivity.this, "You clicked the save button",
                        Toast.LENGTH_LONG).show();
            }
        });
        mEventSpinner = (Spinner) findViewById(R.id.spinner_event);
        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {

        ArrayAdapter eventSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.events, android.R.layout.simple_spinner_item);

        eventSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mEventSpinner.setAdapter(eventSpinnerAdapter);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_EVENT_NAME,
                EventContract.EventEntry.COLUMN_EVENT_DETAILS,
                EventContract.EventEntry.COLUMN_EVENT_TYPE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentEventUri,       // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Bail early if the cursor is null or there is less than 1 row in the cursor
        if (data == null || data.getCount() < 1) {
            return;
        }

        if(data.moveToFirst()) {
            int nameColumnIndex     = data.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_NAME);
            int detailsColumnIndex  = data.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_DETAILS);
            int typeColumnIndex     = data.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_TYPE);

            String name     = data.getString(nameColumnIndex);
            String details  = data.getString(detailsColumnIndex);
            int type        = data.getInt(typeColumnIndex);

            //Update the views on the screen with the values from the database.
            mNameEditText.setText(name);
            mDetailsEditText.setText(details);

            switch (type) {
                case EventContract.EventEntry.EVENT_BALANCE_BEAM:
                    mEventSpinner.setSelection(1);
                    break;
                case EventContract.EventEntry.EVENT_FLOOR_EX:
                    mEventSpinner.setSelection(2);
                    break;
                case EventContract.EventEntry.EVENT_POMMEL_HORSE:
                    mEventSpinner.setSelection(3);
                    break;
                case EventContract.EventEntry.EVENT_STILL_RINGS:
                    mEventSpinner.setSelection(4);
                    break;
                case EventContract.EventEntry.EVENT_OTHER:
                    mEventSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mNameEditText.setText("");
        mDetailsEditText.setText("");
        mEventSpinner.setSelection(0); //Select "Other" event
    }
}
