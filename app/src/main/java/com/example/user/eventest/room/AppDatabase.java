package com.example.user.eventest.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.user.eventest.model.Attachments;
import com.example.user.eventest.model.Memo;

/**
 * Created by DR
 * on 27.12.2017.
 */
@TypeConverters({DateConverterDB.class})
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
            database.execSQL("ALTER TABLE memo RENAME TO origin_memo;");
            database.execSQL("CREATE TABLE memo (" +
                    "memo_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "date   TEXT NOT NULL," +
                    "note   TEXT" +
                    ");");
            database.execSQL("INSERT INTO Memo(memo_id, date, note) " +
                    "SELECT memo_id, name, note " +
                    "FROM origin_memo; ");
            database.execSQL("DROP TABLE origin_memo;");

        }
    };
    /**
     * Create attachments table
     */
    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS attachments (" +
                    "attach_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "memo_id INTEGER NOT NULL," +
                    "path_to_attach TEXT" +
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
