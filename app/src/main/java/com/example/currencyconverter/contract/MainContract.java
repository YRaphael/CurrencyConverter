package com.example.currencyconverter.contract;

import com.example.currencyconverter.domain.pojo.Item;

import java.util.List;

public interface MainContract  {
    int getAmount();

    String getFromCode();

    String getToCode();

    String getFromName();

    String getToName();


    void updateResult(String value);

    void updateSpinners(List<Item> item);

    String getDate();

    String getTime();
}
