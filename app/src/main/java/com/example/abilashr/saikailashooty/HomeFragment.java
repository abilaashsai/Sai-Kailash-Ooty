package com.example.abilashr.saikailashooty;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abilashr.saikailashooty.data.ThoughtContract;
import com.example.abilashr.saikailashooty.data.ThoughtDbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    @BindView(R.id.thoughtitle) TextView thoughtTitle;
    @BindView(R.id.thoughtdetail) TextView thoughtDetail;
    private SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);

        ThoughtDbHelper thoughtDbHelper = new ThoughtDbHelper(getActivity());
        sqLiteDatabase = thoughtDbHelper.getWritableDatabase();
        cursor = retrieveThoughtsFromDatabase();
        if(cursor.getCount() != 0) {
            cursor.moveToNext();
            thoughtTitle.setText(cursor.getString(cursor.getColumnIndex(ThoughtContract.ThoughtEntry.THOUGHT_TITLE)));
            thoughtDetail.setText(cursor.getString(cursor.getColumnIndex(ThoughtContract.ThoughtEntry.THOUGHT_DETAIL)));
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("thought");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firebaseThoughtTitle = dataSnapshot.child("title").getValue(String.class);
                String firebaseThoughtDetails = dataSnapshot.child("data").getValue(String.class);

                if(cursor.getCount() != 0 && cursor.getString(cursor.getColumnIndex(ThoughtContract.ThoughtEntry.THOUGHT_TITLE)).equals(firebaseThoughtTitle)) {

                } else {
                    thoughtTitle.setText(firebaseThoughtTitle);
                    thoughtDetail.setText(firebaseThoughtDetails);
                    addThoughtsIntoDatabase(firebaseThoughtTitle, firebaseThoughtDetails);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    private Cursor retrieveThoughtsFromDatabase() {
        return sqLiteDatabase.query(ThoughtContract.ThoughtEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    private long addThoughtsIntoDatabase(String title, String details) {
        sqLiteDatabase.execSQL("delete from " + ThoughtContract.ThoughtEntry.TABLE_NAME);
        ContentValues contentValues = new ContentValues();
        contentValues.put(ThoughtContract.ThoughtEntry.THOUGHT_TITLE, title);
        contentValues.put(ThoughtContract.ThoughtEntry.THOUGHT_DETAIL, details);
        return sqLiteDatabase.insert(ThoughtContract.ThoughtEntry.TABLE_NAME, null, contentValues);
    }
}
