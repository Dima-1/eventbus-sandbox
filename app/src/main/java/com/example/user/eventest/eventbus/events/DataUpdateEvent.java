package com.example.user.eventest.eventbus.events;

import android.util.Log;


public class DataUpdateEvent {
    private String message = "DataUpdateEvent";

    public DataUpdateEvent(String message) {
        this.message = message;
        String TAG = "EventBus";
        Log.d(TAG, this.getClass().getCanonicalName());
    }

    public String getMessage() {
        return message;
    }
}

