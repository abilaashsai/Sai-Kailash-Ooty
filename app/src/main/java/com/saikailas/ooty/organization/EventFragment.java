package com.saikailas.ooty.organization;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saikailas.ooty.organization.data.DataContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventFragment extends Fragment {

    FragmentTabHost fragmentTabHost;

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

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String events = getResources().getString(R.string.events);
        String upcoming = getResources().getString(R.string.upcoming);
        DatabaseReference databaseReference = database.getReference(events + "/" + upcoming);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isAdded()) {
                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    Calendar c = Calendar.getInstance();
                    deleteValuesFromDatabase();
                    for(DataSnapshot year : dataSnapshot.getChildren()) {
                        for(DataSnapshot month : year.getChildren()) {
                            for(DataSnapshot day : month.getChildren()) {
                                String date = day.child(getResources().getString(R.string.date)).getValue(String.class);
                                String name = day.child(getResources().getString(R.string.message)).getValue(String.class);
                                String type = day.child(getResources().getString(R.string.type)).getValue(String.class);
                                addValuesDatabase(date, name, type);
                            }
                        }
                    }
                }
            }

            private void deleteValuesFromDatabase() {
                getActivity().getContentResolver().delete(DataContract.EventEntry.CONTENT_URI, null, null);
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

    private Cursor getAllEventsFromDatabase() {
        return getActivity().getContentResolver().query(DataContract.EventEntry.CONTENT_URI, null, null, null, null);
    }

    private void addValuesDatabase(String date, String name, String type) {
        Cursor eventFromDatabase = null;
        if(name.contains("'")) {
            eventFromDatabase = getActivity().getContentResolver().query(DataContract.EventEntry.CONTENT_URI, null, DataContract.EventEntry.EVENT_NAME + "='" + name.replaceAll("'", "''").toString() + "'", null, null);
        } else {
            eventFromDatabase = getActivity().getContentResolver().query(DataContract.EventEntry.CONTENT_URI, null, DataContract.EventEntry.EVENT_NAME + "= '" + name + "'", null, null);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataContract.EventEntry.EVENT_NAME, name);
        contentValues.put(DataContract.EventEntry.EVENT_DATE, getTimstampforDate(date));
        contentValues.put(DataContract.EventEntry.EVENT_TYPE, type);
        contentValues.put(DataContract.EventEntry.COLUMN_TIMESTAMP, getDateAndTime());
        Uri insertedUri = getActivity().getContentResolver().insert(DataContract.EventEntry.CONTENT_URI, contentValues);
        eventFromDatabase.setNotificationUri(getActivity().getContentResolver(), DataContract.EventEntry.CONTENT_URI);
        getActivity().getContentResolver().notifyChange(insertedUri, null);
    }

    private String getTimstampforDate(String dateString) {


        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        DateFormat input = new SimpleDateFormat("dd MMM yyyy");
        Date date = new Date();

        try {
            date = input.parse(dateString);
        } catch(ParseException e) {
            e.printStackTrace();
        }

        return dateFormat.format(date);
    }

    private void pastEventSelected() {
        fragmentTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_selector_selected);
        fragmentTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.upcoming_tab_selector_not_selected);
    }

    private void upcomingEventSelected() {
        fragmentTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_selector_selected);
        fragmentTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.past_tab_selector_not_selected);
    }

    private String getDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
