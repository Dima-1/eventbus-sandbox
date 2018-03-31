package com.example.user.eventest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.user.eventest.model.Memo

/**
 * Created by User on 11.01.2018.
 * Adapter for list of memo
 */

class MemoAdapter(context: Context, private val eventsData: EventsData) :
        ArrayAdapter<Memo>(context, R.layout.list_row, eventsData.getAllData()) {

    override fun hasStableIds(): Boolean = true

    override fun getItemId(position: Int): Long {
        val memo: Memo = getItem(position)
        return memo.memoID
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val tmpView: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            tmpView = LayoutInflater.from(context)
                    .inflate(R.layout.list_row, parent, false)
            viewHolder = ViewHolder(tmpView.findViewById(R.id.tvContent),
                    tmpView.findViewById(R.id.tvDate),
                    tmpView.findViewById(R.id.tvTime))
            tmpView.tag = viewHolder
        } else {
            tmpView = convertView
            viewHolder = tmpView.tag as ViewHolder
        }
        val memo: Memo = getItem(position)
        viewHolder.tvContent.text = memo.note
        viewHolder.tvDate.text = memo.getDateString()
        viewHolder.tvTime.text = memo.getTimeString()
        return tmpView
    }

    fun refreshEvents() {
        clear()
        addAll(eventsData.getAllData())
        notifyDataSetChanged()
    }

    class ViewHolder(val tvContent: TextView, val tvDate: TextView, val tvTime: TextView)
}