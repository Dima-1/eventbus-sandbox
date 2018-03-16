package com.example.user.eventest.eventbus.events

import android.util.Log

const val TAG = "EventBus"

class MemoAdapterRefreshEvent {
    init {
        Log.d(TAG, this.javaClass.canonicalName)
    }
}
