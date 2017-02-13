package com.example.david.gymnasticsmeetapp;

import android.content.Context;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by David on 2/13/2017.
 */

public class EventCursorAdapter extends CursorAdapter {
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
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
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

        TextView eventName = (TextView) view.findViewById(R.id.event_name);
        TextView eventDetails = (TextView) view.findViewById(R.id.event_details);
    }
}
