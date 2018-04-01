package com.example.user.eventest;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.user.eventest.model.Preferences;
import com.example.user.eventest.model.RoomRepository;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.text.DateFormat;
import java.util.Calendar;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private MockMainView mainView = new MockMainView();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    private static final String TEST_STRING = "Test memo " + Calendar.getInstance().getTimeInMillis();


    @Test
    public void checkListViewAndToolbar() {
        Context context = getInstrumentation().getTargetContext();
        int totalRecords;
        int actionBarId = context.getResources().
                getIdentifier("action_bar_title", "id", context.getPackageName());
        EventsData eventsData = new EventsData(mainView,
                new RoomRepository(context), new Preferences(context));
        checkAddMemo();
        totalRecords = eventsData.getAllData().size();
        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(0).perform(longClick());
        onView(withId(R.id.menuAmDelete)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int remainingRecords = eventsData.getAllData().size();
        assertEquals(remainingRecords, totalRecords - 1);
        checkAddMemo();
        checkAddMemo();
        checkAddMemo();

        totalRecords = eventsData.getAllData().size();

        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(0).perform(longClick());
        onView(withId(actionBarId)).check(matches(withText("1/" + String.valueOf(totalRecords))));
        onView(withId(R.id.menuAmSelectAll)).perform(click());
        onView(withId(R.id.menuAmSelectAll)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(0).perform(longClick());
        onView(withId(R.id.menuAmSelectAll)).perform(click());
        onView(withId(actionBarId)).check(matches(withText(String.valueOf(totalRecords)
                + "/" + String.valueOf(totalRecords))));
        onView(withId(R.id.menuAmDelete)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        totalRecords = eventsData.getAllData().size();
        assertEquals(totalRecords, 0);
    }

    @Test
    public void checkEditSelectedMemo() {
        checkAddMemo();
        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(0).perform(click());
        onView(withId(R.id.menuEdit)).perform(click());
        pressBack();
        onView(withId(R.id.menuEdit)).perform(click());
        onView(allOf(withId(R.id.emvMemo), isDisplayed()))
                .perform(clearText(), replaceText(TEST_STRING));
        onView(withId(R.id.fabNewMemo)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(0)
                .check(matches(hasDescendant(withText(TEST_STRING))));
    }

    @Test
    public void checkAboutDialog() {
        Context context = getInstrumentation().getTargetContext();
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(R.string.about)).perform(click());
        onView(withText(context.getString(R.string.version)
                + BuildConfig.VERSION_NAME + " "
                + BuildConfig.VERSION_CODE)).check(matches(isDisplayed()));

    }

    @Test
    public void checkSettingsDialog() {
        Context context = getInstrumentation().getTargetContext();
        openActionBarOverflowOrOptionsMenu(context);
        onView(withText(R.string.action_settings)).perform(click());
        onView(withText(context.getString(R.string.pref_start_with_new_memo)
        )).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(withId(R.id.menuEdit)).check(matches(isDisplayed()));

    }

    public static Matcher<View> withResourceName(final Matcher<String> resourceNameMatcher) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with resource name: ");
                resourceNameMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                int id = view.getId();
                return id != View.NO_ID && id != 0 && view.getResources() != null
                        && resourceNameMatcher.matches(view.getResources().getResourceName(id));
            }
        };
    }

    @Test
    public void checkDateTimeDialog() throws ReflectiveOperationException {
        onView(withId(R.id.menuEdit)).perform(click());
        onView(allOf(withId(R.id.vDateTimeBackground),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 1),
                isDisplayed()))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(android.R.id.button2)).perform(click());
        onView(allOf(withId(R.id.vDateTimeBackground),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 1),
                isDisplayed()))
                .perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.fabDateTime),
                isDisplayed()))
                .perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.fabDateTime),
                isDisplayed()))
                .perform(click());
        onView(allOf(withId(R.id.fabDateTime),
                isDisplayed()))
                .perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .check(matches(isDisplayed()));
        int year = 2015;
        int month = 11;
        int day = 22;
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month + 1, day));
        onView(withId(android.R.id.button1)).perform(click());
        DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        onView(allOf(withId(R.id.tvDate), childAtPosition(
                childAtPosition(
                        withId(android.R.id.content),
                        0),
                2), isDisplayed())).check(matches(withText(sdf.format(calendar.getTime()))));
        pressBack();
        rotateScreen();
        onView(withId(R.id.menuEdit)).perform(click());
        onView(allOf(withId(R.id.vDateTimeBackground),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 1),
                isDisplayed()))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(android.R.id.button2)).perform(click());
    }

    @Test
    public void checkAddMemo() {
        onView(withId(R.id.fabNewMemo)).perform(click());
        onView(allOf(withId(R.id.emvMemo), isDisplayed()))
                .perform(clearText(), replaceText(TEST_STRING));
        onView(withId(R.id.fabNewMemo)).perform(click());
    }

    @Test
    public void makeRotateScreen() {
        rotateScreen();
        onView(withId(R.id.fabNewMemo)).perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private void rotateScreen() {
        Context context = getInstrumentation().getTargetContext();
        int orientation = context.getResources().getConfiguration().orientation;
        Activity activity = mActivityTestRule.getActivity();
        activity.setRequestedOrientation(
                (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
