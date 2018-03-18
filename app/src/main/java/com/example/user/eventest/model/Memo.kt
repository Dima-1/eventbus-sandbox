package com.example.user.eventest.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.text.DateFormat
import java.text.ParseException
import java.util.*

/**
 * Created by DR
 * on 25.12.2017.
 */
@Entity
class Memo(@PrimaryKey(autoGenerate = true)
           var memoID: Long = 0,
           var date: Date,
           var note: String?) {

    @Ignore
    constructor(date: Date, note: String) : this(0, date, note) {
        this.date = date
    }

    @Ignore
    constructor(date: String, time: String, note: String) : this(0, Date(), note) {
        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        try {
            this.date = dateFormat.parse("$date $time")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    fun getDateString(): String = DateFormat.getDateInstance(DateFormat.SHORT).format(date)

    fun getTimeString(): String = DateFormat.getTimeInstance(DateFormat.SHORT).format(date)

}
