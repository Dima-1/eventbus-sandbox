package com.example.user.eventest.eventbus.events;

import android.util.Log;

/*
 * Created by User on 01.01.2018.
 */

public class DatePickerUpdateEvent {
    private String message = "DatePickerUpdateEvent";

    public DatePickerUpdateEvent(String message) {
        this.message = message;
        String TAG = "EventBus";
        Log.d(TAG, this.getClass().getCanonicalName());
    }

    public String getMessage() {
        return message;
    }
}
