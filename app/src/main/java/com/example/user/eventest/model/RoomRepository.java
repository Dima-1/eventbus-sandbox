package com.example.user.eventest.model;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.user.eventest.eventbus.events.MemoAdapterRefreshEvent;
import com.example.user.eventest.room.AppDatabase;
import com.example.user.eventest.room.DateConverterDB;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by DR
 * on 28.02.2018.
 */

public class RoomRepository implements MemoRepository {
    private AppDatabase db;

    public RoomRepository(Context context) {
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

    @NonNull
    public ArrayList<Memo> getAllData() {
        try {
            return new GetAllMemos(db).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, db).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
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

    @Override
    public void addMemo(@NotNull Memo memo) {
        new AddMemoTask(memo, db).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    static class AddMemoTask extends AsyncTask<Void, Void, Void> {
        private Memo memo;
        private AppDatabase db;

        AddMemoTask(Memo memo, AppDatabase db) {
            this.memo = memo;
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... v) {
            db.getMemoDAO().insert(memo);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().post(new MemoAdapterRefreshEvent());
        }
    }

    @Override
    public void deleteMemo(@NotNull final Memo memo) {
        deleteByMemoID(memo.getMemoID());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.getMemoDAO().delete(memo);
            }
        });
    }

    @Override
    public void deleteByMemoID(long memoID) {
        new DeleteMemoByIDTask(memoID, db).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    static class DeleteMemoByIDTask extends AsyncTask<Void, Void, Void> {
        private long id;
        private AppDatabase db;

        DeleteMemoByIDTask(long id, AppDatabase db) {
            this.id = id;
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... v) {
            db.getMemoDAO().deleteByMemoId(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            EventBus.getDefault().post(new MemoAdapterRefreshEvent());
        }
    }

    @Override
    public void updateMemo(@NotNull Memo memo) {
        new UpdateMemoTask(memo, db).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    static class UpdateMemoTask extends AsyncTask<Void, Void, Void> {
        private Memo memo;
        private AppDatabase db;

        UpdateMemoTask(Memo memo, AppDatabase db) {
            this.memo = memo;
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... v) {
            db.getMemoDAO().update(memo);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().post(new MemoAdapterRefreshEvent());
        }
    }
}
