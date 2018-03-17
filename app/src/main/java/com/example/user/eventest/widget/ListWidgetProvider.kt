package com.example.user.eventest.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import com.example.user.eventest.R
import com.example.user.eventest.model.Memo
import com.example.user.eventest.model.RoomRepository
import java.util.*

/**
 * Created on 19.12.2017.
 */

class ListWidgetProvider(val context: Context, intent: Intent) : RemoteViewsFactory {
    private val eventItemList: ArrayList<Memo> = ArrayList()
    private val widgetPresenter: WidgetPresenter = WidgetData(RoomRepository(context))

    private fun populateList() {
        eventItemList.addAll(widgetPresenter.populateListItem())
    }

    override fun onCreate() {
        populateList()
    }

    override fun onDataSetChanged() {
        eventItemList.clear()
        populateList()
    }

    override fun onDestroy() {}

    override fun getCount(): Int {
        return eventItemList.size
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    override fun getViewAt(position: Int): RemoteViews {
        val remoteView = RemoteViews(
                context.packageName, R.layout.widget_list_row)
        val listItem = eventItemList[position]
        remoteView.setTextViewText(R.id.tvDate, listItem.getDateString())
        remoteView.setTextViewText(R.id.tvTime, listItem.getTimeString())
        remoteView.setTextViewText(R.id.tvAmount, "")
        remoteView.setTextViewText(R.id.tvContent, listItem.note)

        return remoteView
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1
}
