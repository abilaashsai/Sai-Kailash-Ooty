package com.example.abilashr.saikailashooty.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.abilashr.saikailashooty.data.DataContract.*;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "thought.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_THOUGHT_TABLE = "CREATE TABLE " +
                ThoughtEntry.TABLE_NAME + " (" +
                ThoughtEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ThoughtEntry.THOUGHT_TITLE + " TEXT NOT NULL," +
                ThoughtEntry.THOUGHT_DETAIL + " TEXT NOT NULL," +
                ThoughtEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_THOUGHT_TABLE);

        final String SQL_CREATE_EVENT_TABLE = "CREATE TABLE " +
                EventEntry.TABLE_NAME + " (" +
                EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                EventEntry.EVENT_NAME + " TEXT NOT NULL," +
                EventEntry.EVENT_DATE + " TIMESTAMP NOT NULL," +
                EventEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ThoughtEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
