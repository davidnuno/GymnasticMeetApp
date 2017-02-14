package com.example.david.gymnasticsmeetapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by David on 2/13/2017.
 */

public class EventContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.gymnasticsmeetapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EVENTS = "events";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private EventContract(){}

    public static final class EventEntry implements BaseColumns {

        public static final String TABLE_NAME = "events";

        /**
         * The content URI to access the pet data in the provider.
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EVENTS);

        /**
         * Unique ID number for the pet (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * The event name column for the event table.
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_EVENT_NAME = "eventName";

        /** The event type column for the event table.
         *
         * Type: TEXT
         */
        public static final String COLUMN_EVENT_TYPE = "eventType";

        /** The event details column for the event table.
         *
         * Type: TEXT
         */
        public static final String COLUMN_EVENT_DETAILS = "eventDetails";
    }


}
