package com.example.david.gymnasticsmeetapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.gymnasticsmeetapp.data.EventContract;

import static android.R.attr.defaultHeight;
import static android.R.attr.type;
import static com.example.david.gymnasticsmeetapp.data.EventContract.EventEntry.EVENT_BALANCE_BEAM;
import static com.example.david.gymnasticsmeetapp.data.EventContract.EventEntry.EVENT_FLOOR_EX;
import static com.example.david.gymnasticsmeetapp.data.EventContract.EventEntry.EVENT_OTHER;
import static com.example.david.gymnasticsmeetapp.data.EventContract.EventEntry.EVENT_POMMEL_HORSE;
import static com.example.david.gymnasticsmeetapp.data.EventContract.EventEntry.EVENT_STILL_RINGS;

/**
 * Created by David on 2/13/2017.
 */

public class EventCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = "Steps => " + EventCursorAdapter.class.getSimpleName();

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.

     */
    public EventCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvEventName = (TextView) view.findViewById(R.id.event_name);
        TextView tvEventDetails = (TextView) view.findViewById(R.id.event_details);
        TextView tvEventType = (TextView) view.findViewById(R.id.event_type);

        int name = cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_NAME);
        int details = cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_DETAILS);
        int type = cursor.getColumnIndex(EventContract.EventEntry.COLUMN_EVENT_TYPE);

        String eventName = cursor.getString(name);
        String eventDetails = cursor.getString(details);
        int eventType = cursor.getInt(type);

        Log.v(LOG_TAG, "Setting the text for TextViews.");
        tvEventName.setText(eventName);
        tvEventDetails.setText(eventDetails);
        tvEventType.setText(determineEventType(eventType));
    }

    /**
     * This method determines the event type based on the int.
     *
     * @param event the type of event as an integer
     * @return the {@link String} event.
     */
    private String determineEventType(int event) {

        //String to hold the event type
        String type = "";

        switch (event) {

            case EVENT_OTHER:
                type = "Event Other";
                break;
            case EVENT_BALANCE_BEAM:
                type = "Balance Beam";
                break;
            case EVENT_FLOOR_EX:
                type = "Floor Exercise";
                break;
            case EVENT_POMMEL_HORSE:
                type = "Pommel Horse";
                break;
            case EVENT_STILL_RINGS:
                type = "Still Rings";
                break;
        }

        //Return the type of event
        return type;
    }
}
