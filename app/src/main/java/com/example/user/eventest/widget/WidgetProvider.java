package com.example.user.eventest.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

    static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                             int widgetID) {
        try {
            Context mainAppContext =
                    context.createPackageContext("com.example.user.eventest", 0);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mainAppContext);
            boolean prefTestState = pref.getBoolean(MainActivity.PREF_TEST_STATE, false);
            Log.d(TAG, MainActivity.PREF_TEST_STATE + " " + prefTestState);
            RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
            widgetView.setTextViewText(R.id.tvCheck, String.valueOf(prefTestState));
            Intent updateIntent = new Intent(context, WidgetProvider.class);
            updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{widgetID});
            appWidgetManager.updateAppWidget(widgetID, widgetView);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Not data shared", e.toString());
        }
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
