package com.example.user.eventest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.user.eventest.eventbus.events.DatePickerUpdateEvent;

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by DR
 * on 25.12.2017.
 */

public class DateTimeDialog extends DialogFragment {
    private Date mDate;
    boolean isDatePickerShown = false;
    @BindView(R.id.datePicker)
    DatePicker datePicker;
    @BindView(R.id.timePicker)
    TimePicker timePicker;
    @BindView(R.id.fabDateTime)
    FloatingActionButton fabDateTime;
    private Unbinder unbinder;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String date = "";
        String time = "";
        if (bundle != null) {
            date = bundle.getString("date", "");
            time = bundle.getString("time", "");
        }
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        try {
            mDate = dateFormat.parse(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(mDate);
        LayoutInflater li = LayoutInflater.from(getActivity());
        View dateTimeLayout = li.inflate(R.layout.date_time_picker, null);
        unbinder = ButterKnife.bind(this, dateTimeLayout);

        updateDatePicker(calendarDate);
        updateTimePicker(calendarDate);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder
                .setView(dateTimeLayout)
                .setCancelable(false)
                .setPositiveButton(R.string.OK,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Calendar calendar = Calendar.getInstance();
                                int hour;
                                int minute;
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                    hour = timePicker.getCurrentHour();
                                    minute = timePicker.getCurrentMinute();
                                } else {
                                    hour = timePicker.getHour();
                                    minute = timePicker.getMinute();
                                }
                                calendar.set(datePicker.getYear(),
                                        datePicker.getMonth(),
                                        datePicker.getDayOfMonth(),
                                        hour, minute);
                                EventBus.getDefault().post(new DatePickerUpdateEvent(calendar));
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DateTimeDialogTheme;

        return alertDialog;

    }

    @OnClick(R.id.fabDateTime)
    void switchPickers() {
        if (isDatePickerShown) {
            datePicker.setVisibility(View.INVISIBLE);
            timePicker.setVisibility(View.VISIBLE);
            fabDateTime.setImageResource(
                    R.drawable.ic_date_range_white_24px);
        } else {
            datePicker.setVisibility(View.VISIBLE);
            timePicker.setVisibility(View.INVISIBLE);
            fabDateTime.setImageResource(
                    R.drawable.ic_access_time_white_24px);
        }
        isDatePickerShown = !isDatePickerShown;
    }

    private void updateTimePicker(Calendar c) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            timePicker.setCurrentHour(c.get(Calendar.HOUR));
            timePicker.setCurrentMinute(c.get(Calendar.MINUTE));
        } else {
            timePicker.setHour(c.get(Calendar.HOUR));
            timePicker.setMinute(c.get(Calendar.MINUTE));
        }
    }

    private void updateDatePicker(Calendar c) {
        datePicker.updateDate(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

