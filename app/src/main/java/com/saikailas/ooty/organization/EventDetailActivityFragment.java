package com.saikailas.ooty.organization;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventDetailActivityFragment extends Fragment {

    @BindView(R.id.eventTitleDate)
    TextView eventTitleDate;

    @BindView(R.id.eventTitleName)
    TextView eventTitleName;

    @BindView(R.id.eventDetail)
    TextView eventDetail;

    public EventDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, rootView);

        final String dateString = getActivity().getIntent().getStringExtra(getResources().getString(R.string.date));
        final String name = getActivity().getIntent().getStringExtra(getResources().getString(R.string.title));

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String events = getResources().getString(R.string.events);
        String upcoming = getResources().getString(R.string.upcoming);
        DatabaseReference databaseReference = database.getReference(events + "/" + upcoming + "/" + getReferenceDate(dateString));

        eventTitleDate.setText(getDateDisplayFormat(dateString));
        eventTitleName.setText(name);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isAdded() && getActivity() != null) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        eventDetail.setText(dataSnapshot.child("detail").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    private String getReferenceDate(String dateString) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "yyyy/M/d";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String dateOutput = null;

        try {
            date = inputFormat.parse(dateString);
            dateOutput = outputFormat.format(date);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return dateOutput;
    }

    private String getDateDisplayFormat(String dateString) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String dateOutput = null;

        try {
            date = inputFormat.parse(dateString);
            dateOutput = outputFormat.format(date);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return dateOutput;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().supportFinishAfterTransition();
        return true;
    }
}
