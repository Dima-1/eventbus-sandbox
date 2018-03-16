package com.example.user.eventest.eventbus.events

import android.util.Log


class DataUpdateEvent(message: String) {
    val message = "DataUpdateEvent"

    init {
        Log.d(TAG, this.javaClass.canonicalName)
    }

}

