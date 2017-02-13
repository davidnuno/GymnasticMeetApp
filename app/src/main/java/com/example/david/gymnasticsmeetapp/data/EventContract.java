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

        public static final String COLUMN_EVENT_NAME = "eventName";

        public static final String COLUYM_EVENT_TYPE = "eventType";


    }


}
