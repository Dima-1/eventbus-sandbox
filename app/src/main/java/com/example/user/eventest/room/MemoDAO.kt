package com.example.user.eventest.room

import android.arch.persistence.room.*
import com.example.user.eventest.model.Attachments
import com.example.user.eventest.model.Memo
import java.util.List

/**
 * Created by DR
 * on 27.12.2017.
 */

@Dao
interface MemoDAO {
    @Query("SELECT * FROM memo")
    fun getAllMemo(): List<Memo>

    @Query("SELECT * FROM memo where date = :date and note = :note")
    fun getConcreteMemo(date: String, note: String?): Memo

    @Insert
    fun insert(memo: Memo)

    @Insert
    fun insert(attachments: Attachments)

    @Delete
    fun delete(memo: Memo)

    @Update
    fun update(memo: Memo)

    @Query("DELETE FROM memo WHERE memoID = :memoID")
    fun deleteByMemoId(memoID: Long)
}
