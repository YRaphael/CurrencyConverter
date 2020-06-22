package com.example.currencyconverter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currencyconverter.adapters.Adapter;
import com.example.currencyconverter.contract.HistoryContract;
import com.example.currencyconverter.presenters.HistoryPresenter;


public class HistoryFragment extends Fragment implements HistoryContract {

    private HistoryPresenter historyPresenter;
    private RecyclerView recyclerView;

    public HistoryFragment() {

    }

    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_history, container, false);
        historyPresenter = new HistoryPresenter(this, getContext());
        recyclerView = (RecyclerView) inflate.findViewById(R.id.history_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        historyPresenter.updateHistory();
        return inflate;
    }

    @Override
    public void updateHistory(Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }
}