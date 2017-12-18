package com.example.user.eventest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.user.eventest.eventbus.events.DataUpdateEvent;

import de.greenrobot.event.EventBus;

class SaveStateAsyncTask extends AsyncTask<Boolean, Void, Void> {

    private Context context;

    SaveStateAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Boolean... params) {
        Boolean isChecked = params[0];
        saveState(isChecked);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        EventBus.getDefault().post(new DataUpdateEvent("onPostExecute"));
    }

    private void saveState(boolean isChecked) {
        try {
            Context mainAppContext =
                    context.createPackageContext("com.example.user.eventest", 0);

            Log.d("saveState", "StartDelay");
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("saveState", "EndDelay");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainAppContext);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(MainActivity.PREF_TEST_STATE, isChecked);
            editor.commit();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
