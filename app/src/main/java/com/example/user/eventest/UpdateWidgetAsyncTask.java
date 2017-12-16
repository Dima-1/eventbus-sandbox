package com.example.user.eventest;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.user.eventest.eventbus.events.DataUpdateEvent;
import com.example.user.eventest.widget.WidgetProvider;

import de.greenrobot.event.EventBus;

class UpdateWidgetAsyncTask extends AsyncTask<Void, Void, Void> {

    private Context context;

    UpdateWidgetAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        WidgetUpdateSubscriber backgroundReceiver = new WidgetUpdateSubscriber();
        return null;
    }

    class WidgetUpdateSubscriber {

        WidgetUpdateSubscriber() {
            EventBus.getDefault().register(this);
        }

        public void onEvent(DataUpdateEvent event) {
            String TAG = "event receiver " + this.getClass().getName();
            Log.d(TAG, event.getMessage());
            updateWidget();
            EventBus.getDefault().unregister(this);
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
}
