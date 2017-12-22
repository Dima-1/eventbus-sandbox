package com.example.user.eventest;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)

//testing a Widget with UI Automator testing framework

public class WidgetTest {

    private static final String TEST_PACKAGE = "com.example.user.eventest";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(TEST_PACKAGE);
        // Clear out any previous instances
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(TEST_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);
    }

    @Test
    public void ChangingWidgetWithDelay() throws InterruptedException, UiObjectNotFoundException {

        UiObject checkBox = mDevice.findObject(new UiSelector()
                .text("CheckBox")
                .className("android.widget.CheckBox"));
        String newWidgetValue = ((Boolean) (!checkBox.isChecked())).toString();

        checkBox.click();
        mDevice.pressHome();

        UiObject alarmAlert = mDevice.findObject(new UiSelector().packageName(TEST_PACKAGE)
                .className("android.widget.TextView")
                .text(newWidgetValue));
        assertTrue("Timeout while waiting for widget string changed",
                alarmAlert.waitForExists(15 * 1000));
    }
}

