package com.example.aca.stocktrack2.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aca.stocktrack2.MainActivity;
import com.example.aca.stocktrack2.Models.Stock;
import com.example.aca.stocktrack2.R;
import com.example.aca.stocktrack2.UpdateService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class mRecyclerAdapter extends RecyclerView.Adapter<mViewHolder> {

    Context context;
    List<Stock> list;
    HashMap<Stock,String> stockAndLimit = new HashMap<>();
    MutableLiveData<Boolean> isEmpty ;
    private onStockClick onStockClick;

    public mRecyclerAdapter(Context context,onStockClick onStockClick) {
        this.context = context;
        list = new ArrayList<>();
        this.onStockClick = onStockClick;
        isEmpty = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View layoutInflater = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new mViewHolder(layoutInflater,onStockClick);
    }


    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int i) {

        holder.title.setText(list.get(i).getCompanyName());
        holder.desc.setText(list.get(i).getSector());
        holder.currentPrice.setText(String.valueOf(list.get(i).getLatestPrice()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void addStockToList(Stock stock) {
        list.add(stock);
        notifyDataSetChanged();
    }
    public void addStockToMap(Stock stock,String limit){
        stockAndLimit.put(stock,limit) ;
        isEmpty.postValue(false);
    }
    public void deleteItem(int position){
        stockAndLimit.remove(list.get(position));
        list.remove(position);
        notifyDataSetChanged();
        if(!stockAndLimit.isEmpty()) {
            Intent intent = new Intent(context, UpdateService.class);
            context.stopService(intent);
            intent.putExtra("newStocks", stockAndLimit);
            context.startService(intent);
            isEmpty.postValue(false);
        }else {
            Intent intent = new Intent(context, UpdateService.class);
            context.stopService(intent);
            isEmpty.postValue(true);
        }

    }

    public HashMap getStockLimitMap(){
        return stockAndLimit;
    }

    public LiveData<Boolean> getIsEmpty (){return isEmpty;}

}