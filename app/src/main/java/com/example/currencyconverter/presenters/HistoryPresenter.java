package com.example.currencyconverter.presenters;

import android.content.Context;

import com.example.currencyconverter.adapters.Adapter;
import com.example.currencyconverter.contract.HistoryContract;
import com.example.currencyconverter.domain.db.HistoryItem;
import com.example.currencyconverter.repo.HistoryRepo;

import java.util.List;

public class HistoryPresenter {
    private HistoryContract contract;
    private HistoryRepo historyRepo;
    private Context ctx;

    public HistoryPresenter(HistoryContract contract, Context ctx) {
        this.contract = contract;
        historyRepo = new HistoryRepo(ctx);
    }

    public void updateHistory() {
        List<HistoryItem> load = historyRepo.load();
        Adapter adapter = new Adapter(load);
        contract.updateHistory(adapter);
    }
}
