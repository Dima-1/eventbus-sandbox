package com.example.user.eventest.eventbus.events

import android.util.Log
import java.util.*

/**
 * Created by DR
 * on 01.01.2018.
 */

class DatePickerUpdateEvent(var message: Calendar) {
    init {
        Log.d(TAG, this.javaClass.canonicalName)
    }
}
