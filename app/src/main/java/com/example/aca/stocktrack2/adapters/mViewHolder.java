package com.example.aca.stocktrack2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.aca.stocktrack2.R;

public class mViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title,desc,currentPrice;
    onStockClick onStockClick;
    public mViewHolder(@NonNull View itemView,onStockClick onStockClick) {
        super(itemView);

        this.onStockClick = onStockClick;

        title  = itemView.findViewById(R.id.ItemTitle);
        desc = itemView.findViewById(R.id.ItemDesc);
        currentPrice = itemView.findViewById(R.id.currentPrice);
    }

    @Override
    public void onClick(View v) {
        onStockClick.onStockClick(getAdapterPosition());
    }
}