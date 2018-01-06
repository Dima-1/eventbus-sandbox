package com.example.user.eventest.eventbus.events;

import android.util.Log;

import java.util.Calendar;

/*
 * Created by User on 01.01.2018.
 */

public class DatePickerUpdateEvent {
    private Calendar message;

    public DatePickerUpdateEvent(Calendar message) {
        this.message = message;
        String TAG = "EventBus";
        Log.d(TAG, this.getClass().getCanonicalName());
    }

    public Calendar getMessage() {
        return message;
    }
}
