package com.example.user.eventest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.eventest.eventbus.events.DatePickerUpdateEvent;

import java.text.DateFormat;
import java.util.Date;

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

        memoAdapter = new MemoAdapter(this, eventsData);
        lvEvents.setAdapter(memoAdapter);
        lvEvents.setSelector(R.color.colorMemoSelect);
        lvEvents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        lvEvents.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
                return true;
            }

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                mode.finish();
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                mode.setTitle(lvEvents.getCheckedItemCount() + "/" + lvEvents.getCount());
                String TAG = "ActionMode";
                Log.d(TAG, "position = " + position + ", checked = " + checked);
            }
        });



       /* lvEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> adapter, View v, int position, long id) {

                Memo selItem = memoAdapter.getItem(position); //
                String value = selItem != null ? selItem.getNote() : null; //getter method
                Toast.makeText(getApplicationContext(),
                        String.valueOf(position) + ":" + value, Toast.LENGTH_SHORT).show();
                Memo memo = memoAdapter.getItem(position);
                eventsData.deleteMemo(memo);
                memoAdapter.refreshEvents();
                return false;
            }
        });*/

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
        Date tmpDate = event.getMessage().getTime();
        date.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(tmpDate));
        time.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(tmpDate));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.abmenu, menu);
        return true;
    }
}
