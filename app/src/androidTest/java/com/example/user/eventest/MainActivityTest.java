package com.example.user.eventest;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CheckBox;

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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkBoxToggle() {
        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.checkBox), withText("CheckBox"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        CheckBox checkBox = mActivityTestRule.getActivity().findViewById(R.id.checkBox);
        Boolean checkClick = checkBox.isChecked();
        appCompatCheckBox.perform(click());
        Matcher<View> matcher = checkClick ? not(isChecked()) : isChecked();
        onView(withId(R.id.checkBox)).check(matches(matcher));
    }

    @Test
    public void checkDateDialog() {
        EventsData eventsData =
                new EventsData(mActivityTestRule.getActivity().getApplicationContext());
        onView(allOf(withId(R.id.tvDate),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 2),
                withText(eventsData.getDate()),
                isDisplayed()))
                .perform(click());
        onView(withClassName(Matchers.equalTo(DateTimeDialog.class.getName())))
                .check(matches(isDisplayed()));

        int year = 2011;
        int month = 11;
        int day = 11;
        // TODO: 06.01.2018 No views in hierarchy found matching:
        // with class name:"com.example.user.eventest.DateTimeDialog"
        onView(withClassName(Matchers.equalTo(DateTimeDialog.class.getName())))
                .perform(PickerActions.setDate(year, month + 1, day));
        onView(withId(android.R.id.button1)).perform(click());
        DateFormat sdf = DateFormat.getDateInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        onView(allOf(withId(R.id.tvDate), childAtPosition(
                childAtPosition(
                        withId(android.R.id.content),
                        0),
                2), isDisplayed()));//.check(matches(withText(sdf.format(calendar.getTime()))));


    }

    @Test
    public void checkAddMemo() {
        String testString = "Test memo";
        onView(allOf(withId(R.id.etNote), isDisplayed()))
                .perform(typeText(testString), pressImeActionButton());

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
