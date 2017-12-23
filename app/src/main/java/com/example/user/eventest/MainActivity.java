package com.example.user.eventest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public final static String PREF_TEST_STATE = "test_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView lvEvents = findViewById(R.id.lvEvents);
        CheckBox checkBox = findViewById(R.id.checkBox);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        TextView textView = findViewById(R.id.tvDate);

        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            list.add(String.valueOf(i));
        }

        ArrayAdapter arrayAdapter =
                new ArrayAdapter(this, R.layout.list_row, R.id.content, list);
        lvEvents.setAdapter(arrayAdapter);

        checkBox.setChecked(prefs.getBoolean(PREF_TEST_STATE, false));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Context context = getApplicationContext();
                Toast.makeText(context,
                        String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                new SaveStateAsyncTask(context)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, isChecked);
                String TAG = "on click " + this.getClass().getName();
                Log.d(TAG, "AsyncTask.Status.RUNNING");
                new UpdateWidgetAsyncTask(context)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),
                        String.valueOf(new Date().getTime()), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
