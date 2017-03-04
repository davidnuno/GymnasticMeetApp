package com.example.david.gymnasticsmeetapp;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.LoaderManager;

import com.example.david.gymnasticsmeetapp.data.EventContract;

import static com.example.david.gymnasticsmeetapp.data.EventContract.EventEntry.EVENT_BALANCE_BEAM;
import static com.example.david.gymnasticsmeetapp.data.EventContract.EventEntry.EVENT_FLOOR_EX;
import static com.example.david.gymnasticsmeetapp.data.EventContract.EventEntry.EVENT_OTHER;
import static com.example.david.gymnasticsmeetapp.data.EventContract.EventEntry.EVENT_POMMEL_HORSE;
import static com.example.david.gymnasticsmeetapp.data.EventContract.EventEntry.EVENT_STILL_RINGS;

/**
 * Created by David on 2/22/2017.
 */


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String LOG_TAG = "Steps => " + EditorActivity.class.getSimpleName();

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

    /**
     * Type of event. The possible values are:
     * 0 for other, 1 for , 2 for , 3 for ,
     * 4 for , 5 for .
     */
    private int mEventType;

    /**
     * Has Event changed
     */
    private boolean mEventHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mEventHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentEventUri = intent.getData();

        if (mCurrentEventUri == null) {

            //If new Event set the screen title to New Event.
            setTitle(R.string.editor_activity_title_new_event);
        } else {

            //If existing Event set the screen title to Edit Event.
            setTitle(R.string.editor_activity_title_edit_event);

            getLoaderManager().initLoader(EXISTING_EVENT, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.event_editor_name);
        mDetailsEditText = (EditText) findViewById(R.id.event_editor_details);
        mEventSpinner = (Spinner) findViewById(R.id.spinner_event);

        mNameEditText.setOnTouchListener(mTouchListener);
        mDetailsEditText.setOnTouchListener(mTouchListener);
        mEventSpinner.setOnTouchListener(mTouchListener);

        //Setup the Save button to save the event.
        saveButton = (Button) findViewById(R.id.button_save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveEvent();
                finish();
            }
        });

        setupSpinner();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {

        if (!mEventHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the event.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveEvent() {

        String nameString = mNameEditText.getText().toString().trim();
        String detailsString = mDetailsEditText.getText().toString().trim();

        // Check if this is supposed to be a new event
        // and check if all the fields in the editor are blank
        if (mCurrentEventUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(detailsString)){return;}

        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.COLUMN_EVENT_NAME, nameString);
        values.put(EventContract.EventEntry.COLUMN_EVENT_DATE, detailsString);
        values.put(EventContract.EventEntry.COLUMN_EVENT_TYPE, mEventType);

        if (mCurrentEventUri == null) {

            // Insert a new event into the provider, returning the content URI for the new event.
            Uri newUri = getContentResolver().insert(EventContract.EventEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                Log.v(LOG_TAG, "Error with saving event" + newUri);
                // Show a toast message depending on whether or not the insertion was successful.
                //"Error with saving event"
                Toast.makeText(this, getString(R.string.editor_insert_event_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_event_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING event, so update the pet with content URI: mCurrentEventUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentEventUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentEventUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                //If no rows were affected, then there was an error with the update.
                Toast.makeText(this, "Error with updating event", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {

        ArrayAdapter eventSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.events, android.R.layout.simple_spinner_item);

        eventSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mEventSpinner.setAdapter(eventSpinnerAdapter);

        mEventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.event_still_rings))) {
                        mEventType = EVENT_STILL_RINGS;
                    } else if (selection.equals(getString(R.string.event_balance_beam))) {
                        mEventType = EVENT_BALANCE_BEAM;
                    } else if (selection.equals(getString(R.string.event_floor_exercise))) {
                        mEventType = EVENT_FLOOR_EX;
                    } else if (selection.equals(getString(R.string.event_pommel_horse))) {
                        mEventType = EVENT_POMMEL_HORSE;
                    } else {
                        mEventType = EVENT_OTHER; // Other
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                mEventType = 0; // Other
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_EVENT_NAME,
                EventContract.EventEntry.COLUMN_EVENT_DATE,
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
            int nameColumnIndex = data.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_NAME);
            int dateColumnIndex = data.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_DATE);
            int typeColumnIndex = data.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_TYPE);

            //Extract out the value from the Cursor for the given index
            String name = data.getString(nameColumnIndex);
            String date = data.getString(dateColumnIndex);
            int type = data.getInt(typeColumnIndex);

            //Update the views on the screen with the values from the database.
            mNameEditText.setText(name);
            mDetailsEditText.setText(date);

            switch (type) {
                case EVENT_BALANCE_BEAM:
                    mEventSpinner.setSelection(1);
                    break;
                case EVENT_FLOOR_EX:
                    mEventSpinner.setSelection(2);
                    break;
                case EVENT_POMMEL_HORSE:
                    mEventSpinner.setSelection(3);
                    break;
                case EVENT_STILL_RINGS:
                    mEventSpinner.setSelection(4);
                    break;
                case EVENT_OTHER:
                    mEventSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        Log.v(LOG_TAG, "onLoaderReset");
        mNameEditText.setText("");
        mDetailsEditText.setText("");
        mEventSpinner.setSelection(0); //Select "Other" event
    }
}
