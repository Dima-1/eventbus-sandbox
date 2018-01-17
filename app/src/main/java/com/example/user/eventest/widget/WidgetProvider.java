package com.example.user.eventest.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.user.eventest.MainActivity;
import com.example.user.eventest.R;

import java.util.Arrays;

public class WidgetProvider extends AppWidgetProvider {
    static final String TAG = "TestWidgetLogs";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d(TAG, "onUpdate " + Arrays.toString(appWidgetIds));
        for (int widgetID : appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetID);
        }
    }

    void updateWidget(Context context, AppWidgetManager appWidgetManager,
                      int widgetID) {
        try {
            Context mainAppContext = context
                    .createPackageContext("com.example.user.eventest", 0);
            SharedPreferences pref = PreferenceManager
                    .getDefaultSharedPreferences(mainAppContext);
            boolean prefTestState = pref.getBoolean(MainActivity.PREF_TEST_STATE, false);
            Log.d(TAG, MainActivity.PREF_TEST_STATE + " " + prefTestState);
            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
            widgetView.setTextViewText(R.id.tvCheck, String.valueOf(prefTestState));

            Intent updateIntent = new Intent(context, WidgetProvider.class);
            updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{widgetID});
            appWidgetManager.updateAppWidget(widgetID, widgetView);

            RemoteViews remoteViews = updateWidgetListView(context, widgetID);
            appWidgetManager.updateAppWidget(widgetID,
                    remoteViews);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Not data shared", e.toString());
        }
    }

    private RemoteViews updateWidgetListView(Context context,
                                             int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews =
                new RemoteViews(context.getPackageName(), R.layout.widget);
        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(R.id.lvItemList, svcIntent);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.lvItemList, R.id.tvBackground);
        return remoteViews;
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(TAG, "onDisabled");
    }
}
