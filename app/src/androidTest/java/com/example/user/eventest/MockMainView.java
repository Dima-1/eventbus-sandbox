package com.example.user.eventest;

import com.example.user.eventest.model.Memo;

import org.jetbrains.annotations.NotNull;
import org.mockito.Mock;

/**
 * Created by DR
 * on 24.03.2018.
 */

class MockMainView implements MainView {
    @Mock
    private Memo memo;

    @NotNull
    @Override
    public Memo getEditedMemo() {
        return memo;
    }

    @Override
    public boolean hasFocusNote() {
        return false;
    }

    @Override
    public void setEditViewsGone() {

    }

    @Override
    public void setEditViewsVisible() {

    }

    @Override
    public void setEditedMemo(@NotNull Memo memo) {

    }

    @Override
    public void getSelectedMemoToEdit(int position) {

    }

    @Override
    public void updateWidget() {

    }
}
