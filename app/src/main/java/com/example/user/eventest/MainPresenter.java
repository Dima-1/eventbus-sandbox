package com.example.user.eventest;

import com.example.user.eventest.model.Memo;

/**
 * Created by DR
 * on 26.02.2018.
 */

public interface MainPresenter {
    void fabClick();

    void setSelectedMemoToEdit(Memo item);

    void menuEditClick();

    void showNewMemoOnStart();
}
