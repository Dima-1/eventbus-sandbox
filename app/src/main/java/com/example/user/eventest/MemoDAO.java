package com.example.user.eventest;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/*
 * Created by User on 27.12.2017.
 */

@Dao
public interface MemoDAO {
    @Query("SELECT * FROM memo")
    List<Memo> getAllMemo();

    @Query("SELECT * FROM memo where date = :date and note = :note")
    Memo getConcreteMemo(String date, String note);

    @Insert
    void insert(Memo memo);

    @Delete
    void delete(Memo memo);
}
