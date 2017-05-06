package com.example.abilashr.saikailashooty;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.example.abilashr.saikailashooty.data.DataContract;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventFragment extends Fragment {

    FragmentTabHost fragmentTabHost;

    public EventFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        fragmentTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        TabHost.TabSpec tabSpecUpcoming = fragmentTabHost.newTabSpec(getResources().getString(R.string.upcoming)).setIndicator(getResources().getString(R.string.upcoming), null);
        fragmentTabHost.addTab(tabSpecUpcoming, UpcomingFragment.class, null);
        TabHost.TabSpec tabSpecPast = fragmentTabHost.newTabSpec(getResources().getString(R.string.past)).setIndicator(getResources().getString(R.string.past), null);
        fragmentTabHost.addTab(tabSpecPast, PastFragment.class, null);

        final String timeBeforeDatabaseUpdate = getDateTime();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String events = getResources().getString(R.string.events);
        String upcoming = getResources().getString(R.string.upcoming);
        DatabaseReference databaseReference = database.getReference(events + "/" + upcoming);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                Calendar c = Calendar.getInstance();
                for(DataSnapshot year : dataSnapshot.getChildren()) {
                    for(DataSnapshot month : year.getChildren()) {
                        for(DataSnapshot day : month.getChildren()) {
                            String date = day.child(getResources().getString(R.string.date)).getValue(String.class);
                            String name = day.child(getResources().getString(R.string.message)).getValue(String.class);
                             addValuesDatabaseIfnotExist(date, name);
                        }
                    }
                }
                deleteValuesInDatabaseNotFound(timeBeforeDatabaseUpdate);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        upcomingEventSelected();
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(fragmentTabHost.getCurrentTab() == 0) {
                    upcomingEventSelected();
                } else {
                    pastEventSelected();
                }
            }
        });

        return rootView;
    }

    private void deleteValuesInDatabaseNotFound(String dateTime) {
        getActivity().getContentResolver().delete(DataContract.EventEntry.CONTENT_URI, DataContract.EventEntry.COLUMN_TIMESTAMP + "< '" + dateTime+"'", null);
    }

    private Cursor getAllEventsFromDatabase() {
        return getActivity().getContentResolver().query(DataContract.EventEntry.CONTENT_URI, null, null, null, null);
    }

    private void addValuesDatabaseIfnotExist(String date, String name) {
        Cursor eventFromDatabase = getActivity().getContentResolver().query(DataContract.EventEntry.CONTENT_URI, null, DataContract.EventEntry.EVENT_NAME + "= '" + name + "' AND " + DataContract.EventEntry.EVENT_DATE + "= '" + date + "'", null, null);
        ContentValues contentValues = new ContentValues();
        if(eventFromDatabase.getCount() != 0) {
            contentValues.put(DataContract.EventEntry.COLUMN_TIMESTAMP, getDateTime());
        } else {
            contentValues.put(DataContract.EventEntry.EVENT_NAME, name);
            contentValues.put(DataContract.EventEntry.EVENT_DATE, date);
            contentValues.put(DataContract.EventEntry.COLUMN_TIMESTAMP, getDateTime());
            getActivity().getContentResolver().insert(DataContract.EventEntry.CONTENT_URI, contentValues);

        }
    }

    private void pastEventSelected() {
        fragmentTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_selector_selected);
        fragmentTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.upcoming_tab_selector_not_selected);
    }

    private void upcomingEventSelected() {
        fragmentTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_selector_selected);
        fragmentTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.past_tab_selector_not_selected);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
