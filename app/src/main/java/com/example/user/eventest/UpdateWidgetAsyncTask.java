package com.example.user.eventest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.user.eventest.eventbus.events.MemoAdapterRefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

class UpdateWidgetAsyncTask extends AsyncTask<Void, Void, Void> {

    private MainView view;

    UpdateWidgetAsyncTask(MainView view) {
        this.view = view;
    }

    @Override
    protected Void doInBackground(Void... params) {
        new WidgetUpdateSubscriber();
        return null;
    }

    class WidgetUpdateSubscriber {

        WidgetUpdateSubscriber() {
            EventBus.getDefault().register(this);
        }

        @Subscribe
        public void onDataUpdateEvent(MemoAdapterRefreshEvent event) {
            String TAG = "event receiver " + this.getClass().getName();
            Log.d(TAG, "WidgetUpdateSubscriber");
            view.updateWidget();
            EventBus.getDefault().unregister(this);
        }

    }
}
