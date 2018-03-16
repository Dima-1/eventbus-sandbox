package com.example.user.eventest.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.text.DateFormat
import java.text.ParseException
import java.util.*

/**
 * Created by DR
 * on 25.12.2017.
 */
@Entity
class Memo() {
    @PrimaryKey(autoGenerate = true)
    var memoID: Long = 0
    var date: Date? = Date()
    var note: String? = ""

    constructor(date: Date, note: String) : this() {
        this.date = date
        this.note = note
    }

    constructor(date: String, data: String) : this() {
        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
        try {
            this.date = dateFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        note = data

    }

    constructor(date: String, time: String, data: String) : this() {
        val dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        try {
            this.date = dateFormat.parse("$date $time")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        this.note = data
    }

    fun getDateString(): String = DateFormat.getDateInstance(DateFormat.SHORT).format(date)

    fun getTimeString(): String = DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
}
