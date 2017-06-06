package com.example.abilashr.saikailashooty;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.abilashr.saikailashooty.data.DataContract.*;

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

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setContentDescription(getResources().getString(R.string.share_button));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, cursor.getString(cursor.getColumnIndex(ThoughtEntry.THOUGHT_TITLE)));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, cursor.getString(cursor.getColumnIndex(ThoughtEntry.THOUGHT_DETAIL)));
                startActivity(Intent.createChooser(sharingIntent, getActivity().getResources().getString(R.string.ShareVia)));
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(getResources().getString(R.string.thought));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isAdded() && getActivity() != null) {
                    String firebaseThoughtTitle = dataSnapshot.child(getResources().getString(R.string.title)).getValue(String.class);
                    String firebaseThoughtDetails = dataSnapshot.child(getResources().getString(R.string.data)).getValue(String.class);
                    if(cursor.getCount() != 0) {
                        if(!cursor.getString(cursor.getColumnIndex(ThoughtEntry.THOUGHT_TITLE)).equals(firebaseThoughtTitle)) {
                            addThoughtsIntoDatabase(firebaseThoughtTitle, firebaseThoughtDetails);
                            populateThoughtsFromDatabaseIfExist();
                        }
                    } else {
                        addThoughtsIntoDatabase(firebaseThoughtTitle, firebaseThoughtDetails);
                        populateThoughtsFromDatabaseIfExist();
                    }
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
