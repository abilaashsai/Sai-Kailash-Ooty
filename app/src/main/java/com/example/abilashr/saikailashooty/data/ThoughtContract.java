package com.example.abilashr.saikailashooty.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class ThoughtContract {
    public static final String AUTHORITY = "com.example.abilashr.saikailashooty";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "title";

    public static final class ThoughtEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();
        public static final String TABLE_NAME = "thought";
        public static final String THOUGHT_TITLE = "title";
        public static final String THOUGHT_DETAIL = "detail";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
