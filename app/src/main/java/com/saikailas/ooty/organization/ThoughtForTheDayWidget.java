package com.saikailas.ooty.organization;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.saikailas.ooty.organization.data.DataContract;

public class ThoughtForTheDayWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Cursor cursor) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.thought_for_the_day_widget);
        if(cursor != null) {
            views.setTextViewText(R.id.thoughtitleWidget, cursor.getString(cursor.getColumnIndex(DataContract.ThoughtEntry.THOUGHT_TITLE)));
            views.setTextViewText(R.id.thoughtdetailWidget, cursor.getString(cursor.getColumnIndex(DataContract.ThoughtEntry.THOUGHT_DETAIL)));
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Cursor cursor;
        cursor = populateThoughtsFromDatabaseIfExist(context);
        for(int appWidgetId : appWidgetIds) {
            if(cursor != null) {
                cursor.moveToNext();
            }
            updateAppWidget(context, appWidgetManager, appWidgetId, cursor);
        }
    }

    private Cursor populateThoughtsFromDatabaseIfExist(Context context) {
        Cursor cursor;
        cursor = context.getContentResolver().query(DataContract.ThoughtEntry.CONTENT_URI, null, null, null, null);
        return cursor;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

