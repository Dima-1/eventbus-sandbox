package com.example.user.eventest;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;


/**
 * Created by DR 12.02.2018.
 */

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
