package com.example.abilashr.saikailashooty.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class ThoughtContentProvider extends ContentProvider {
    private ThoughtDbHelper thoughtDbHelper;
    public static final int THOUGHT_ID = 101;
    public static final int THOUGHT = 200;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ThoughtContract.AUTHORITY, ThoughtContract.PATH_TASKS, THOUGHT);
        uriMatcher.addURI(ThoughtContract.AUTHORITY, ThoughtContract.PATH_TASKS + "/#", THOUGHT_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        thoughtDbHelper = new ThoughtDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        final SQLiteDatabase sqLiteDatabase = thoughtDbHelper.getWritableDatabase();
        return sqLiteDatabase.query(ThoughtContract.ThoughtEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase sqLiteDatabase = thoughtDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri = null;
        if(match == THOUGHT) {
            sqLiteDatabase.insert(ThoughtContract.ThoughtEntry.TABLE_NAME, null, contentValues);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        final SQLiteDatabase sqLiteDatabase = thoughtDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        if(match == THOUGHT) {
            sqLiteDatabase.execSQL("delete from " + ThoughtContract.ThoughtEntry.TABLE_NAME);
            Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
