package com.example.user.eventest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.example.user.eventest.eventbus.events.MemoAdapterRefreshEvent;
import com.example.user.eventest.room.AppDatabase;
import com.example.user.eventest.room.DateConverterDB;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created on 24.12.2017.
 */

public class EventsData implements IMainPresenter {
    private IMainView view;
    private Context context;
    private AppDatabase db;
    private static final int NEW_MEMO_ID = -1;
    private Memo selectedMemo;

    EventsData(IMainView view, Context context) {
        this.view = view;
        this.context = context;
        db = AppDatabase.getInstance(context);
        selectedMemo = new Memo();
        selectedMemo.setMemoID(NEW_MEMO_ID);
        if (isNewMemoOnStart()) {
            view.setEditViewsVisible();
        }
    }

    public EventsData(Context context) {
        this(null, context);
    }

    private SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private boolean isNewMemoOnStart() {
        return getPreferences()
                .getBoolean(context.getString(R.string.start_new_memo), false);
    }

    private void getNewMemoWithCurrentDate() {
        Date date = new Date();
        Memo memo = new Memo(
                DateFormat.getDateInstance(DateFormat.SHORT).format(date),
                DateFormat.getTimeInstance(DateFormat.SHORT).format(date),
                "");
        memo.setMemoID(NEW_MEMO_ID);
        setSelectedMemoToEdit(memo);
    }

    public ArrayList<Memo> getAllData() {
        try {
            return new GetAllMemos(db).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, db).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void fabClick() {
        if (view.hasFocusNote()) {
            saveMemoAfterEdit();
            view.setEditViewsGone();
        } else {
            getNewMemoWithCurrentDate();
            view.setEditViewsVisible();
        }
    }

    @Override
    public void setSelectedMemoToEdit(Memo memo) {
        selectedMemo = memo;
        view.setEditedMemo(selectedMemo);
    }

    @Override
    public void menuEditClick() {
        if (selectedMemo.getMemoID() == NEW_MEMO_ID)
            getNewMemoWithCurrentDate();
        view.setEditViewsVisible();
    }

    private void saveMemoAfterEdit() {
        Memo memo = view.getEditedMemo();
        if (selectedMemo.getMemoID() == NEW_MEMO_ID) {

            addMemo(memo);
        } else {
            memo.setMemoID(selectedMemo.getMemoID());
            updateMemo(memo);
            selectedMemo.setMemoID(NEW_MEMO_ID);
        }

        new UpdateWidgetAsyncTask(context)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    Memo getConcreteMemo(final Memo memo) {

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

    void addMemo(Memo memo) {
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

    void deleteMemo(final Memo memo) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.getMemoDAO().delete(memo);
            }
        });
    }

    private void updateMemo(final Memo memo) {
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

    void deleteByMemoID(final long memoID) {
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
}
