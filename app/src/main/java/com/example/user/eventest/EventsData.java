package com.example.user.eventest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created on 24.12.2017.
 */

public class EventsData {
    private Context context;
    private AppDatabase db;

    public EventsData(Context context) {
        this.context = context;
        db = AppDatabase.getInstance(context);
//        populateListItem();
    }

    public ArrayList<Memo> getAllData() {
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

    Memo getConcreteMemo(final Memo memo) {
        try {
            return new AsyncTask<AppDatabase, Void, Memo>() {

                @Override
                protected Memo doInBackground(AppDatabase... v) {
                    return db.getMemoDAO().getConcreteMemo(memo.getDate(), memo.getNote());
                }

                @Override
                protected void onPostExecute(Memo memo) {
                    super.onPostExecute(memo);
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

    Date getDate() {
        Date date = new Date();
        return date;

    }
    void addMemo(final Memo memo) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.getMemoDAO().insert(memo);
            }
        });
    }

    void deleteMemo(final Memo memo) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.getMemoDAO().delete(memo);
            }
        });

    }
}
