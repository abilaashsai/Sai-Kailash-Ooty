package com.example.abilashr.saikailashooty;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abilashr.saikailashooty.data.ThoughtContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.abilashr.saikailashooty.data.ThoughtContract.*;

public class HomeFragment extends Fragment {
    @BindView(R.id.thoughtitle)
    TextView thoughtTitle;
    @BindView(R.id.thoughtdetail)
    TextView thoughtDetail;

    Cursor cursor;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);

        populateThoughtsFromDatabaseIfExist();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(getResources().getString(R.string.thought));

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String firebaseThoughtTitle = dataSnapshot.child(getResources().getString(R.string.title)).getValue(String.class);
                String firebaseThoughtDetails = dataSnapshot.child(getResources().getString(R.string.data)).getValue(String.class);

                if(cursor.getCount() != 0 && !cursor.getString(cursor.getColumnIndex(ThoughtEntry.THOUGHT_TITLE)).equals(firebaseThoughtTitle)) {
                    addThoughtsIntoDatabase(firebaseThoughtTitle, firebaseThoughtDetails);
                    updateUI();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    private void updateUI() {
        thoughtTitle.setText(cursor.getString(cursor.getColumnIndex(ThoughtEntry.THOUGHT_TITLE)));
        thoughtDetail.setText(cursor.getString(cursor.getColumnIndex(ThoughtEntry.THOUGHT_DETAIL)));
    }

    private void populateThoughtsFromDatabaseIfExist() {
        cursor = getContentResolver().query(ThoughtEntry.CONTENT_URI, null, null, null, null);
        if(cursor.getCount() != 0) {
            cursor.moveToNext();
            updateUI();
        }
    }

    private void addThoughtsIntoDatabase(String title, String details) {
        getContentResolver().delete(ThoughtEntry.CONTENT_URI, null, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put(ThoughtEntry.THOUGHT_TITLE, title);
        contentValues.put(ThoughtEntry.THOUGHT_DETAIL, details);
        getContentResolver().insert(ThoughtEntry.CONTENT_URI, contentValues);
    }

    private ContentResolver getContentResolver() {
        return getActivity().getContentResolver();
    }
}
