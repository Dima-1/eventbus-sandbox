package com.example.user.eventest.model

import android.content.SharedPreferences
import java.util.*

/**
 * Created by DR
 * on 28.02.2018.
 */

interface MemoRepository {
    fun getConcreteMemo(memo: Memo): Memo
    fun getAllData(): ArrayList<Memo>

    interface Preferences {
        fun getPreferences(): SharedPreferences
        fun isNewMemoOnStart(): Boolean
    }
}
