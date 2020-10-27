package com.example.aca.stocktrack2.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.aca.stocktrack2.Models.Company;
import com.example.aca.stocktrack2.Models.Stock;
import com.example.aca.stocktrack2.repository.StockRepository;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private StockRepository stockRepository;

    public MainActivityViewModel() {
        stockRepository = StockRepository.getInstance();
    }

    public LiveData<Stock> getStock(){return stockRepository.getStock();}

    public void searchStock(String companyName){ stockRepository.searchStock(companyName);}

    public LiveData<List<Company>> getCompanies(){return stockRepository.getCompanies();}

    public void queryCompanies(){stockRepository.queryCompanies();}


}
