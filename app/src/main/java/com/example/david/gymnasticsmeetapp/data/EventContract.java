package com.example.david.gymnasticsmeetapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

import static android.text.style.TtsSpan.GENDER_FEMALE;
import static android.text.style.TtsSpan.GENDER_MALE;

/**
 * Created by David on 2/13/2017.
 */

public class EventContract {

    public static final String CONTENT_AUTHORITY = "com.example.david.gymnasticsmeetapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EVENTS = "events";

    //For the competitor table
    public static final String PATH_COMPETITOR = "competitor";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private EventContract(){}

    public static final class EventEntry implements BaseColumns {

        //For the competitor table
        public static final String TABLE_NAME_COMPETITOR = "competitor";

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

        /**
         * Gender of the pet.
         *
         * The only possible values are {@link #EVENT_OTHER}, {@link #EVENT_BALANCE_BEAM},
         * {@link #EVENT_FLOOR_EX}, {@link #EVENT_POMMEL_HORSE},or {@link #EVENT_STILL_RINGS}.
         *
         * Type: INTEGER
         */
        public static final String COLUMN_EVENT_TYPE = "eventType";

        /** The event details column for the event table.
         *
         * Type: TEXT
         */
        public static final String COLUMN_EVENT_DETAILS = "eventDetails";

        /**
         * Possible values for the gender of the pet.
         */
        public static int EVENT_OTHER = 0;
        public static int EVENT_BALANCE_BEAM = 1;
        public static int EVENT_FLOOR_EX = 2;
        public static int EVENT_POMMEL_HORSE = 3;
        public static int EVENT_STILL_RINGS = 4;

        /**
         * Returns whether or not the given event is {@link #EVENT_OTHER}, {@link #EVENT_BALANCE_BEAM},
         * {@link #EVENT_FLOOR_EX}, {@link #EVENT_POMMEL_HORSE}, or {@link #EVENT_STILL_RINGS}.
         */
        public static boolean isValidEvent(int event) {
            return event == EVENT_OTHER || event == EVENT_BALANCE_BEAM || event == EVENT_FLOOR_EX ||
                    event == EVENT_POMMEL_HORSE || event == EVENT_STILL_RINGS;
        }
    }


}
