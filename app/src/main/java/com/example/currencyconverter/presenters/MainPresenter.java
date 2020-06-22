package com.example.currencyconverter.presenters;

import android.content.Context;

import com.example.currencyconverter.contract.MainContract;
import com.example.currencyconverter.domain.pojo.Item;
import com.example.currencyconverter.domain.pojo.Record;
import com.example.currencyconverter.domain.pojo.ValCurs;
import com.example.currencyconverter.domain.pojo.Valuta;
import com.example.currencyconverter.repo.HistoryRepo;
import com.example.currencyconverter.service.APIService;

import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainPresenter {

    private static final String BASE_URL = "http://www.cbr.ru/scripts/";


    private MainContract mainContract;
    private APIService apiService;
    private HistoryRepo historyRepo;

    public MainPresenter(MainContract mainContract) {
        this.mainContract = mainContract;
        initService();
    }

    public void convert() {
        int amount = mainContract.getAmount();
        String fromName = mainContract.getFromName();
        String toName = mainContract.getToName();
        String fromCode = mainContract.getFromCode();
        String toCode = mainContract.getToCode();
        String date = mainContract.getDate();


        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                new Thread(new Runnable() {

                    boolean isEmptyData = false;

                    @Override
                    public void run() {


                        Record from = getRecord(fromCode);
                        Record to = getRecord(toCode);
                        int fromNom = Integer.parseInt(from.getNominal());
                        double fromVal = Double.parseDouble(from.getValue().replace(",", "."));
                        int toNom = Integer.parseInt(to.getNominal());
                        double toVal = Double.parseDouble(to.getValue().replace(",", "."));

                        double result = (toNom * fromVal) / (fromNom * toVal);
                        emitter.onNext(isEmptyData ? "Нет данных." : String.valueOf(result * amount));
                        if (!isEmptyData) {
                            historyRepo.save(mainContract.getDate() + " " + mainContract.getTime() + "\n" + amount + " " + fromName + "\n" + result + " " + toName);
                        }
                        isEmptyData = false;

                    }


                    private Record getRecord(String code) {

                        if (Item.RUB_ITEM_PARENT_CODE.equals(code)) {
                            return Record.RUB_RECORD;
                        } else {


                            Call<ValCurs> valCursCall = apiService.loadValCurs(date, date, code);
                            Record record;

                            try {
                                List<Record> record1 = valCursCall.execute().body().getRecord();
                                if (record1 != null) {
                                    record = record1.get(0);
                                } else {
                                    isEmptyData = true;
                                    record = new Record();
                                    record.setNominal("1");
                                    record.setValue("1");
                                }
                            } catch (IOException e) {
                                record = null;
                                e.printStackTrace();
                            }
                            return record;
                        }
                    }


                }).start();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String aDouble) {
                        mainContract.updateResult(aDouble);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void updateSpinners() {
        apiService.loadValuta("0").enqueue(new Callback<Valuta>() {
            @Override
            public void onResponse(Call<Valuta> call, Response<Valuta> response) {
                List<Item> item = response.body().getItem();
                item.add(0, Item.RUB_ITEM);
                mainContract.updateSpinners(item);
            }

            @Override
            public void onFailure(Call<Valuta> call, Throwable t) {

            }
        });
    }

    private void initService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);
    }

    public void initRepo(Context context) {
        historyRepo = new HistoryRepo(context);
    }
}
