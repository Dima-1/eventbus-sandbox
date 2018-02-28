package com.example.user.eventest.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.user.eventest.R;

/**
 * Created by DR
 * on 28.02.2018.
 */

public class Preferences implements MemoRepository.Preferences {
    private Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    @Override
    public SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public boolean isNewMemoOnStart() {
        return getPreferences()
                .getBoolean(context.getString(R.string.start_new_memo), false);
    }
}
