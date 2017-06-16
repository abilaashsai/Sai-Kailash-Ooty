package com.saikailash.ooty.saikailashooty.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.saikailash.ooty.saikailashooty.R;
import com.saikailash.ooty.saikailashooty.data.DataContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReminderTasks {

    static Cursor cursor;

    public static void executeTask(final Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(context.getResources().getString(R.string.thought));

        cursor = populateThoughtsFromDatabaseIfExist(context);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firebaseThoughtTitle = dataSnapshot.child(context.getResources().getString(R.string.title)).getValue(String.class);
                String firebaseThoughtDetails = dataSnapshot.child(context.getResources().getString(R.string.data)).getValue(String.class);
                if(cursor.getCount() != 0) {
                    if(!cursor.getString(cursor.getColumnIndex(DataContract.ThoughtEntry.THOUGHT_TITLE)).equals(firebaseThoughtTitle)) {
                        addThoughtsIntoDatabase(firebaseThoughtTitle, firebaseThoughtDetails, context);
                    }
                } else {
                    addThoughtsIntoDatabase(firebaseThoughtTitle, firebaseThoughtDetails, context);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static Cursor populateThoughtsFromDatabaseIfExist(Context context) {
        cursor = context.getContentResolver().query(DataContract.ThoughtEntry.CONTENT_URI, null, null, null, null);
        if(cursor.getCount() != 0) {
            cursor.moveToNext();
        }
        return cursor;
    }

    private static void addThoughtsIntoDatabase(String title, String details, Context context) {
        context.getContentResolver().delete(DataContract.ThoughtEntry.CONTENT_URI, null, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataContract.ThoughtEntry.THOUGHT_TITLE, title);
        contentValues.put(DataContract.ThoughtEntry.THOUGHT_DETAIL, details);
        context.getContentResolver().insert(DataContract.ThoughtEntry.CONTENT_URI, contentValues);
    }
}
