package com.example.user.eventest.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created on 19.12.2017.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        int appWidgetId = intent.getIntExtra(
//                AppWidgetManager.EXTRA_APPWIDGET_ID,
//                AppWidgetManager.INVALID_APPWIDGET_ID);

        return (new ListWidgetProvider(this.getApplicationContext(), intent));
    }
}
