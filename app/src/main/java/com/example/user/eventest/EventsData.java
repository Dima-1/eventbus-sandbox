package com.example.user.eventest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import com.example.user.eventest.eventbus.events.MemoAdapterRefreshEvent;
import org.greenrobot.eventbus.EventBus;
/**
 * Created on 24.12.2017.
 */

public class EventsData {
    private Context context;
    private AppDatabase db;

    public EventsData(Context context) {
        this.context = context;
        db = AppDatabase.getInstance(context);
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
            return new getConcreteMemoTask(memo,db)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, db).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    static class getConcreteMemoTask extends AsyncTask<AppDatabase, Void, Memo> {
        private Memo memo;
        private AppDatabase db;

        getConcreteMemoTask(Memo memo, AppDatabase db) {
            this.memo = memo;
            this.db = db;
        }

        @Override
        protected Memo doInBackground(AppDatabase... v) {
            return db.getMemoDAO().getConcreteMemo(memo.getDate(), memo.getNote());
        }

        @Override
        protected void onPostExecute(Memo memo) {
            super.onPostExecute(memo);
        }
    }

    SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    Date getDate() {
        return new Date();
    }

    void addMemo(Memo memo) {
        new addMemoTask(memo, db).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    static class addMemoTask extends AsyncTask<Void, Void, Void> {
        private Memo memo;
        private AppDatabase db;

        addMemoTask(Memo memo, AppDatabase db) {
            this.memo = memo;
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.getMemoDAO().insert(memo);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().post(new MemoAdapterRefreshEvent());
        }
    }

    void deleteMemo(final Memo memo) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.getMemoDAO().delete(memo);
            }
        });
    }

    void deleteByMemoID(final long memoID) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.getMemoDAO().deleteByMemoId(memoID);
                EventBus.getDefault().post(new MemoAdapterRefreshEvent());
            }
        });

    }
}
