package com.example.aca.stocktrack2.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Stock implements Parcelable{

    double latestPrice;
    double change;
    double changePercent;
    String sector;
    String companyName;
    String symbol;

    public Stock() {
    }

    protected Stock(Parcel in) {
        latestPrice = in.readDouble();
        change = in.readDouble();
        changePercent = in.readDouble();
        sector = in.readString();
        companyName = in.readString();
        symbol = in.readString();
    }

    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    public double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "latestPrice=" + latestPrice +
                ", change=" + change +
                ", changePercent=" + changePercent +
                ", sector='" + sector + '\'' +
                ", companyName='" + companyName + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latestPrice);
        dest.writeDouble(change);
        dest.writeDouble(changePercent);
        dest.writeString(sector);
        dest.writeString(companyName);
        dest.writeString(symbol);
    }
}
