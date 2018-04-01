package com.example.user.eventest

import android.os.Bundle
import android.preference.PreferenceFragment
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions


/**
 * Created by DR
 * on 12.02.2018.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}
