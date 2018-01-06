package com.example.user.eventest;

/*
 * Created by User on 25.12.2017.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.user.eventest.eventbus.events.DatePickerUpdateEvent;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

public class DateTimeDialog extends DialogFragment {
    boolean isDatePickerShown = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.date_time_picker, null);
        final Button butDateTime = promptsView.findViewById(R.id.btnDateTime);

        butDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDatePickerShown) {
                    promptsView.findViewById(R.id.datePicker).setVisibility(View.INVISIBLE);
                    promptsView.findViewById(R.id.timePicker).setVisibility(View.VISIBLE);
                    butDateTime.setText(R.string.date);
                    butDateTime.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_date_range_black_48px, 0, 0, 0);
                } else {
                    promptsView.findViewById(R.id.datePicker).setVisibility(View.VISIBLE);
                    promptsView.findViewById(R.id.timePicker).setVisibility(View.INVISIBLE);
                    butDateTime.setText(R.string.time);
                    butDateTime.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_access_time_black_48px, 0, 0, 0);
                }
                isDatePickerShown = !isDatePickerShown;
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatePicker datePicker = promptsView.findViewById(R.id.datePicker);
                                TimePicker timePicker = promptsView.findViewById(R.id.timePicker);
                                Calendar calendar = Calendar.getInstance();
                                int dayOfMonth = datePicker.getDayOfMonth();
                                int month = datePicker.getMonth() + 1;
                                int year = datePicker.getYear();
                                int hour;
                                int minute;
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                    hour = timePicker.getCurrentHour();
                                    minute = timePicker.getCurrentMinute();
                                } else {
                                    hour = timePicker.getHour();
                                    minute = timePicker.getMinute();
                                }
                                calendar.set(year, month, dayOfMonth, hour, minute);
                                EventBus.getDefault().post(new DatePickerUpdateEvent(calendar));
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        return alertDialogBuilder.create();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
            int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
            getDialog().getWindow().setLayout(width, height);
        }
    }
}

