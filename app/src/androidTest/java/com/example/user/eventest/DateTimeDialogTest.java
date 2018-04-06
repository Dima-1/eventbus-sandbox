package com.example.user.eventest;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by DR
 * on 05.04.2018.
 */
@RunWith(AndroidJUnit4.class)
public class DateTimeDialogTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkDateTimeDialog() {
        String date = mActivityTestRule.getActivity().getEditedMemo().getDateString();
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
        onView(withId(R.id.btnCancel)).perform(click());
        onView(allOf(withId(R.id.vDateTimeBackground),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 1),
                isDisplayed()))
                .perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .check(matches(isDisplayed()));
        onView(withId(R.id.tvNow)).check(matches(withText(date)));
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
        onView(withId(R.id.btnOK)).perform(click());
        DateFormat sdf = DateFormat.getDateInstance(DateFormat.SHORT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        onView(allOf(withId(R.id.tvDate), childAtPosition(
                childAtPosition(
                        withId(android.R.id.content),
                        0),
                2), isDisplayed())).check(matches(withText(sdf.format(calendar.getTime()))));
        pressBack();
        TestHelper.rotateScreen(mActivityTestRule);
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
        onView(withId(R.id.btnCancel)).perform(click());
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

}
