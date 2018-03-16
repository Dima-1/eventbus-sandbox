package com.example.user.eventest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.eventest.eventbus.events.DatePickerUpdateEvent;
import com.example.user.eventest.eventbus.events.MemoAdapterRefreshEvent;
import com.example.user.eventest.model.Memo;
import com.example.user.eventest.model.Preferences;
import com.example.user.eventest.model.RoomRepository;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity implements MainView {
    public final static String PREF_TEST_STATE = "test_state";
    private static final int GET_PHOTO_ACTIVITY_REQUEST_CODE = 1;
    private static final int GET_FILE_ACTIVITY_REQUEST_CODE = 2;
    public static final String TV_DATE_KEY = "tvDate";
    public static final String TV_TIME_KEY = "tvTime";
    public static final String EDIT_MEMO_KEY = "editMemo";
    private EventsData eventsData;
    private MemoAdapter memoAdapter;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.etMemo)
    EditMemoView emvMemo;
    @BindView(R.id.vDateTimeBackground)
    View vDateTimeBackground;
    @BindView(R.id.lvEvents)
    ListView lvEvents;
    @BindView(R.id.fabNewMemo)
    FloatingActionButton fabNewMemo;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.my_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.bottomToolbar)
    Toolbar bottomToolbar;
    @BindView(R.id.amvMenu)
    ActionMenuView amvMenu;
    private ConstraintLayout myLayout;
    private ConstraintSet constraintSet1;
    private ConstraintSet constraintSet2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        eventsData = new EventsData(this,
                new RoomRepository(this),
                new Preferences(getApplicationContext()),
                getApplicationContext());

        setSupportActionBar(mainToolbar);
        myLayout = findViewById(R.id.main_layout);
        constraintSet1 = new ConstraintSet();
        constraintSet1.clone(myLayout);
        constraintSet2 = new ConstraintSet();
        constraintSet2.clone(this, R.layout.main_activity_edit);
        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuAddPhoto:
                        Snackbar.make(coordinatorLayout, "Add photo",
                                Snackbar.LENGTH_LONG).show();
                        addPhoto();
                        return true;
                    case R.id.menuAddLocation:
                        Snackbar.make(coordinatorLayout, "Add location",
                                Snackbar.LENGTH_LONG).show();
                        addLocation();
                        return true;
                    case R.id.menuAddFile:
                        Snackbar.make(coordinatorLayout, "Add file",
                                Snackbar.LENGTH_LONG).show();
                        addFile();
                        return true;
                }
                return false;
            }
        });
        getMenuInflater().inflate(R.menu.bottom_menu, amvMenu.getMenu());
        memoAdapter = new MemoAdapter(this, eventsData);
        lvEvents.setAdapter(memoAdapter);
        lvEvents.setSelector(R.color.colorMemoSelect);
        lvEvents.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvEvents.setMultiChoiceModeListener(getMemoListMultiChoiceListener());
        eventsData.showNewMemoOnStart();
    }

    private void addLocation() {
    }

    private void addFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, GET_FILE_ACTIVITY_REQUEST_CODE);
    }

    private void addPhoto() {
        String nameFromDate = new SimpleDateFormat(
                "yyyyMMdd_HHmmss_SSS", Locale.getDefault()).format(new Date());
        String fileName = "IMG_" + nameFromDate + ".jpg";
        String filePath = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), fileName).getAbsolutePath();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        long dateSeconds = new Date().getTime() / 1000;
        values.put(MediaStore.Images.Media.DATA, filePath);
        values.put(MediaStore.Images.Media.DATE_ADDED, dateSeconds);
        values.put(MediaStore.Images.Media.DATE_MODIFIED, dateSeconds);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
        Uri imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, GET_PHOTO_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GET_PHOTO_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this,
                            "!!! Picture was taken !!!", Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this,
                            "Picture was not taken", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,
                            "Picture was not taken", Toast.LENGTH_SHORT).show();
                }
                break;
            case GET_FILE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this,
                            "!!! File was chosen !!!", Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this,
                            "File was not chosen", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,
                            "File was not chosen", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EDIT_MEMO_KEY, hasFocusNote());
        outState.putString(TV_DATE_KEY, tvDate.getText().toString());
        outState.putString(TV_TIME_KEY, tvTime.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getBoolean(EDIT_MEMO_KEY)) setEditViewsVisible();
        if (savedInstanceState.getString(TV_DATE_KEY) != null)
            tvDate.setText(savedInstanceState.getString(TV_DATE_KEY));
        if (savedInstanceState.getString(TV_TIME_KEY) != null)
            tvTime.setText(savedInstanceState.getString(TV_TIME_KEY));
    }

    @NonNull
    private MultiChoiceModeListener getMemoListMultiChoiceListener() {

        return new MultiChoiceModeListener() {
            boolean checked = false;

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
                return true;
            }

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                int amountDeleted = lvEvents.getCheckedItemCount();
                switch (item.getItemId()) {
                    case R.id.menuAmDelete:
                        for (long id : lvEvents.getCheckedItemIds()) {
                            eventsData.deleteByMemoID(id);
                        }
                        mode.finish();
                        String plural = amountDeleted > 1 ? "s" : "";
                        Snackbar.make(coordinatorLayout, "Delete "
                                        + amountDeleted + " memo" + plural,
                                Snackbar.LENGTH_LONG)
                                .setAction("UNDO", snackBarUndoOnClickListener).show();
                        setEditViewsGone();
                        return false;

                    case R.id.menuAmSelectAll:
                        checked = !checked;
                        for (int i = 0; i < lvEvents.getCount(); i++)
                            lvEvents.setItemChecked(i, checked);
                        return false;
                }
                return false;
            }

            View.OnClickListener snackBarUndoOnClickListener = new View.OnClickListener() {
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
    public void getSelectedMemoToEdit(int position) {
        eventsData.setSelectedMemoToEdit(memoAdapter.getItem(position));
    }

    @OnClick(R.id.vDateTimeBackground)
    void showDateTimeDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(TV_DATE_KEY, tvDate.getText().toString());
        bundle.putString(TV_TIME_KEY, tvTime.getText().toString());

        DialogFragment dateTimeDialog = new DateTimeDialog();
        dateTimeDialog.setArguments(bundle);
        dateTimeDialog.show(getSupportFragmentManager(), "datePicker");
    }

    @OnClick(R.id.fabNewMemo)
    void fabClick() {
        eventsData.fabClick();
    }

    public void setEditViewsGone() {
        emvMemo.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(emvMemo.getApplicationWindowToken(), 0);
        }
        fabNewMemo.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_add_white_24px));
        TransitionManager.beginDelayedTransition(myLayout);
        constraintSet1.applyTo(myLayout);
        emvMemo.setEditState(false);
        invalidateOptionsMenu();
    }

    public void setEditViewsVisible() {
        TransitionManager.beginDelayedTransition(myLayout);
        constraintSet2.applyTo(myLayout);
        emvMemo.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(emvMemo, InputMethodManager.SHOW_IMPLICIT);
        }
        fabNewMemo.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_done_white_24px));
        emvMemo.setEditState(true);
        invalidateOptionsMenu();
    }

    @NonNull
    @Override
    public Memo getEditedMemo() {
        return new Memo(
                tvDate.getText().toString(), tvTime.getText().toString(), emvMemo.getText().toString());
    }

    @Override
    public void setEditedMemo(@NonNull Memo memo) {
        emvMemo.setText(memo.getNote());
        tvDate.setText(memo.getDateString());
        tvTime.setText(memo.getTimeString());
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
        tvDate.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(tmpDate));
        tvTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(tmpDate));
    }

    @Subscribe
    public void onMemoAdapterRefreshEvent(MemoAdapterRefreshEvent event) {
        memoAdapter.refreshEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menuEdit).setVisible(!hasFocusNote());
        return super.onPrepareOptionsMenu(menu);
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
            case R.id.menuEdit:
                eventsData.menuEditClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean hasFocusNote() {
        String TAG = "MainActivity";
        Log.d(TAG, "hasFocusNote --- return : " + emvMemo.hasFocus());
        return emvMemo.isEditState();
    }
}
