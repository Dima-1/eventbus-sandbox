package com.example.user.eventest;

/*
 * Created by User on 25.12.2017.
 */

public class Memo {
    private String date;
    private String note;

    Memo(String data) {
        this("", data);
    }

    public Memo(String date, String data) {
        this.date = date;
        this.note = data;
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
