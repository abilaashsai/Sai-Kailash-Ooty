package com.example.abilashr.saikailashooty.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ThoughtDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "thought.db";
    private static final int DATABASE_VERSION =1;

    public ThoughtDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_THOUGHT_TABLE = "CREATE TABLE "+
                ThoughtContract.ThoughtEntry.TABLE_NAME + " ("+
                ThoughtContract.ThoughtEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ThoughtContract.ThoughtEntry.THOUGHT_TITLE + " TEXT NOT NULL," +
                ThoughtContract.ThoughtEntry.THOUGHT_DETAIL + " TEXT NOT NULL," +
                ThoughtContract.ThoughtEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP"+
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_THOUGHT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ ThoughtContract.ThoughtEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
