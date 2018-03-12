package com.example.user.eventest.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.user.eventest.model.Attachments;
import com.example.user.eventest.model.Memo;

/*
 * Created by User on 27.12.2017.
 */
@Database(entities = {Memo.class, Attachments.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "database.db";
    private static AppDatabase INSTANCE;
    private static final Object sLock = new Object();

    public abstract MemoDAO getMemoDAO();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };

    /**
     * Rename field note to date
     */
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Memo RENAME TO orig_Memo;");
            database.execSQL("CREATE TABLE Memo (" +
                    "memoID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "date TEXT," +
                    "note TEXT" +
                    ");");
            database.execSQL("INSERT INTO Memo(memoID, date, note) " +
                    "SELECT memoID, name, note " +
                    "FROM orig_Memo; ");
            database.execSQL("DROP TABLE orig_Memo;");

        }
    };
    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE Attachments (" +
                    "attachID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "memoID INTEGER NOT NULL," +
                    "pathToAttach TEXT" +
                    ");");
        }
    };
    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .addMigrations(MIGRATION_2_3)
                        .addMigrations(MIGRATION_3_4)
                        .build();
            }
            return INSTANCE;
        }
    }

}
