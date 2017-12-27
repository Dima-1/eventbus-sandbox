package com.example.user.eventest;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public final static String PREF_TEST_STATE = "test_state";
    EventsData eventsData = new EventsData(this);
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").build();

        Memo memo = new Memo("1", "2");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView lvEvents = findViewById(R.id.lvEvents);
        CheckBox checkBox = findViewById(R.id.checkBox);
        TextView textView = findViewById(R.id.tvDate);
        EditText note = findViewById(R.id.etNote);

        ArrayAdapter<Memo> arrayAdapter = new NoteAdapter(this);
        lvEvents.setAdapter(arrayAdapter);

        checkBox.setChecked(eventsData.getPreferences().getBoolean(PREF_TEST_STATE, false));
        note.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                eventsData.addNewData(String.valueOf(v.getText()));
                Memo memo = new Memo("1", String.valueOf(v.getText()));
                addMemo(db, memo);
                return false;
            }
        });

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
                DialogFragment dateTimeDialog = new DateTimeDialog();
                dateTimeDialog.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    private static Memo addMemo(final AppDatabase db, Memo memo) {
        db.getMemoDAO().insert(memo);
        return memo;
    }
    class NoteAdapter extends ArrayAdapter<Memo> {

        NoteAdapter(@NonNull Context context) {
            super(context, R.layout.list_row, eventsData.getAllData());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_row, parent, false);
            } else {
                Memo memo = getItem(position);
                if (memo != null) {
                    ((TextView) convertView.findViewById(R.id.tvContent)).setText(memo.getNote());
                    ((TextView) convertView.findViewById(R.id.tvDate)).setText(memo.getDate());
                }
            }
            return convertView;
        }

    }

}
