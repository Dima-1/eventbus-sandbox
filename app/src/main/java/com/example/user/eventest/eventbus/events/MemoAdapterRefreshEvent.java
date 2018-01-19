package com.example.user.eventest.eventbus.events;

import android.util.Log;

public class MemoAdapterRefreshEvent {
    public MemoAdapterRefreshEvent() {
        String TAG = "EventBus";
        Log.d(TAG, this.getClass().getCanonicalName());
    }
}
