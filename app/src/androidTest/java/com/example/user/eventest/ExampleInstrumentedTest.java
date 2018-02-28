package com.example.user.eventest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.user.eventest.model.Memo;
import com.example.user.eventest.model.Preferences;
import com.example.user.eventest.model.RoomRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Instrumented test, which will execute on an Android device.
 * <p>
 * database testing
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private EventsData eventsData;
    private Memo testMemo;

    @Before
    public void init() {
        Context context = InstrumentationRegistry.getTargetContext();
        eventsData = new EventsData(null,
                new RoomRepository(context), new Preferences(context), context);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 10, 10, 11, 12);
        Date testDate = calendar.getTime();
        testMemo = new Memo(testDate, "Memo " + Calendar.getInstance().getTimeInMillis());
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(BuildConfig.APPLICATION_ID, appContext.getPackageName());
    }

    @Test
    public void getAllData_isCorrect() throws Exception {
        eventsData.addMemo(testMemo);
        Thread.sleep(1000);
        ArrayList<Memo> arrayList = eventsData.getAllData();
        assertFalse("getAllData not return list of memo", arrayList.isEmpty());
    }

    @Test
    public void addition_isCorrect() throws Exception {
        eventsData.addMemo(testMemo);
        Thread.sleep(500);

        Memo readMemo = eventsData.getConcreteMemo(testMemo);
        assertThat(testMemo.getDateString(), equalTo(readMemo.getDateString()));
        assertThat(testMemo.getTimeString(), equalTo(readMemo.getTimeString()));
        assertThat(testMemo.getNote(), equalTo(readMemo.getNote()));

        eventsData.deleteMemo(readMemo);
        Thread.sleep(500);
        readMemo = eventsData.getConcreteMemo(testMemo);
        assertNull(readMemo);
    }
}
