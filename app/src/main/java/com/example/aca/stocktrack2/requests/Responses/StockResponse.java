package com.example.aca.stocktrack2.requests.Responses;

import com.example.aca.stocktrack2.Models.Stock;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockResponse {

    @SerializedName("quote")
    @Expose()
    private Stock stock;

    public Stock getStock() {
        return stock;
    }

    @Override
    public String toString() {
        return "StockResponse{" +
                "stock=" + stock +
                '}';
    }
}
