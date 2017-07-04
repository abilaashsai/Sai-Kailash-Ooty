package com.saikailas.ooty.organization.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import static com.saikailas.ooty.organization.data.DataContract.*;

public class DataContentProvider extends android.content.ContentProvider {
    private DbHelper dbHelper;
    public static final int THOUGHT_ID = 101;
    public static final int THOUGHT = 200;
    public static final int EVENT = 102;
    public static final int EVENT_ID = 202;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_TASKS, THOUGHT);
        uriMatcher.addURI(AUTHORITY, PATH_TASKS + "/#", THOUGHT_ID);
        uriMatcher.addURI(AUTHORITY, EVENT_TASKS, EVENT);
        uriMatcher.addURI(AUTHORITY, EVENT_TASKS + "/#", EVENT_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortorder) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        if(uri.equals(ThoughtEntry.CONTENT_URI)) {
            return sqLiteDatabase.query(ThoughtEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortorder);
        }
        return sqLiteDatabase.query(EventEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortorder);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        if(match == THOUGHT) {
            sqLiteDatabase.insert(ThoughtEntry.TABLE_NAME, null, contentValues);
        }
        if(match == EVENT) {
            sqLiteDatabase.insert(EventEntry.TABLE_NAME, null, contentValues);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        int match = sUriMatcher.match(uri);
        if(match == THOUGHT) {
            sqLiteDatabase.execSQL("delete from " + ThoughtEntry.TABLE_NAME);
        }
        if(match == EVENT) {
            sqLiteDatabase.delete(EventEntry.TABLE_NAME,s,strings);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int rowsUpdated = 0;

        int match = sUriMatcher.match(uri);
        if(match == EVENT) {
            rowsUpdated = sqLiteDatabase.update(EventEntry.TABLE_NAME, contentValues, s, strings);
        }
        return rowsUpdated;
    }
}
