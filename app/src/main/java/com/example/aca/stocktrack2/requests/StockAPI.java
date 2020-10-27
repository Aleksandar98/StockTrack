package com.example.aca.stocktrack2.requests;

import com.example.aca.stocktrack2.Models.Company;
import com.example.aca.stocktrack2.requests.Responses.StockResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface StockAPI {

    @GET("stock/{company}/book?filter=latestPrice,sector,companyName,symbol,change,changePercent,")
    Call<StockResponse> getStock(
        @Path("company") String company);

    @GET("ref-data/symbols?filter=symbol,name")
    Call<List<Company>>getCompanyNames();

    }

