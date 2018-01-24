package com.example.user.eventest;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.testing.MigrationTestHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.user.eventest.room.AppDatabase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static com.example.user.eventest.room.AppDatabase.MIGRATION_1_2;
import static com.example.user.eventest.room.AppDatabase.MIGRATION_2_3;
import static org.junit.Assert.assertEquals;

/**
 * Created by User on 14.01.2018.
 * testing the migration from version 2 to version 3
 */
@RunWith(AndroidJUnit4.class)
public class MigrationTest {
    @Rule
    public MigrationTestHelper testHelper =
            new MigrationTestHelper(
                    InstrumentationRegistry.getInstrumentation(),
                    AppDatabase.class.getCanonicalName(),
                    new FrameworkSQLiteOpenHelperFactory());


    @Test
    public void testMigration() throws IOException {
        SupportSQLiteDatabase db =
                testHelper.createDatabase(AppDatabase.DATABASE_NAME, 2);
        Memo testMemo = new Memo("21/12/32", "test");
        String testDate = "2032-12-21 23:45:30";
        String testNote = "test";
        db.execSQL("INSERT INTO Memo(name, note) " +
                "VALUES ('" + testDate + "','" + testNote + "')");
        db.close();

        testHelper.runMigrationsAndValidate(
                AppDatabase.DATABASE_NAME, 3, true, MIGRATION_2_3);
        Memo dbMemo = getMigratedRoomDatabase().getMemoDAO()
                .getConcreteMemo(testMemo.getDateString(), testMemo.getNote());
        assertEquals(dbMemo.getNote(), testMemo.getNote());
        assertEquals(dbMemo.getDateString(), testMemo.getDateString());
    }

    private AppDatabase getMigratedRoomDatabase() {
        AppDatabase database = Room.databaseBuilder(InstrumentationRegistry.getTargetContext(),
                AppDatabase.class, AppDatabase.DATABASE_NAME)
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build();
        testHelper.closeWhenFinished(database);
        return database;
    }
}
