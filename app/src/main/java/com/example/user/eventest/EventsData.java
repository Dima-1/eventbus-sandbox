package com.example.user.eventest;

import android.content.Context;
import android.os.AsyncTask;

import com.example.user.eventest.eventbus.events.MemoAdapterRefreshEvent;
import com.example.user.eventest.model.Memo;
import com.example.user.eventest.model.MemoRepository;
import com.example.user.eventest.model.Preferences;
import com.example.user.eventest.room.AppDatabase;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DR
 * on 24.12.2017.
 */

public class EventsData implements MainPresenter {
    private MainView view;
    private MemoRepository memoRepository;
    private MemoRepository.Preferences preferences;
    private Context context;
    private AppDatabase db;
    private static final int NEW_MEMO_ID = -1;
    private Memo selectedMemo;

    EventsData(MainView view, MemoRepository repository, Preferences preferences, Context context) {
        this.view = view;
        this.context = context;
        this.memoRepository = repository;
        this.preferences = preferences;
        db = AppDatabase.getInstance(context);
        selectedMemo = new Memo();
        selectedMemo.setMemoID(NEW_MEMO_ID);

    }

    EventsData(Context context) {
        this(null, null, null, context);
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

    @Override
    public void showNewMemoOnStart() {
        if (preferences.isNewMemoOnStart()) {
            view.setEditViewsVisible();
        }
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

    Memo getConcreteMemo(final Memo memo) {
        return memoRepository.getConcreteMemo(memo);
    }

    void addMemo(Memo memo) {
        new AddMemoTask(memo, db).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    ArrayList<Memo> getAllData() {
        return memoRepository.getAllData();
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
