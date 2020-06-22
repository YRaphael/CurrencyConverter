package com.example.currencyconverter.repo;

import android.content.Context;

import com.example.currencyconverter.domain.db.HistoryItem;
import com.example.currencyconverter.helpers.DatabaseHelper;

import java.util.List;

public class HistoryRepo {

    private DatabaseHelper dbHelper;

    private List<String> historyList = null;

    public HistoryRepo(Context ctx) {
        dbHelper = new DatabaseHelper(ctx);
    }

    public List<HistoryItem> load() {
        return dbHelper.getHistory();

    }

    public void save(String historyElement) {
        String[] split = historyElement.split("\n");
        HistoryItem historyItem = new HistoryItem(split[0], split[1], split[2]);
        dbHelper.addHistoryItem(historyItem);
    }


}
