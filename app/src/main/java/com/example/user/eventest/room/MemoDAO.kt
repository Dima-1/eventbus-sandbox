package com.example.user.eventest.room

import android.arch.persistence.room.*
import com.example.user.eventest.model.Attachments
import com.example.user.eventest.model.Memo

/**
 * Created by DR
 * on 27.12.2017.
 */

@Dao
interface MemoDAO {
    @Query("SELECT * FROM memo order by date desc")
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

    @Query("DELETE FROM memo WHERE memo_id = :memoID")
    fun deleteByMemoId(memoID: Long)
}
