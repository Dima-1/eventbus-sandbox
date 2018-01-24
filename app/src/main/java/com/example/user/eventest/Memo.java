package com.example.user.eventest;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.user.eventest.room.DateConverterDB;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/*
 * Created by User on 25.12.2017.
 */
@Entity
public class Memo {

    @PrimaryKey(autoGenerate = true)
    private long memoID;
    @ColumnInfo(name = "date")
    @TypeConverters({DateConverterDB.class})
    private Date date;
    @ColumnInfo(name = "note")
    private String note;

    @Ignore
    public Memo() {
    }


    public Memo(Date date, String note) {
        this.date = date;
        this.note = note;
    }

    public Memo(String date, String data) {
        DateFormat dateFormat = DateFormat
                .getDateInstance(DateFormat.SHORT);

        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.note = data;
    }

    public long getMemoID() {
        return memoID;
    }

    public void setMemoID(long memoID) {
        this.memoID = memoID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDateString() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        return (date == null) ? "" : dateFormat.format(date);
    }

    public String getTimeString() {
        DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
        return (date == null) ? "" : dateFormat.format(date);
    }
}
