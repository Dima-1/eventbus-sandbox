package com.example.user.eventest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.eventest.eventbus.events.DatePickerUpdateEvent;
import com.example.user.eventest.eventbus.events.MemoAdapterRefreshEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {
    public final static String PREF_TEST_STATE = "test_state";
    private EventsData eventsData;
    private MemoAdapter memoAdapter;
    @BindView(R.id.tvDate)
    TextView date;
    @BindView(R.id.vDateTimeBackground)
    View vDateTimeBackground;
    @BindView(R.id.tvTime)
    TextView time;
    @BindView(R.id.lvEvents)
    ListView lvEvents;
    @BindView(R.id.etNote)
    EditText note;
    @BindView(R.id.fabNewMemo)
    FloatingActionButton fabNewMemo;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    private boolean visible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        eventsData = new EventsData(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        memoAdapter = new MemoAdapter(this, eventsData);
        lvEvents.setAdapter(memoAdapter);
        lvEvents.setSelector(R.color.colorMemoSelect);
        lvEvents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvEvents.setMultiChoiceModeListener(getMemoListMultiChoiceListener());

        date.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(eventsData.getDate()));
        time.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(eventsData.getDate()));
    }

    @NonNull
    private MultiChoiceModeListener getMemoListMultiChoiceListener() {
        return new MultiChoiceModeListener() {

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
                return true;
            }

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                int amountDeleted = lvEvents.getCheckedItemCount();
                if (item.getItemId() == R.id.menuAmDelete) {
                    for (long id : lvEvents.getCheckedItemIds()) {
                        eventsData.deleteByMemoID(id);
                    }
                }
                mode.finish();
                String plural = amountDeleted > 1 ? "s" : "";
                Snackbar.make(coordinatorLayout, "Delete "
                                + amountDeleted + " memo" + plural,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction("UNDO", snackBarOnClickListener).show();
                return false;
            }

            View.OnClickListener snackBarOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "OK!", Toast.LENGTH_LONG).show();
                }
            };

            public void onDestroyActionMode(ActionMode mode) {
            }

            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                mode.setTitle(lvEvents.getCheckedItemCount() + "/" + lvEvents.getCount());
                String TAG = "ActionMode";
                Log.d(TAG, "position = " + position + ", checked = " + checked);
            }
        };
    }

    @OnItemClick(R.id.lvEvents)
    void getSelectedMemoToEdit(AdapterView<?> adapter, View v, int position, long id) {
        Memo memo = memoAdapter.getItem(position);
        String selectedMemoNote = memo != null ? memo.getNote() : null;
        String selectedMemoDate = memo != null ? memo.getDateString() : null;
        String selectedMemoTime = memo != null ? memo.getTimeString() : null;
        note.setText(selectedMemoNote);
        date.setText(selectedMemoDate);
        time.setText(selectedMemoTime);
    }


    @OnEditorAction(R.id.etNote)
    boolean onEditorAction(TextView note, int actionId, KeyEvent event) {
        saveMemoAfterEdit(note);
        return false;
    }

    private void saveMemoAfterEdit(TextView note) {
        Memo memo = new Memo(
                date.getText().toString(), time.getText().toString(), note.getText().toString());
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(note.getApplicationWindowToken(), 0);
        }
        eventsData.addMemo(memo);

        new UpdateWidgetAsyncTask(this)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @OnClick(R.id.vDateTimeBackground)
    void showDateTimeDialog() {
        Bundle bundle = new Bundle();
        bundle.putString("date", date.getText().toString());
        bundle.putString("time", time.getText().toString());

        DialogFragment dateTimeDialog = new DateTimeDialog();
        dateTimeDialog.setArguments(bundle);
        dateTimeDialog.show(getSupportFragmentManager(), "datePicker");
    }

    @OnClick(R.id.fabNewMemo)
    void newMemo(View view) {
        if (note.hasFocus()) {
            saveMemoAfterEdit(note);
            note.clearFocus();
            note.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
            vDateTimeBackground.setVisibility(View.GONE);
        } else {
            visible = !visible;
            note.setVisibility(View.VISIBLE);
            note.requestFocus();
            time.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
            vDateTimeBackground.setVisibility(View.VISIBLE);
            Snackbar.make(view, "New memo created", Snackbar.LENGTH_LONG).show();
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(note, InputMethodManager.SHOW_IMPLICIT);
            }
        }
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

    @Subscribe
    public void onDatePickerUpdateEvent(DatePickerUpdateEvent event) {
        String TAG = "event receiver " + this.getClass().getName();
        Log.d(TAG, event.getMessage().getTime().toString());
        Date tmpDate = event.getMessage().getTime();
        date.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(tmpDate));
        time.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(tmpDate));
    }

    @Subscribe
    public void onMemoAdapterRefreshEvent(MemoAdapterRefreshEvent event) {
        memoAdapter.refreshEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuAbout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.about).setMessage(R.string.about)
                        .setMessage(getString(R.string.version)
                                + BuildConfig.VERSION_NAME + " "
                                + BuildConfig.VERSION_CODE);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.menuSettings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
