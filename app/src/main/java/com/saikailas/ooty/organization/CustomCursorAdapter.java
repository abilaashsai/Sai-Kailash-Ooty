package com.saikailas.ooty.organization;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter {
    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.event_name, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView eventDate = (TextView) view.findViewById(R.id.eventDate);
        TextView eventName = (TextView) view.findViewById(R.id.eventName);
        TextView eventType = (TextView) view.findViewById(R.id.eventType);
        String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
        eventDate.setText(date);
        eventName.setText(name);
        eventType.setText(type);
    }
}
