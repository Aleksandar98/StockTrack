package com.example.aca.stocktrack2.requests;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.aca.stocktrack2.Utils.Constants.BASE_URL;

public class ServiceGenerator {

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static StockAPI stockAPI = retrofit.create(StockAPI.class);

    public static StockAPI getStockAPI(){
        return stockAPI;
    }
}
