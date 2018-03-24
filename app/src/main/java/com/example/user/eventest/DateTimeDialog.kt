package com.example.user.eventest

import android.app.Dialog
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.example.user.eventest.eventbus.events.DatePickerUpdateEvent
import org.greenrobot.eventbus.EventBus
import java.text.DateFormat
import java.text.ParseException
import java.util.*

/**
 * Created by DR
 * on 25.12.2017.
 */

class DateTimeDialog : DialogFragment() {
    private var mDate: Date = Date()
    private var isDatePickerShown = false
    @BindView(R.id.datePicker)
    lateinit var datePicker: DatePicker
    @BindView(R.id.timePicker)
    lateinit var timePicker: TimePicker
    @BindView(R.id.fabDateTime)
    lateinit var fabDateTime: FloatingActionButton
    private var unbinder: Unbinder? = null

    @Suppress("DEPRECATION")
    @Override
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments
        var date = ""
        var time = ""
        if (bundle != null) {
            date = bundle.getString(MainActivity.TV_DATE_KEY, "")
            time = bundle.getString(MainActivity.TV_TIME_KEY, "")
        }
        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        try {
            mDate = dateFormat.parse("$date $time")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val calendarDate = Calendar.getInstance()
        calendarDate.time = mDate
        val li = LayoutInflater.from(activity)
        val dateTimeLayout = li.inflate(R.layout.date_time_picker, null)
        unbinder = ButterKnife.bind(this, dateTimeLayout)

        updateDatePicker(calendarDate)
        updateTimePicker(calendarDate)

        val alertDialogBuilder = AlertDialog.Builder(dateTimeLayout.context)
        alertDialogBuilder
                .setView(dateTimeLayout)
                .setCancelable(false)
                .setPositiveButton(R.string.OK,
                        { _, _ ->
                            val calendar = Calendar.getInstance()
                            val hour: Int
                            val minute: Int
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                hour = timePicker.currentHour
                                minute = timePicker.currentMinute
                            } else {
                                hour = timePicker.hour
                                minute = timePicker.minute
                            }
                            calendar.set(datePicker.year,
                                    datePicker.month,
                                    datePicker.dayOfMonth,
                                    hour, minute)
                            EventBus.getDefault().post(DatePickerUpdateEvent(calendar))
                        })
                .setNegativeButton(R.string.cancel, { dialog, _ -> dialog.cancel() })
        val alertDialog = alertDialogBuilder.create()
        alertDialog.window.attributes.windowAnimations = R.style.DateTimeDialogTheme

        return alertDialog

    }

    @OnClick(R.id.fabDateTime)
    fun switchPickers() {
        if (isDatePickerShown) {
            datePicker.visibility = View.INVISIBLE
            timePicker.visibility = View.VISIBLE
            fabDateTime.setImageResource(
                    R.drawable.ic_date_range_white_24px)
        } else {
            datePicker.visibility = View.VISIBLE
            timePicker.visibility = View.INVISIBLE
            fabDateTime.setImageResource(
                    R.drawable.ic_access_time_white_24px)
        }
        isDatePickerShown = !isDatePickerShown
    }

    @Suppress("DEPRECATION")
    private fun updateTimePicker(c: Calendar) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            timePicker.currentHour = c.get(Calendar.HOUR)
            timePicker.currentMinute = c.get(Calendar.MINUTE)
        } else {
            timePicker.hour = c.get(Calendar.HOUR)
            timePicker.minute = c.get(Calendar.MINUTE)
        }
    }

    private fun updateDatePicker(c: Calendar) {
        datePicker.updateDate(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH))
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

}

