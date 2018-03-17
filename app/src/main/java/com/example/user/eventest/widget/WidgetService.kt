package com.example.user.eventest.widget

import android.content.Intent
import android.widget.RemoteViewsService

/**
 * Created on 19.12.2017.
 */

class WidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
            ListWidgetProvider(applicationContext, intent)
}
