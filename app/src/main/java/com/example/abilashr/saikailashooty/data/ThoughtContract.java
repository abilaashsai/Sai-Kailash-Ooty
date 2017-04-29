package com.example.abilashr.saikailashooty.data;

import android.provider.BaseColumns;

public class ThoughtContract {

    public static final class ThoughtEntry implements BaseColumns{
        public static final String TABLE_NAME = "thought";
        public static final String THOUGHT_TITLE = "title";
        public static final String THOUGHT_DETAIL = "detail";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
