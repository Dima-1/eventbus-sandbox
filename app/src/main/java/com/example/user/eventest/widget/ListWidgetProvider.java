package com.example.user.eventest.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.example.user.eventest.EventsData;
import com.example.user.eventest.Memo;
import com.example.user.eventest.R;

import java.util.ArrayList;

/**
 * Created on 19.12.2017.
 */

class ListWidgetProvider implements RemoteViewsFactory {
    private ArrayList<Memo> eventItemList = new ArrayList<>();
    private Context context = null;
    private int appWidgetId;
    private EventsData eventsData;

    ListWidgetProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        eventsData = new EventsData(context);

    }

    private void populateListItem() {
        eventItemList.addAll(eventsData.getAllData());
    }

    @Override
    public void onCreate() {
        populateListItem();

    }

    @Override
    public void onDataSetChanged() {
        eventItemList.clear();
        populateListItem();

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
                context.getPackageName(), R.layout.widget_list_row);
        Memo listItem = eventItemList.get(position);
        remoteView.setTextViewText(R.id.tvDate, listItem.getDateString());
        remoteView.setTextViewText(R.id.tvTime, listItem.getTimeString());
        remoteView.setTextViewText(R.id.tvContent, listItem.getNote());

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

}
