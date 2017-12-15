package com.example.user.eventest;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.user.eventest.widget.WidgetProvider;

class UpdateWidgetAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    UpdateWidgetAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
//        updateWidget();
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
//        EventBus.getDefault().post(new DataUpdateEvent("onPostExecute"));
    }

    private void updateWidget() {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }
}
