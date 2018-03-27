package com.example.user.eventest;

import android.content.Context;

import com.example.user.eventest.model.Memo;
import com.example.user.eventest.model.MemoRepository;
import com.example.user.eventest.model.Preferences;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Mock
    private MainView view;
    @Mock
    private MemoRepository movieRepo;
    @Mock
    private Preferences preferences;
    @Mock
    private Memo memo;
    @Mock
    private Context context;

    private EventsData eventsData;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        eventsData = new EventsData(view, movieRepo, preferences);
    }

    @Test
    public void showNewMemoOnStart_test() {
        when(preferences.isNewMemoOnStart()).thenReturn(true);
        eventsData.showNewMemoOnStart();
        verify(view).setEditViewsVisible();
    }

    @Test
    public void notShowNewMemoOnStart_test() {
        when(preferences.isNewMemoOnStart()).thenReturn(false);
        eventsData.showNewMemoOnStart();
        verify(view, never()).setEditViewsVisible();
    }

    @Test
    public void setSelectedMemoToEdit_test() {
        eventsData.setSelectedMemoToEdit(memo);
        verify(view).setEditedMemo(any(Memo.class));
    }

    @Test
    public void testDifferentMemoConstructor() {
        String testString = "Test";
        Calendar calendar = Calendar.getInstance();
        calendar.set(12, 12, 12);
        Date testDate = calendar.getTime();
        Memo memo = new Memo(1, new Date(), null);
        assertNull(memo.getNote());
        memo.setNote(testString);
        assertSame(memo.getNote(), testString);
        memo.setDate(testDate);
        assertSame(memo.getDate(), testDate);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testMemoDateTimeParseException() {
        DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
        Memo memo = new Memo(" ", " ", " ");
        assertThat(sdf.format(memo.getDate()), equalTo(sdf.format(new Date())));
    }

    @Test
    public void testPreferenceConstructor() {
        Preferences preferences = new Preferences(context);
        assertEquals(preferences.getContext(), context);
    }
}