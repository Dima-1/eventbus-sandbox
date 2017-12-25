package com.example.user.eventest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created on 24.12.2017.
 */

class EventsData {
    private Context context;
    private ArrayList<Memo> list = new ArrayList<>();

    EventsData(Context context) {
        this.context = context;
        populateListItem();
    }

    ArrayList<Memo> getAllData() {
        return list;
    }

    void addNewData(String data) {
        if (data.length() > 0) {
            Memo memo = new Memo(data);
            list.add(memo);
        }
    }

    SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void populateListItem() {
        for (int i = 0; i < 10; i++) {
            DateFormat sdf = DateFormat.getDateInstance();
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, i);
            date = c.getTime();
            Memo memo = new Memo(sdf.format(date),
                    i + "Content for example " + String.valueOf(i));
            list.add(memo);
        }
    }
}
