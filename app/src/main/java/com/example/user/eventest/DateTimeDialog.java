package com.example.user.eventest;

/*
 * Created by User on 25.12.2017.
 */

import android.app.DatePickerDialog;
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

import com.example.user.eventest.eventbus.events.DatePickerUpdateEvent;

import java.text.DateFormat;
import java.util.Calendar;

import de.greenrobot.event.EventBus;

public class DateTimeDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    boolean isDatePickerShown = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View promptsView = li.inflate(R.layout.date_time_pick, null);
        final Button butDateTime = promptsView.findViewById(R.id.butDateTime);

        butDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDatePickerShown) {
                    promptsView.findViewById(R.id.datePicker).setVisibility(View.INVISIBLE);
                    promptsView.findViewById(R.id.timePicker).setVisibility(View.VISIBLE);
                    butDateTime.setText(R.string.date);
                } else {
                    promptsView.findViewById(R.id.datePicker).setVisibility(View.VISIBLE);
                    promptsView.findViewById(R.id.timePicker).setVisibility(View.INVISIBLE);
                    butDateTime.setText(R.string.time);
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
                                // get user input and set it to result
                                // edit text
//                                        result.setText(userInput.getText());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        return alertDialog;

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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DateFormat sdf = DateFormat.getDateInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        EventBus.getDefault().post(new DatePickerUpdateEvent(sdf.format(calendar.getTime())));
    }
}

