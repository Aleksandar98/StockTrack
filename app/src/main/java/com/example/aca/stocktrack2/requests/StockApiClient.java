package com.example.aca.stocktrack2.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.aca.stocktrack2.Models.Company;
import com.example.aca.stocktrack2.Models.Stock;
import com.example.aca.stocktrack2.requests.Responses.StockResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockApiClient {

    private static final String TAG = "StockApiClient";

    private static StockApiClient instance;

    private MutableLiveData<Stock> stock;
    private MutableLiveData<List<Company>> companies;

    public static StockApiClient getInstance(){
        if(instance==null){
            instance = new StockApiClient();
        }
        return instance;
    }

    public StockApiClient() {
        stock = new MutableLiveData<>();
        companies = new MutableLiveData<>();
    }

    public LiveData<Stock> getStock(){return stock;}
    public LiveData<List<Company>> getCompanies(){return companies;}

    public void searchStock(String companyName){
        ServiceGenerator.getStockAPI().getStock(companyName).enqueue(new Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {
                if(response!=null) {
                  //  Log.d(TAG, "onResponse: " + response.body().getStock());
                    stock.postValue(response.body().getStock());
                }
            }

            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {

            }
        });
    }

    public void queryCompanies(){
        ServiceGenerator.getStockAPI().getCompanyNames().enqueue(new Callback<List<Company>>() {
            @Override
            public void onResponse(Call<List<Company>> call, Response<List<Company>> response) {
                if(response!=null){
                    companies.postValue(response.body());
                }
                Log.d(TAG, "onResponse: "+response.body());
            }

            @Override
            public void onFailure(Call<List<Company>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
}
