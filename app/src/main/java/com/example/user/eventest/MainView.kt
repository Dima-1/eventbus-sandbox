package com.example.user.eventest

import com.example.user.eventest.model.Memo

/**
 * Created by DR
 * on 26.02.2018.
 */

interface MainView {
    fun hasFocusNote(): Boolean
    fun setEditViewsGone()
    fun setEditViewsVisible()
    fun getEditedMemo(): Memo
    fun setEditedMemo(memo: Memo)
    fun getSelectedMemoToEdit(position: Int)
    fun updateWidget()
}
