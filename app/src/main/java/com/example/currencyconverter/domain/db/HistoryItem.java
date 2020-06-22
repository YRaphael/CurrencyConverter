package com.example.currencyconverter.domain.db;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HistoryItem {
    private String id;
    private String time;
    private String from;
    private String to;

    public static List<HistoryItem> from(Cursor cursor) {
        cursor.moveToFirst();
        List<HistoryItem> historyItems = new ArrayList<>();
        if (cursor.getCount() > 0) {

            while (!cursor.isLast()) {
                getNewHistoryItem(cursor, historyItems);
                cursor.moveToNext();
            }
            getNewHistoryItem(cursor, historyItems);
        }
        return historyItems;
    }

    private static void getNewHistoryItem(Cursor cursor, List<HistoryItem> historyItems) {
        HistoryItem historyItem = new HistoryItem(cursor.getString(1), cursor.getString(2), cursor.getString(3));
        historyItem.setId(cursor.getString(0));
        historyItems.add(historyItem);
    }

    public HistoryItem(String time, String from, String to) {
        this.id = UUID.randomUUID().toString();
        this.time = time;
        this.from = from;
        this.to = to;
    }

    public HistoryItem() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
