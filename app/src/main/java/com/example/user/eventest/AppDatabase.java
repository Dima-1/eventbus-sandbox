package com.example.user.eventest;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/*
 * Created by User on 27.12.2017.
 */
@Database(entities = {Memo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MemoDAO getMemoDAO();

}
