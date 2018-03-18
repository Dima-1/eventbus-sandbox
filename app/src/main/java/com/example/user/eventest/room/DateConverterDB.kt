package com.example.user.eventest.room

import android.annotation.SuppressLint
import android.arch.persistence.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by DR
 * on 24.01.2018.
 */
const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

class DateConverterDB {
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat(DATE_FORMAT)

    @TypeConverter
    fun dateFromString(value: String): Date = dateFormat.parse(value)

    @TypeConverter
    fun stringFromDate(date: Date): String = dateFormat.format(date)
}
