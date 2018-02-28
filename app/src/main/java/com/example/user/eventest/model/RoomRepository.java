package com.example.user.eventest.model;

import android.content.Context;
import android.os.AsyncTask;

import com.example.user.eventest.room.AppDatabase;
import com.example.user.eventest.room.DateConverterDB;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by DR
 * on 28.02.2018.
 */

public class RoomRepository implements MemoRepository {
    private Context context;
    private AppDatabase db;


    public RoomRepository(Context context) {
        this.context = context;
        db = AppDatabase.getInstance(context);
    }

    @Override
    public Memo getConcreteMemo(Memo memo) {
        try {
            return new GetConcreteMemoTask(memo, db)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, db).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    static class GetConcreteMemoTask extends AsyncTask<AppDatabase, Void, Memo> {
        private Memo memo;
        private AppDatabase db;
        DateConverterDB dateConverter = new DateConverterDB();

        GetConcreteMemoTask(Memo memo, AppDatabase db) {
            this.memo = memo;
            this.db = db;
        }

        @Override
        protected Memo doInBackground(AppDatabase... v) {
            return db.getMemoDAO().getConcreteMemo(
                    dateConverter.stringFromDate(memo.getDate()), memo.getNote());
        }

        @Override
        protected void onPostExecute(Memo memo) {
            super.onPostExecute(memo);
        }
    }

    public ArrayList<Memo> getAllData() {
        try {
            return new GetAllMemos(db).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, db).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    static class GetAllMemos extends AsyncTask<AppDatabase, Void, ArrayList<Memo>> {

        private AppDatabase db;

        GetAllMemos(AppDatabase db) {
            this.db = db;
        }

        @Override
        protected ArrayList<Memo> doInBackground(AppDatabase... appDatabases) {
            return new ArrayList<>(db.getMemoDAO().getAllMemo());
        }

        @Override
        protected void onPostExecute(ArrayList<Memo> memos) {
            super.onPostExecute(memos);
        }
    }

}
