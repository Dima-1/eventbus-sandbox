package com.example.user.eventest;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasType;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;


/**
 * Created by DR
 * on 16.03.2018.
 */
@RunWith(AndroidJUnit4.class)
public class IntentTest {
    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void getFileTest() {
        onView(withId(R.id.fabNewMemo)).perform(click());
        onView(withId(R.id.menuAddFile)).perform(click());
        intended(allOf(
                hasAction(Intent.ACTION_GET_CONTENT),
                hasType("*/*")));
    }

    @Test
    public void getPhotoTest() {
        onView(withId(R.id.fabNewMemo)).perform(click());
        onView(withId(R.id.menuAddPhoto)).perform(click());
        intended(
                hasAction(MediaStore.ACTION_IMAGE_CAPTURE));
    }


    @Test
    public void getLocationTest() {
        onView(withId(R.id.fabNewMemo)).perform(click());
        onView(withId(R.id.menuAddLocation)).perform(click());
//        intended(allOf(
//                hasAction(MediaStore.ACTION_IMAGE_CAPTURE),
//                hasExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
//        ));
    }

    @Test
    public void addTimeStamp() {
        onView(withId(R.id.fabNewMemo)).perform(click());
        onView(withId(R.id.menuAddTimeStamp)).perform(click());
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        String dateStamp = dateFormat.format(new Date().getTime()) + " ";
        onView(withId(R.id.emvMemo)).check(matches(withText(dateStamp)));
    }
}