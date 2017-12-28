package com.example.user.eventest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created on 24.12.2017.
 */

class EventsData {
    private Context context;
    private ArrayList<Memo> list = new ArrayList<>();
    private AppDatabase db;

    EventsData(Context context) {
        this.context = context;
        populateListItem();
        db = AppDatabase.getInstance(context);
    }

    ArrayList<Memo> getAllData() {
        try {
            return new AsyncTask<AppDatabase, Void, ArrayList<Memo>>() {

                @Override
                protected ArrayList<Memo> doInBackground(AppDatabase... v) {
                    return new ArrayList<>(db.getMemoDAO().getAllMemo());
                }

                @Override
                protected void onPostExecute(ArrayList<Memo> memos) {
                    super.onPostExecute(memos);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, db).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

//    ArrayList<Memo> getAllData() {
//        return list;
//    }

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

    void addMemo(final Memo memo) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.getMemoDAO().insert(memo);
            }
        });

    }
}
