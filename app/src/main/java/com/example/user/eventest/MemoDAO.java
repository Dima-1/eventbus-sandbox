package com.example.user.eventest;

import android.arch.persistence.room.Dao;
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

    @Insert
    void insert(Memo memo);
}
