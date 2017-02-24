package com.example.david.gymnasticsmeetapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import static android.R.attr.duration;

/**
 * Created by David on 2/22/2017.
 */


public class EditorActivity extends AppCompatActivity {

    /**
     * The save {@link Button}
     */
    Button saveButton;
    /**
     * EditText field to enter the pet's gender
     */
    private Spinner mEventSpinner;

    @Override
    protected void onCreate(Bundle savedInstance) {

        super.onCreate(savedInstance);
        setContentView(R.layout.activity_editor);

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
}
