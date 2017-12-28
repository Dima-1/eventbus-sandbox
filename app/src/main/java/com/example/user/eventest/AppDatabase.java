package com.example.user.eventest;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

/*
 * Created by User on 27.12.2017.
 */
@Database(entities = {Memo.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    private static final Object sLock = new Object();

    public abstract MemoDAO getMemoDAO();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
        }
    };

    static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "database")
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
            return INSTANCE;
        }
    }

}
