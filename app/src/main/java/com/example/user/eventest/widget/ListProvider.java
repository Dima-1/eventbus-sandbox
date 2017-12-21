package com.example.user.eventest.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.example.user.eventest.R;

import java.util.ArrayList;

/**
 * Created on 19.12.2017.
 */

class ListProvider implements RemoteViewsFactory {
    private ArrayList<EventItem> eventItemList = new ArrayList<>();
    private Context context = null;
    private int appWidgetId;

    ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem() {
        for (int i = 0; i < 10; i++) {
            EventItem listItem = new EventItem();
            listItem.event_date = "Heading" + i;
            listItem.content = i
                    + " This is the content of the app widget listView. Nice content though";
            eventItemList.add(listItem);
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return eventItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.list_row);
        EventItem listItem = eventItemList.get(position);
        remoteView.setTextViewText(R.id.tv_date, listItem.event_date);
        remoteView.setTextViewText(R.id.content, listItem.content);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


    public class EventItem {
        String event_date, content;
    }
}
