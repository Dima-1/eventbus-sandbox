package com.example.user.eventest;

/**
 * Created by DR on 26.02.2018.
 */

public interface IMainView {
    boolean hasFocusNote();

    void setEditViewsGone();

    void setEditViewsVisible();

    Memo getEditedMemo();

    void setEditedMemo(Memo memo);

    void getSelectedMemoToEdit(int position);
}
