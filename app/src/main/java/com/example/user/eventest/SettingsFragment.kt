package com.example.user.eventest

import android.os.Bundle
import android.preference.PreferenceFragment


/**
 * Created by DR
 * on 12.02.2018.
 */

class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}
