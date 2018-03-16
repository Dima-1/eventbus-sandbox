package com.example.user.eventest.model

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.example.user.eventest.R

/**
 * Created by DR
 * on 28.02.2018.
 */

class Preferences(val context: Context) : MemoRepository.Preferences {

    override fun getPreferences(): SharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(context)

    override fun isNewMemoOnStart(): Boolean = getPreferences()
            .getBoolean(context.getString(R.string.start_new_memo), false)
}
