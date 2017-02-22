package com.example.david.gymnasticsmeetapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by David on 2/22/2017.
 */


public class EditorActivity extends AppCompatActivity {

    /**
     * EditText field to enter the pet's gender
     */
    private Spinner mEventSpinner;

    @Override
    protected void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_editor);

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
}
