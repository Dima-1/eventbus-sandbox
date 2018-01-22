package com.example.user.eventest;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.uiautomator.Until.hasObject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)

//testing a Widget with UI Automator testing framework

public class WidgetTest {

    private static final String TEST_PACKAGE = BuildConfig.APPLICATION_ID;
    private String TEST_LAUNCHER_PACKAGE;
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;
    private boolean isWidgetShown;

    @Before
    public void startMainActivityFromHomeScreen() {

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            TEST_LAUNCHER_PACKAGE = "com.android.launcher";
        } else {
            TEST_LAUNCHER_PACKAGE = TEST_PACKAGE;
        }
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();
        isWidgetShown = mDevice.hasObject(By.pkg(TEST_LAUNCHER_PACKAGE)
                .clazz(".RelativeLayout"));
        boolean isTestPackageWidget = mDevice.hasObject(By.pkg(TEST_LAUNCHER_PACKAGE)
                .clazz(".TextView").res(TEST_PACKAGE, "tvAllBackground"));
        isWidgetShown &= isTestPackageWidget;


        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(hasObject(By.pkg(launcherPackage).depth(0)),
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
        mDevice.wait(hasObject(By.pkg(TEST_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);
    }

    @Test
    public void ChangingWidgetWithDelay() throws InterruptedException, UiObjectNotFoundException {

        Assume.assumeTrue("Widget not on the home screen", isWidgetShown);

        UiObject checkBox = mDevice.findObject(new UiSelector()
                .text("CheckBox")
                .className("android.widget.CheckBox"));
        String newWidgetValue = ((Boolean) (!checkBox.isChecked())).toString();

        checkBox.click();
        mDevice.pressHome();

        UiObject alarmAlert = mDevice.findObject(new UiSelector().packageName(TEST_LAUNCHER_PACKAGE)
                .className("android.widget.TextView")
                .text(newWidgetValue));
        assertTrue("Timeout while waiting for widget string changed",
                alarmAlert.waitForExists(15 * 1000));
    }
}


