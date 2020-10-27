package com.example.aca.stocktrack2.repository;

import android.arch.lifecycle.LiveData;

import com.example.aca.stocktrack2.Models.Company;
import com.example.aca.stocktrack2.Models.Stock;
import com.example.aca.stocktrack2.requests.StockApiClient;

import java.util.List;

public class StockRepository {

    private StockApiClient stockApiClient;
    private static StockRepository instance;

    public static StockRepository getInstance(){
        if(instance==null){
            instance = new StockRepository();
        }
        return instance;
    }

    public StockRepository() {
      stockApiClient = StockApiClient.getInstance();
    }

    public LiveData<Stock> getStock(){return stockApiClient.getStock();}

    public LiveData<List<Company>> getCompanies(){return stockApiClient.getCompanies();}

    public void searchStock(String companyName){ stockApiClient.searchStock(companyName);}

    public void queryCompanies(){stockApiClient.queryCompanies();}

}
