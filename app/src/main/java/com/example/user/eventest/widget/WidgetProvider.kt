package com.example.user.eventest.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.example.user.eventest.R
import java.util.*

private const val TAG = "TestWidgetLogs"

class WidgetProvider : AppWidgetProvider() {

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d(TAG, "onEnabled")
    }

    @Override
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        Log.d(TAG, "onUpdate " + Arrays.toString(appWidgetIds))
        for (widgetID in appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetID)
        }
    }

    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetID: Int) {
        val updateIntent = Intent(context, WidgetProvider::class.java)
        updateIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(widgetID))
        val remoteViews =
                updateWidgetListView(context, widgetID)
        appWidgetManager.updateAppWidget(widgetID, remoteViews)
        appWidgetManager.notifyAppWidgetViewDataChanged(widgetID, R.id.lvItemList)

    }

    private fun updateWidgetListView(context: Context, appWidgetId: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget)
        //RemoteViews Service needed to provide adapter for ListView
        val svcIntent = Intent(context, WidgetService::class.java)
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.data = Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME))
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(R.id.lvItemList, svcIntent)
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.lvItemList, R.id.tvBackground)
        return remoteViews
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        Log.d(TAG, "onDeleted " + Arrays.toString(appWidgetIds))
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled")
    }
}
