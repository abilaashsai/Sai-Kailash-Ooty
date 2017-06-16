package com.saikailash.ooty.saikailashooty.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataContract {
    public static final String AUTHORITY = "com.saikailash.ooty.saikailashooty";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "thought";
    public static final String EVENT_TASKS = "event";

    public static final class ThoughtEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();
        public static final String TABLE_NAME = "thought";
        public static final String THOUGHT_TITLE = "title";
        public static final String THOUGHT_DETAIL = "detail";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

    public static final class EventEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(EVENT_TASKS).build();
        public static final String TABLE_NAME = "event";
        public static final String EVENT_NAME = "name";
        public static final String EVENT_DATE = "date";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
