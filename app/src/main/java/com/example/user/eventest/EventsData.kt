package com.example.user.eventest

import com.example.user.eventest.model.Attachments
import com.example.user.eventest.model.Memo
import com.example.user.eventest.model.MemoRepository
import java.text.DateFormat
import java.util.*

/**
 * Created by DR
 * on 24.12.2017.
 */
private const val NEW_MEMO_ID = -1L

class EventsData(private val view: MainView,
                 private val memoRepository: MemoRepository,
                 private val preferences: MemoRepository.Preferences) : MainPresenter {

    private var selectedMemo = Memo(NEW_MEMO_ID, Date(), "")
    private var attachments = Attachments(memoID = NEW_MEMO_ID, pathToAttach = null)


    private fun getNewMemoWithCurrentDate() {
        val date = Date()
        val memo = Memo(
                DateFormat.getDateInstance(DateFormat.SHORT).format(date),
                DateFormat.getTimeInstance(DateFormat.SHORT).format(date),
                "")
        memo.memoID = NEW_MEMO_ID
        setSelectedMemoToEdit(memo)
    }

    override fun fabClick() {
        if (view.hasFocusNote()) {
            saveMemoAfterEdit()
            view.setEditViewsGone()
        } else {
            getNewMemoWithCurrentDate()
            view.setEditViewsVisible()
        }
    }

    override fun setSelectedMemoToEdit(memo: Memo) {
        selectedMemo = memo
        view.setEditedMemo(selectedMemo)
    }

    override fun menuEditClick() {
        if (selectedMemo.memoID == NEW_MEMO_ID)
            getNewMemoWithCurrentDate()
        view.setEditViewsVisible()
    }

    override fun showNewMemoOnStart() {
        if (preferences.isNewMemoOnStart()) {
            view.setEditViewsVisible()
        }
    }

    private fun saveMemoAfterEdit() {
        val memo = view.getEditedMemo()
        if (selectedMemo.memoID == NEW_MEMO_ID) {
            addMemo(memo)
        } else {
            memo.memoID = selectedMemo.memoID
            updateMemo(memo)
            selectedMemo.memoID = NEW_MEMO_ID
        }
    }

    fun getConcreteMemo(memo: Memo): Memo? {
        return memoRepository.getConcreteMemo(memo)
    }

    fun addMemo(memo: Memo): Long {
        val memoID = memoRepository.addMemo(memo)
        attachments.memoID = memoID
        memoRepository.addAttachment(attachments)
        return memoID
    }

    fun getAllData(): ArrayList<Memo> {
        return memoRepository.getAllData()
    }

    fun deleteMemo(memo: Memo) {
        memoRepository.deleteMemo(memo)
    }

    private fun updateMemo(memo: Memo) {
        memoRepository.updateMemo(memo)
    }

    fun deleteByMemoID(memoID: Long) {
        memoRepository.deleteByMemoID(memoID)
    }

    fun addAttachment(path: String) {
        attachments.pathToAttach = path
    }
}
