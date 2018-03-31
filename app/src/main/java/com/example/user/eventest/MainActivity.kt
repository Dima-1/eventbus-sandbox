package com.example.user.eventest

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.transition.TransitionManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ActionMenuView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView.MultiChoiceModeListener
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnItemClick
import com.example.user.eventest.eventbus.events.DatePickerUpdateEvent
import com.example.user.eventest.eventbus.events.MemoAdapterRefreshEvent
import com.example.user.eventest.model.Memo
import com.example.user.eventest.model.Preferences
import com.example.user.eventest.model.RoomRepository
import com.example.user.eventest.widget.WidgetProvider
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

const val PREF_TEST_STATE = "test_state"
const val TV_DATE_KEY = "tvDate"
const val TV_TIME_KEY = "tvTime"
private const val GET_PHOTO_ACTIVITY_REQUEST_CODE = 1
private const val GET_FILE_ACTIVITY_REQUEST_CODE = 2
private const val EDIT_MEMO_KEY = "editMemo"

class MainActivity : AppCompatActivity(), MainView {
    lateinit var eventsData: EventsData
    private lateinit var memoAdapter: MemoAdapter
    @BindView(R.id.tvDate)
    lateinit var tvDate: TextView
    @BindView(R.id.tvTime)
    lateinit var tvTime: TextView
    @BindView(R.id.etMemo)
    lateinit var emvMemo: EditMemoView
    @BindView(R.id.vDateTimeBackground)
    lateinit var vDateTimeBackground: View
    @BindView(R.id.lvEvents)
    lateinit var lvEvents: ListView
    @BindView(R.id.fabNewMemo)
    lateinit var fabNewMemo: FloatingActionButton
    @BindView(R.id.coordinatorLayout)
    lateinit var coordinatorLayout: CoordinatorLayout
    @BindView(R.id.my_toolbar)
    lateinit var mainToolbar: Toolbar
    @BindView(R.id.bottomToolbar)
    lateinit var bottomToolbar: Toolbar
    @BindView(R.id.amvMenu)
    lateinit var amvMenu: ActionMenuView
    private lateinit var myLayout: ConstraintLayout
    private val constraintSet1 = ConstraintSet()
    private val constraintSet2 = ConstraintSet()
    private val TAG: String = this.javaClass.simpleName

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        ButterKnife.bind(this)
        eventsData = EventsData(this,
                RoomRepository(this), Preferences(applicationContext))
        setSupportActionBar(mainToolbar)
        myLayout = findViewById(R.id.main_layout)
        constraintSet1.clone(myLayout)
        constraintSet2.clone(this, R.layout.main_activity_edit)
        amvMenu.setOnMenuItemClickListener(ActionMenuView.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuAddPhoto -> {
                    Snackbar.make(coordinatorLayout, "Add photo",
                            Snackbar.LENGTH_LONG).show()
                    addPhoto()
                    return@OnMenuItemClickListener true
                }
                R.id.menuAddLocation -> {
                    Snackbar.make(coordinatorLayout, "Add location",
                            Snackbar.LENGTH_LONG)
                            .show()
                    addLocation()
                    return@OnMenuItemClickListener true
                }
                R.id.menuAddFile -> {
                    Snackbar.make(coordinatorLayout, "Add file",
                            Snackbar.LENGTH_LONG)
                            .show()
                    addFile()
                    return@OnMenuItemClickListener true
                }
            }
            false
        })
        menuInflater.inflate(R.menu.bottom_menu, amvMenu.menu)
        memoAdapter = MemoAdapter(this, eventsData)
        lvEvents.adapter = memoAdapter
        lvEvents.setSelector(R.color.colorMemoSelect)
        lvEvents.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        lvEvents.setMultiChoiceModeListener(getMemoListMultiChoiceListener())
        eventsData.showNewMemoOnStart()
    }

    private fun addLocation() {
    }

    private fun addFile() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "*/*"
        startActivityForResult(intent, GET_FILE_ACTIVITY_REQUEST_CODE)
    }

    private fun addPhoto() {
        val nameFromDate = SimpleDateFormat(
                "yyyyMMdd_HHmmss_SSS", Locale.getDefault()).format(Date())
        val fileName = "IMG_$nameFromDate.jpg"
        val filePath = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), fileName).absolutePath
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, fileName)
        val dateSeconds = Date().time / 1000
        values.put(MediaStore.Images.Media.DATA, filePath)
        values.put(MediaStore.Images.Media.DATE_ADDED, dateSeconds)
        values.put(MediaStore.Images.Media.DATE_MODIFIED, dateSeconds)
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera")
        val imageUri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
        startActivityForResult(intent, GET_PHOTO_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GET_PHOTO_ACTIVITY_REQUEST_CODE ->
                when (resultCode) {
                    RESULT_OK -> Toast.makeText(this,
                            "!!! Picture was taken !!!", Toast.LENGTH_SHORT).show()
                    RESULT_CANCELED -> Toast.makeText(this,
                            "Picture was not taken", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this,
                            "Picture was not taken", Toast.LENGTH_SHORT).show()
                }
            GET_FILE_ACTIVITY_REQUEST_CODE ->
                when (resultCode) {
                    RESULT_OK -> Toast.makeText(this,
                            "!!! File was chosen !!!", Toast.LENGTH_SHORT).show()
                    RESULT_CANCELED -> Toast.makeText(this,
                            "File was not chosen", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this,
                            "File was not chosen", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(EDIT_MEMO_KEY, hasFocusNote())
        outState.putString(TV_DATE_KEY, tvDate.text.toString())
        outState.putString(TV_TIME_KEY, tvTime.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getBoolean(EDIT_MEMO_KEY)) setEditViewsVisible()
        if (savedInstanceState.getString(TV_DATE_KEY) != null)
            tvDate.text = savedInstanceState.getString(TV_DATE_KEY)
        if (savedInstanceState.getString(TV_TIME_KEY) != null)
            tvTime.text = savedInstanceState.getString(TV_TIME_KEY)
    }

    private fun getMemoListMultiChoiceListener(): MultiChoiceModeListener {

        return object : MultiChoiceModeListener {
            var checked = false

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                mode.menuInflater.inflate(R.menu.action_mode_menu, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                val amountDeleted = lvEvents.checkedItemCount
                when (item.itemId) {
                    R.id.menuAmDelete -> {
                        for (id in lvEvents.checkedItemIds) {
                            eventsData.deleteByMemoID(id)
                        }
                        mode.finish()
                        val plural = if (amountDeleted > 1) "s" else ""
                        Snackbar.make(coordinatorLayout, "Delete "
                                + amountDeleted + " memo" + plural,
                                Snackbar.LENGTH_LONG)
                                .setAction("UNDO", snackBarUndoOnClickListener).show()
                        setEditViewsGone()
                        return false
                    }
                    R.id.menuAmSelectAll -> {
                        checked = !checked
                        for (i in 0 until lvEvents.count)
                            lvEvents.setItemChecked(i, checked)
                        return false
                    }
                }
                return false
            }

            val snackBarUndoOnClickListener = View.OnClickListener {
                Toast.makeText(applicationContext, "OK!", Toast.LENGTH_LONG).show()
            }

            override fun onDestroyActionMode(mode: ActionMode) {
            }

            override fun onItemCheckedStateChanged(mode: ActionMode,
                                                   position: Int, id: Long, checked: Boolean) {
                mode.title = "${lvEvents.checkedItemCount}/${lvEvents.count}"
                Log.d(TAG, "ActionMode position = $position, checked = $checked")
            }
        }
    }

    @OnItemClick(R.id.lvEvents)
    override fun getSelectedMemoToEdit(position: Int) {
        eventsData.setSelectedMemoToEdit(memoAdapter.getItem(position))
    }

    @OnClick(R.id.vDateTimeBackground)
    fun showDateTimeDialog() {
        val bundle = Bundle()
        bundle.putString(TV_DATE_KEY, tvDate.text.toString())
        bundle.putString(TV_TIME_KEY, tvTime.text.toString())

        val dateTimeDialog = DateTimeDialog()
        dateTimeDialog.arguments = bundle
        dateTimeDialog.show(supportFragmentManager, "datePicker")
    }

    @OnClick(R.id.fabNewMemo)
    fun fabClick() {
        eventsData.fabClick()
    }

    override fun setEditViewsGone() {
        emvMemo.clearFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(emvMemo.applicationWindowToken, 0)
        fabNewMemo.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_add_white_24px))
        TransitionManager.beginDelayedTransition(myLayout)
        constraintSet1.applyTo(myLayout)
        emvMemo.editState = false
        invalidateOptionsMenu()
    }

    override fun setEditViewsVisible() {
        TransitionManager.beginDelayedTransition(myLayout)
        constraintSet2.applyTo(myLayout)
        emvMemo.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(emvMemo, InputMethodManager.SHOW_IMPLICIT)
        fabNewMemo.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.ic_done_white_24px))
        emvMemo.editState = true
        invalidateOptionsMenu()
    }

    override fun getEditedMemo(): Memo {
        return Memo(
                tvDate.text.toString(), tvTime.text.toString(), emvMemo.text.toString())
    }

    override fun setEditedMemo(memo: Memo) {
        emvMemo.setText(memo.note)
        tvDate.text = memo.getDateString()
        tvTime.text = memo.getTimeString()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onDatePickerUpdateEvent(event: DatePickerUpdateEvent) {

        Log.d(TAG, "event receiver ${event.message.time}")
        val tmpDate = event.message.time
        tvDate.text = DateFormat.getDateInstance(DateFormat.SHORT).format(tmpDate)
        tvTime.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(tmpDate)
    }

    @Subscribe
    fun onMemoAdapterRefreshEvent(event: MemoAdapterRefreshEvent) {
        memoAdapter.refreshEvents()
        updateWidget()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.menuEdit).isVisible = !hasFocusNote()
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menuAbout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.about).setMessage(R.string.about)
                        .setMessage(getString(R.string.version)
                                + BuildConfig.VERSION_NAME + " "
                                + BuildConfig.VERSION_CODE)
                val dialog = builder.create()
                dialog.show()
                return true
            }
            R.id.menuSettings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.menuEdit -> {
                eventsData.menuEditClick()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun hasFocusNote(): Boolean {
        Log.d(TAG, "hasFocusNote --- return : " + emvMemo.hasFocus())
        return emvMemo.editState
    }

    override fun updateWidget() {
        val intent = Intent(this, WidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(this)
                .getAppWidgetIds(ComponentName(this, WidgetProvider::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        this.sendBroadcast(intent)
    }
}
