package com.example.user.eventest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.eventest.eventbus.events.DatePickerUpdateEvent;

import java.text.DateFormat;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    public final static String PREF_TEST_STATE = "test_state";
    private EventsData eventsData;
    private MemoAdapter memoAdapter;
    private TextView date;
    private TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eventsData = new EventsData(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        final ListView lvEvents = findViewById(R.id.lvEvents);
        CheckBox checkBox = findViewById(R.id.checkBox);
        date = findViewById(R.id.tvDate);
        time = findViewById(R.id.tvTime);
        final EditText note = findViewById(R.id.etNote);

        memoAdapter = new MemoAdapter(this);
        lvEvents.setAdapter(memoAdapter);
        lvEvents.setSelector(R.color.colorMemoSelect);

        lvEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> adapter, View v, int position, long id) {

                Memo selItem = memoAdapter.getItem(position); //
                String value = selItem != null ? selItem.getNote() : null; //getter method
                Toast.makeText(getApplicationContext(),
                        String.valueOf(position) + ":" + value, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

                Memo memo = memoAdapter.getItem(position);
                String selectedMemoNote = memo != null ? memo.getNote() : null;
                String selectedMemoDate = memo != null ? memo.getDate() : null;
                note.setText(selectedMemoNote);
                date.setText(selectedMemoDate);
            }
        });

        note.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView note, int actionId, KeyEvent event) {
                Memo memo = new Memo(
                        String.valueOf(date.getText()), String.valueOf(note.getText()));
                eventsData.addMemo(memo);
                memoAdapter.refreshEvents();
                return false;
            }
        });

        checkBox.setChecked(eventsData.getPreferences().getBoolean(PREF_TEST_STATE, false));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Context context = getApplicationContext();
                Toast.makeText(context,
                        String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                new SaveStateAsyncTask(context)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, isChecked);
                String TAG = "on click " + this.getClass().getName();
                Log.d(TAG, "UpdateWidgetAsyncTask");
                new UpdateWidgetAsyncTask(context)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        date.setText(eventsData.getDate());
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateTimeDialog = new DateTimeDialog();
                dateTimeDialog.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(DatePickerUpdateEvent event) {
        String TAG = "event receiver " + this.getClass().getName();
        Log.d(TAG, event.getMessage().getTime().toString());
        date.setText(DateFormat.getDateInstance().format(event.getMessage().getTime()));
        time.setText(DateFormat.getTimeInstance().format(event.getMessage().getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.abmenu, menu);
        return true;
    }

    class MemoAdapter extends ArrayAdapter<Memo> {

        MemoAdapter(@NonNull Context context) {
            super(context, R.layout.list_row, eventsData.getAllData());
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.list_row, parent, false);
            }
            Memo memo = getItem(position);
            if (memo != null) {
                ((TextView) convertView.findViewById(R.id.tvContent)).setText(memo.getNote());
                ((TextView) convertView.findViewById(R.id.tvDate)).setText(memo.getDate());
            }
            return convertView;
        }

        void refreshEvents() {
            clear();
            addAll(eventsData.getAllData());
            notifyDataSetChanged();
        }
    }
}
