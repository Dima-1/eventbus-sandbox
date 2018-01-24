package com.example.user.eventest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by User on 11.01.2018.
 * Adapter for list of memo
 */

class MemoAdapter extends ArrayAdapter<Memo> {

    private EventsData eventsData;

    MemoAdapter(@NonNull Context context, EventsData eventsData) {
        super(context, R.layout.list_row, eventsData.getAllData());
        this.eventsData = eventsData;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        Memo memo = getItem(position);
        return memo != null ? memo.getMemoID() : 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvContent = convertView.findViewById(R.id.tvContent);
            viewHolder.tvDate = convertView.findViewById(R.id.tvDate);
            viewHolder.tvTime = convertView.findViewById(R.id.tvTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Memo memo = getItem(position);
        if (memo != null) {
            viewHolder.tvContent.setText(memo.getNote());
            viewHolder.tvDate.setText(memo.getDateString());
            viewHolder.tvTime.setText(memo.getTimeString());
        }
        return convertView;
    }

    void refreshEvents() {
        clear();
        addAll(eventsData.getAllData());
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvContent;
        TextView tvDate;
        TextView tvTime;
    }
}