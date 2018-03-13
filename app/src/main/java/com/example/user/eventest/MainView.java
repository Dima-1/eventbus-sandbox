package com.example.user.eventest;

import com.example.user.eventest.model.Memo;

/**
 * Created by DR
 * on 26.02.2018.
 */

public interface MainView {
    boolean hasFocusNote();

    void setEditViewsGone();

    void setEditViewsVisible();

    Memo getEditedMemo();

    void setEditedMemo(Memo memo);

    void getSelectedMemoToEdit(int position);
}
