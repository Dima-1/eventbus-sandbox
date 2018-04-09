package com.example.user.eventest;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasType;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
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
    public void activityResult_FileChosen() {
        Intent resultData = new Intent();
        ActivityResult resultOk =
                new ActivityResult(Activity.RESULT_OK, resultData);
        intending(IntentMatchers.hasAction(Intent.ACTION_GET_CONTENT)).respondWith(resultOk);
        onView(withId(R.id.fabNewMemo)).perform(click());
        onView(withId(R.id.menuAddFile)).perform(click());
        onView(withText(R.string.file_chosen)).inRoot(withDecorView(
                not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        toastDelay();
        ActivityResult resultCancel =
                new ActivityResult(Activity.RESULT_CANCELED, resultData);
        intending(IntentMatchers.hasAction(Intent.ACTION_GET_CONTENT)).respondWith(resultCancel);
        onView(withId(R.id.menuAddFile)).perform(click());
        onView(withText(R.string.file_not_chosen)).inRoot(withDecorView(
                not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        toastDelay();
        int wrongResult = 1111;
        ActivityResult resultWrong =
                new ActivityResult(wrongResult, resultData);
        intending(IntentMatchers.hasAction(Intent.ACTION_GET_CONTENT)).respondWith(resultWrong);
        onView(withId(R.id.menuAddFile)).perform(click());
        onView(withText(R.string.file_not_chosen)).inRoot(withDecorView(
                not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void activityResult_PhotoTaken() {
        Intent resultData = new Intent();
        ActivityResult resultOk =
                new ActivityResult(Activity.RESULT_OK, resultData);
        intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(resultOk);
        onView(withId(R.id.fabNewMemo)).perform(click());
        onView(withId(R.id.menuAddPhoto)).perform(click());
        onView(withText(R.string.photo_taken)).inRoot(withDecorView(
                not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        toastDelay();
        ActivityResult resultCancel =
                new ActivityResult(Activity.RESULT_CANCELED, resultData);
        intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(resultCancel);
        onView(withId(R.id.menuAddPhoto)).perform(click());
        onView(withText(R.string.photo_not_taken)).inRoot(withDecorView(
                not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
        toastDelay();
        int wrongResult = 1111;
        ActivityResult resultWrong =
                new ActivityResult(wrongResult, resultData);
        intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(resultWrong);
        onView(withId(R.id.menuAddPhoto)).perform(click());
        onView(withText(R.string.photo_not_taken)).inRoot(withDecorView(
                not(is(mActivityTestRule.getActivity().getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    private void toastDelay() {
        try {
            final int TOAST_DELAY = 3000;
            Thread.sleep(TOAST_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPhotoTest() {
        onView(withId(R.id.fabNewMemo)).perform(click());
        onView(withId(R.id.menuAddPhoto)).perform(click());
        intended(hasAction(MediaStore.ACTION_IMAGE_CAPTURE));
    }

    @Test
    public void getLocationTest() {
        onView(withId(R.id.fabNewMemo)).perform(click());
        onView(withId(R.id.menuAddLocation)).perform(click());
    }

    @Test
    public void addTimeStamp() {
        onView(withId(R.id.fabNewMemo)).perform(click());
        onView(withId(R.id.menuAddTimeStamp)).perform(click());
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        String dateStamp = dateFormat.format(new Date().getTime()) + " ";
        onView(withId(R.id.emvMemo)).check(matches(withText(dateStamp)));
        pressBack();
        toastDelay();
        onView(withId(R.id.fabNewMemo)).perform(click());
        String testString = "Test lead space";
        onView(withId(R.id.emvMemo)).perform(setEditMemoViewText(testString));
        onView(withId(R.id.menuAddTimeStamp)).perform(click());
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        dateStamp = testString + " " + dateFormat.format(new Date().getTime()) + " ";
        onView(withId(R.id.emvMemo)).check(matches(withText(dateStamp)));
    }

    /*Custom ViewAction for move cursor to the end*/
    public static ViewAction setEditMemoViewText(final String newText) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(EditMemoView.class));
            }

            @Override
            public String getDescription() {
                return "Update the text from the custom EditText";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                ((EditMemoView) view).setText(newText);
                ((EditMemoView) view).setSelection(newText.length());
            }
        };
    }
}