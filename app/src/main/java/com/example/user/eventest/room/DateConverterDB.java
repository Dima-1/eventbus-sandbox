package com.example.user.eventest.room;

import android.annotation.SuppressLint;
import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 24.01.2018.
 */

public class DateConverterDB {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @SuppressLint("SimpleDateFormat")
    private DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    @TypeConverter
    public Date dateFromString(String value) {
        try {
            return value == null ? null : dateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TypeConverter
    public String stringFromDate(Date date) {

        if (date == null) {
            return null;
        } else {
            return dateFormat.format(date);
        }
    }
}
