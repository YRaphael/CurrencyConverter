package com.example.currencyconverter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.currencyconverter.contract.MainContract;
import com.example.currencyconverter.domain.pojo.Item;
import com.example.currencyconverter.contract.DateChangeable;
import com.example.currencyconverter.presenters.MainPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainFragment extends Fragment implements MainContract, DateChangeable {

    private MainPresenter mainPresenter;
    private EditText editText;
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private TextView textVIew;
    private TextView textDate;

    private int day;
    private int month;
    private int year;

    {
        Calendar calendar = Calendar.getInstance();

        setDay(calendar.get(Calendar.DATE));
        setMonth(calendar.get(Calendar.MONTH));
        setYear(calendar.get(Calendar.YEAR));
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private int day;
        private int month;
        private int year;
        private DateChangeable ctx;

        public DatePickerFragment(int day,
                                  int month,
                                  int year,
                                  DateChangeable ctx) {
            this.day = day;
            this.month = month;
            this.year = year;
            this.ctx = ctx;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            ctx.setDay(i2);
            ctx.setMonth(i1);
            ctx.setYear(i);
            ctx.setCurrentDate();
        }
    }

    public MainFragment() {
        this.mainPresenter = new MainPresenter(this);
    }


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainPresenter.initRepo(getContext());
        editText = (EditText) view.findViewById(R.id.currencyAmount);
        spinnerFrom = (Spinner) view.findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner) view.findViewById(R.id.spinnerTo);
        textVIew = (TextView) view.findViewById(R.id.result);
        textDate = (TextView) view.findViewById(R.id.textDate);
        textDate.setText(getDate());
        Button btnConvert = (Button) view.findViewById(R.id.buttonConvert);
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.convert();
            }
        });

        Button btnSetDate = (Button) view.findViewById(R.id.buttondSetDate);
        DateChangeable dateChangeable = this;
        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new MainFragment.DatePickerFragment(getDay(), getMonth(), getYear(), dateChangeable);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        mainPresenter.updateSpinners();
    }

    @Override
    public int getAmount() {
        String text = editText.getText().toString();
        return Integer.parseInt(text.isEmpty() ? "1" : text);
    }

    @Override
    public String getFromCode() {
        return ((Item) spinnerFrom.getSelectedItem()).getParentCode();
    }

    @Override
    public String getToCode() {
        return ((Item) spinnerTo.getSelectedItem()).getParentCode();
    }

    @Override
    public String getFromName() {
        return ((Item) spinnerFrom.getSelectedItem()).getName();
    }

    @Override
    public String getToName() {
        return ((Item) spinnerTo.getSelectedItem()).getName();
    }

    @Override
    public void updateResult(String value) {
        textVIew.setText(value);
    }

    @Override
    public void updateSpinners(List<Item> result) {
        ArrayAdapter<Item> adapterFrom = getItemArrayAdapter(result);
        ArrayAdapter<Item> adapterTo = getItemArrayAdapter(result);

        spinnerFrom.setAdapter(adapterTo);
        spinnerTo.setAdapter(adapterFrom);

    }

    @Override
    public String getDate() {
        return ((day < 10) ? "0" + day : day) + "/" + ((month < 10) ? "0" + month : month) + "/" + year;
    }

    private ArrayAdapter<Item> getItemArrayAdapter(List<Item> result) {
        ArrayAdapter<Item> adapterFrom = new ArrayAdapter<Item>(
                this.getContext(),
                android.R.layout.simple_list_item_1,
                result.toArray(new Item[0])
        );

        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapterFrom;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month + 1;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month - 1;
    }

    public int getYear() {
        return year;
    }

    public void setCurrentDate() {
        textDate.setText(getDate());
    }

    public String getTime() {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        return currentTime;
    }
}