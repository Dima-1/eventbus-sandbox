package com.example.user.eventest

import android.app.Dialog
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.user.eventest.eventbus.events.DatePickerUpdateEvent
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.date_time_picker.view.*
import org.greenrobot.eventbus.EventBus
import java.text.DateFormat
import java.text.ParseException
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by DR
 * on 25.12.2017.
 */

class DateTimeDialog : DialogFragment() {
    private var mDate: Date = Date()
    private var isDatePickerShown = false
    private var dateTimeLayout: View by Delegates.notNull()

    @Override
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments
        var date = ""
        var time = ""
        if (bundle != null) {
            date = bundle.getString(TV_DATE_KEY, "")
            time = bundle.getString(TV_TIME_KEY, "")
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
        dateTimeLayout = li.inflate(R.layout.date_time_picker, null)

        updateDatePicker(calendarDate)
        updateTimePicker(calendarDate)
        repaintPickers()

        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(dateTimeLayout.context)
        alertDialogBuilder
                .setView(dateTimeLayout)
                .setCancelable(false)
        val alertDialog = alertDialogBuilder.create()
        alertDialog.window.attributes.windowAnimations = R.style.DateTimeDialogTheme
        dateTimeLayout.btnOK.setOnClickListener {
            val calendar = getCalendarFromPickers()
            EventBus.getDefault().post(DatePickerUpdateEvent(calendar))
            dialog.cancel()
        }
        dateTimeLayout.btnCancel.setOnClickListener { dialog.cancel() }
        dateTimeLayout.fabDateTime.setOnClickListener {
            isDatePickerShown = !isDatePickerShown
            repaintPickers()
        }
        return alertDialog
    }

    @Suppress("DEPRECATION")
    private fun getCalendarFromPickers(): Calendar {
        val hour: Int
        val minute: Int
        val calendar = Calendar.getInstance()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            hour = dateTimeLayout.timePicker.currentHour
            minute = dateTimeLayout.timePicker.currentMinute
        } else {
            hour = dateTimeLayout.timePicker.hour
            minute = dateTimeLayout.timePicker.minute
        }
        calendar.set(dateTimeLayout.datePicker.year,
                dateTimeLayout.datePicker.month,
                dateTimeLayout.datePicker.dayOfMonth,
                hour, minute)
        return calendar
    }

    private fun repaintPickers() {
        clearFindViewByIdCache()
        if (isDatePickerShown) {
            dateTimeLayout.datePicker.visibility = View.VISIBLE
            dateTimeLayout.timePicker.visibility = View.INVISIBLE
            dateTimeLayout.fabDateTime.setImageResource(
                    R.drawable.ic_access_time_white_24px)
            dateTimeLayout.tvNow.text = DateFormat.getTimeInstance(DateFormat.SHORT)
                    .format(getCalendarFromPickers().time)
        } else {
            dateTimeLayout.datePicker.visibility = View.INVISIBLE
            dateTimeLayout.timePicker.visibility = View.VISIBLE
            dateTimeLayout.fabDateTime.setImageResource(
                    R.drawable.ic_date_range_white_24px)
            dateTimeLayout.tvNow.text = DateFormat.getDateInstance(DateFormat.SHORT)
                    .format(getCalendarFromPickers().time)
        }

    }

    @Suppress("DEPRECATION")
    private fun updateTimePicker(c: Calendar) {
        clearFindViewByIdCache()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            dateTimeLayout.timePicker.currentHour = c[Calendar.HOUR]
            dateTimeLayout.timePicker.currentMinute = c[Calendar.MINUTE]
        } else {
            dateTimeLayout.timePicker.hour = c[Calendar.HOUR]
            dateTimeLayout.timePicker.minute = c[Calendar.MINUTE]
        }
    }

    private fun updateDatePicker(c: Calendar) {
        clearFindViewByIdCache()
        dateTimeLayout.datePicker.updateDate(
                c[Calendar.YEAR],
                c[Calendar.MONTH],
                c[Calendar.DAY_OF_MONTH])
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog = dialog
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
}

