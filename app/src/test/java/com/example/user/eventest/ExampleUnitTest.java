package com.example.user.eventest;

import com.example.user.eventest.model.Memo;
import com.example.user.eventest.model.MemoRepository;
import com.example.user.eventest.model.Preferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
}