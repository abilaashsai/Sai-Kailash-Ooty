package com.saikailas.ooty.organization;

import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.saikailas.ooty.organization.data.DataContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpcomingFragment extends Fragment {
    Cursor cursor;
    ContentObserver contentObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_upcoming, container, false);
        updateUI(rootView);

        contentObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                updateUI(rootView);
            }
        };
        getActivity().getContentResolver().registerContentObserver(DataContract.EventEntry.CONTENT_URI, false, contentObserver);

        return rootView;
    }

    private void updateUI(View rootView) {
        cursor = getActivity().getContentResolver().query(DataContract.EventEntry.CONTENT_URI, null, DataContract.EventEntry.EVENT_DATE + "> '" + getDateAndTime() + "'", null, DataContract.EventEntry.EVENT_DATE);
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(getContext(), cursor);
        if(cursor.getCount() != 0) {
            cursor.moveToNext();
        }
        ListView listView = (ListView) rootView.findViewById(R.id.upcomingList);
        listView.setAdapter(customCursorAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getContentResolver().unregisterContentObserver(contentObserver);
    }

    private String getDateAndTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
