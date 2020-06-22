package com.example.currencyconverter.service;

import com.example.currencyconverter.domain.pojo.ValCurs;
import com.example.currencyconverter.domain.pojo.Valuta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("XML_dynamic.asp")
    Call<ValCurs> loadValCurs(@Query("date_req1") String date_req1, @Query("date_req2") String date_req2, @Query("VAL_NM_RQ") String VAL_NM_RQ);

    @GET("XML_val.asp?d=0")
    Call<Valuta> loadValuta(@Query("d") String d);
}
