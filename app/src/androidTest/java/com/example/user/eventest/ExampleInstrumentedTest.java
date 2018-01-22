package com.example.user.eventest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;

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
        eventsData = new EventsData(InstrumentationRegistry.getTargetContext());
        testMemo = new Memo("10/10/10", "Memo " + Calendar.getInstance().getTimeInMillis());
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
        Thread.sleep(1000);

        Memo readMemo = eventsData.getConcreteMemo(testMemo);
        assertThat(testMemo.getDate(), equalTo(readMemo.getDate()));
        assertThat(testMemo.getNote(), equalTo(readMemo.getNote()));

        eventsData.deleteMemo(readMemo);
        Thread.sleep(1000);
        readMemo = eventsData.getConcreteMemo(testMemo);
        assertNull(readMemo);
    }
}
