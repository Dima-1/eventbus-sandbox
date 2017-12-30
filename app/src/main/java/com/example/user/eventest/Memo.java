package com.example.user.eventest;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/*
 * Created by User on 25.12.2017.
 */
@Entity
public class Memo {

    @PrimaryKey(autoGenerate = true)
    private int memoID;
    @ColumnInfo(name = "name")
    private String date;
    @ColumnInfo(name = "note")
    private String note;

    Memo() {
    }

    public Memo(String date, String data) {
        this.date = date;
        this.note = data;
    }

    int getMemoID() {
        return memoID;
    }

    void setMemoID(int memoID) {
        this.memoID = memoID;
    }

    public String getDate() {
        return date;
    }

    void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    void setNote(String note) {
        this.note = note;
    }
}
