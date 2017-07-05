package com.saikailas.ooty.organization;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.saikailas.ooty.organization.data.DataContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PastFragment extends Fragment {
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
        final View rootView = inflater.inflate(R.layout.fragment_past, container, false);
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
        cursor = getActivity().getContentResolver().query(DataContract.EventEntry.CONTENT_URI, null, DataContract.EventEntry.EVENT_DATE + "< '" + getDateAndTime() + "'", null, DataContract.EventEntry.EVENT_DATE + " DESC");
        final CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(getContext(), cursor);
        if(cursor.getCount() != 0) {
            cursor.moveToNext();
        }
        ListView listView = (ListView) rootView.findViewById(R.id.pastList);
        listView.setAdapter(customCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                Cursor cursor = (Cursor) customCursorAdapter.getItem(position);
                String selectedDate = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String selectedTitle = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String selectedType = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                intent.putExtra(getResources().getString(R.string.date), selectedDate);
                intent.putExtra(getResources().getString(R.string.title), selectedTitle);
                intent.putExtra(getResources().getString(R.string.type), selectedType);
                TextView eventDate = (TextView) view.findViewById(R.id.eventDate);
                TextView eventName = (TextView) view.findViewById(R.id.eventName);

                Pair<View, String> eventTitleTransition = Pair.create((View) eventDate, getResources().getString(R.string.eventDateTransition));
                Pair<View, String> eventDateTransition = Pair.create((View) eventName, getResources().getString(R.string.eventTitleTransition));
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), eventTitleTransition, eventDateTransition);
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });
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
