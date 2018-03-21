package com.example.user.eventest;

import android.support.annotation.Nullable;

import com.example.user.eventest.model.Memo;
import com.example.user.eventest.model.MemoRepository;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by DR
 * on 24.12.2017.
 */

class EventsData implements MainPresenter {
    private MainView view;
    private MemoRepository memoRepository;
    private MemoRepository.Preferences preferences;
    private static final int NEW_MEMO_ID = -1;
    private Memo selectedMemo;

    EventsData(MainView view, MemoRepository repository, MemoRepository.Preferences preferences) {
        this.view = view;
        this.memoRepository = repository;
        this.preferences = preferences;
        selectedMemo = new Memo(NEW_MEMO_ID, new Date(), "");
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
    public void setSelectedMemoToEdit(@Nullable Memo memo) {
        selectedMemo = (memo != null) ? memo : new Memo(new Date(), "");
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
    }

    Memo getConcreteMemo(Memo memo) {
        return memoRepository.getConcreteMemo(memo);
    }

    void addMemo(Memo memo) {
        memoRepository.addMemo(memo);
    }

    ArrayList<Memo> getAllData() {
        return memoRepository.getAllData();
    }

    void deleteMemo(Memo memo) {
        memoRepository.deleteMemo(memo);
    }

    private void updateMemo(final Memo memo) {
        memoRepository.updateMemo(memo);
    }

    void deleteByMemoID(final long memoID) {
        memoRepository.deleteByMemoID(memoID);
    }
}
