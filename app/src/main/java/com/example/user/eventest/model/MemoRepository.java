package com.example.user.eventest.model;

import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by DR
 * on 28.02.2018.
 */

public interface MemoRepository {
    Memo getConcreteMemo(Memo memo);

    ArrayList<Memo> getAllData();

    interface Preferences {
        SharedPreferences getPreferences();

        boolean isNewMemoOnStart();
    }
}
