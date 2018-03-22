package com.example.user.eventest.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.migration.Migration
import android.content.Context

import com.example.user.eventest.model.Attachments
import com.example.user.eventest.model.Memo

/**
 * Created by DR
 * on 27.12.2017.
 */
@TypeConverters(DateConverterDB::class)
@Database(entities = [(Memo::class), (Attachments::class)], version = 4)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMemoDAO(): MemoDAO

    companion object {
        const val DATABASE_NAME = "database.db"
        private var INSTANCE: AppDatabase? = null
        private val sLock = Object()
        @JvmField
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

        /**
         * Rename field note to date
         */
        @JvmField
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE memo RENAME TO origin_memo;")
                database.execSQL("CREATE TABLE memo (" +
                        "memo_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "date   TEXT NOT NULL," +
                        "note   TEXT" +
                        ");")
                database.execSQL("INSERT INTO Memo(memo_id, date, note) " +
                        "SELECT memo_id, name, note " +
                        "FROM origin_memo; ")
                database.execSQL("DROP TABLE origin_memo;")
            }
        }
        /**
         * Create attachments table
         */
        @JvmField
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS attachments (" +
                        "attach_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "memo_id INTEGER NOT NULL," +
                        "path_to_attach TEXT" +
                        ");")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .build()
                }
                return INSTANCE as AppDatabase
            }
        }
    }
}
