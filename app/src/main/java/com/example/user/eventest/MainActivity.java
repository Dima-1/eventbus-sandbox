package com.example.user.eventest;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.user.eventest.eventbus.events.DataUpdateEvent;
import com.example.user.eventest.widget.WidgetProvider;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    public final static String PREF_TEST_STATE = "test_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckBox checkBox = findViewById(R.id.checkBox);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        checkBox.setChecked(prefs.getBoolean(PREF_TEST_STATE, false));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        String.valueOf(isChecked), Toast.LENGTH_SHORT);
                toast.show();
                saveState(isChecked);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEvent(DataUpdateEvent event) {
        String TAG = "event receiver " + this.getClass().getName();
        Log.d(TAG, event.getMessage());
        updateWidget();
    }

    private void saveState(boolean isChecked) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_TEST_STATE, isChecked);
        editor.commit();
        EventBus.getDefault().post(new DataUpdateEvent("saveState"));
    }

    private void updateWidget() {
        Intent intent = new Intent(this, WidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), WidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }
}
