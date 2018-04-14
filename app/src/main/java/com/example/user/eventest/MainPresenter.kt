package com.example.user.eventest

import com.example.user.eventest.model.Attachments
import com.example.user.eventest.model.Memo

/**
 * Created by DR
 * on 26.02.2018.
 */

interface MainPresenter {
    fun fabClick()
    fun setSelectedMemoToEdit(memo: Memo)
    fun menuEditClick()
    fun showNewMemoOnStart()
    fun getAttachments(memoID: Long): Attachments?
}
