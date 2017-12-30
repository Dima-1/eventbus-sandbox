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
    private AppDatabase db;

    EventsData(Context context) {
        this.context = context;
        db = AppDatabase.getInstance(context);
//        populateListItem();
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
        }
        return null;
    }

    SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

   /* private void populateListItem() {
        for (int i = 0; i < 10; i++) {

            Memo memo = new Memo(getDate(),
                    i + "Content for example " + String.valueOf(i));
            addMemo(memo);
        }
    }*/

    String getDate() {
        DateFormat sdf = DateFormat.getDateInstance();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        date = calendar.getTime();
        return sdf.format(date);

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