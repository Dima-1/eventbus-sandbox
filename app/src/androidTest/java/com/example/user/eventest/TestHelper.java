package com.example.user.eventest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.rule.ActivityTestRule;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by DR
 * on 05.04.2018.
 */
public class TestHelper {
    static void rotateScreen(ActivityTestRule<MainActivity> mActivityTestRule) {
        Context context = getInstrumentation().getTargetContext();
        int orientation = context.getResources().getConfiguration().orientation;
        Activity activity = mActivityTestRule.getActivity();
        activity.setRequestedOrientation(
                (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
