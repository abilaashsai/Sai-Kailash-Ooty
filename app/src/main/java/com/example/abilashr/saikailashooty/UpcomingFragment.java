package com.example.abilashr.saikailashooty;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.abilashr.saikailashooty.data.DataContract;

import java.util.ArrayList;

public class UpcomingFragment extends Fragment {
    Cursor cursor;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_upcoming, container, false);

        cursor = getActivity().getContentResolver().query(DataContract.EventEntry.CONTENT_URI, null, null, null, null);
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(getContext(), cursor);
        if(cursor.getCount() != 0) {
            cursor.moveToNext();
        }
        ListView listView = (ListView) rootView.findViewById(R.id.upcomingList);
        listView.setAdapter(customCursorAdapter);
        return rootView;
    }
}
