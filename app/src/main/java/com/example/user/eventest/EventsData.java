package com.example.user.eventest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created on 24.12.2017.
 */

class EventsData {
    private Context context;
    private ArrayList<Memo> list = new ArrayList<>();

    EventsData(Context context) {
        this.context = context;
    }

    ArrayList<Memo> getAllData() {
        for (int i = 0; i < 10; ++i) {
            Memo memo = new Memo("Date =" + String.valueOf(i), String.valueOf(i));
            list.add(memo);
        }
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
}
