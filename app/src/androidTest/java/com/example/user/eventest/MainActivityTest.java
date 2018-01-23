package com.example.user.eventest;


import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;

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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
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
                                6),
                        isDisplayed()));
        CheckBox checkBox = mActivityTestRule.getActivity().findViewById(R.id.checkBox);
        Boolean checkClick = checkBox.isChecked();
        appCompatCheckBox.perform(click());
        Matcher<View> matcher = checkClick ? not(isChecked()) : isChecked();
        onView(withId(R.id.checkBox)).check(matches(matcher));
    }

    @Test
    public void checkListViewAndToolbar() {
        Context context = getInstrumentation().getTargetContext();
        int actionBarId = context.getResources().
                getIdentifier("action_bar_title", "id", context.getPackageName());
        EventsData eventsData = new EventsData(context);
        int totalRecords = eventsData.getAllData().size();

        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(0).perform(longClick());
        onView(withId(actionBarId)).check(matches(withText("1/" + String.valueOf(totalRecords))));
        onData(anything()).inAdapterView(withId(R.id.lvEvents)).atPosition(0).perform(click());
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
        EventsData eventsData =
                new EventsData(mActivityTestRule.getActivity().getApplicationContext());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        String testDate = dateFormat.format(eventsData.getDate());
        onView(allOf(withId(R.id.tvDate),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 2),
                withText(testDate),
                isDisplayed()))
                .perform(click());
        onView(allOf(withText(R.string.cancel), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.tvDate),
                childAtPosition(childAtPosition(withId(android.R.id.content), 0), 2),
                withText(testDate),
                isDisplayed()))
                .perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.btnDateTime),
                withText(R.string.date),
                isDisplayed()))
                .perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .check(matches(isDisplayed()));
        onView(allOf(withId(R.id.btnDateTime),
                withText(R.string.time),
                isDisplayed()))
                .perform(click());
        onView(allOf(withId(R.id.btnDateTime),
                withText(R.string.date),
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
