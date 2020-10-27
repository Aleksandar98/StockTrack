package com.example.aca.stocktrack2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.aca.stocktrack2.Models.Company;
import com.example.aca.stocktrack2.Models.Stock;
import com.example.aca.stocktrack2.Utils.VerticalSpaceDecorator;
import com.example.aca.stocktrack2.ViewModels.MainActivityViewModel;
import com.example.aca.stocktrack2.adapters.SwipeToDeleteCallback;
import com.example.aca.stocktrack2.adapters.mRecyclerAdapter;
import com.example.aca.stocktrack2.adapters.onStockClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements onStockClick {

    MainActivityViewModel viewModel;


    Button trackBtn;
    RecyclerView recyclerView;
    mRecyclerAdapter adapter;

    EditText StockLimint,Interval;
    TextView dividerTitle;

    ImageButton aboveBtn,belowBtn;

   List<String>searchItems = new ArrayList<>();
   HashMap<String,String> hashMap = new HashMap<>();

   boolean isAbove  = true;


    private TextView TitleText,SectorText,ChangePercentText,ChangeText,LatestPriceText;


    private static final String TAG = "MainActivity";
    private Stock currentStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TitleText = findViewById(R.id.TitleText);
        SectorText = findViewById(R.id.SectorText);
        ChangePercentText = findViewById(R.id.ChangePercentText);
        ChangeText = findViewById(R.id.ChangeText);
        LatestPriceText = findViewById(R.id.LatestPriceText);
        trackBtn = findViewById(R.id.TrackBtn);

        StockLimint = findViewById(R.id.editTextLimit);
        Interval = findViewById(R.id.editTextTime);
        dividerTitle = findViewById(R.id.dividerTxt);

        aboveBtn = findViewById(R.id.aboveBtn);
        belowBtn = findViewById(R.id.belowBtn);

        aboveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v.isActivated()) {
                    isAbove = true;
                    v.setBackground(getDrawable(R.drawable.btn_shape_pressed));
                    v.setActivated(true);
                    belowBtn.setBackground(getDrawable(R.drawable.btn_shape));
                    belowBtn.setActivated(false);
                }
                else {
                    isAbove = false;
                    v.setBackground(getDrawable(R.drawable.btn_shape));
                    v.setActivated(false);
                    belowBtn.setBackground(getDrawable(R.drawable.btn_shape_pressed));
                    belowBtn.setActivated(true);
                }
            }

        });
        belowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!v.isActivated()) {
                    isAbove = false;
                    aboveBtn.setBackground(getDrawable(R.drawable.btn_shape));
                    aboveBtn.setActivated(false);
                    v.setBackground(getDrawable(R.drawable.btn_shape_pressed));
                    v.setActivated(true);
                }
                else {
                    isAbove = true;
                    aboveBtn.setBackground(getDrawable(R.drawable.btn_shape_pressed));
                    aboveBtn.setActivated(true);
                    v.setBackground(getDrawable(R.drawable.btn_shape));
                    v.setActivated(false);
                }
            }
        });

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);


        initRecyclerView();

        initObservers();

        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));



        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentStock == null) {
                    Toast.makeText(MainActivity.this, "Please First Select A Stock", Toast.LENGTH_LONG).show();
                }else if(Interval.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter Update Interval", Toast.LENGTH_SHORT).show();
                }
                else if(StockLimint.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter Stock Limit", Toast.LENGTH_SHORT).show();
                }
                else {
                    adapter.addStockToList(currentStock);
                    adapter.addStockToMap(currentStock,StockLimint.getText().toString());
                    Intent intent = new Intent(v.getContext(), UpdateService.class);
                    intent.putExtra("newStocks", adapter.getStockLimitMap());
                    intent.putExtra("isAbove",isAbove);
                    intent.putExtra("interval",Interval.getText().toString());
                    startService(intent);

                }
            }
        });


    }

    private void initObservers() {

        viewModel.getCompanies().observe(this, new Observer<List<Company>>() {
            @Override
            public void onChanged(@Nullable List<Company> companies) {
                Log.d(TAG, "onChanged: number of companies" + companies.size());
                for(Company company:companies){
                    searchItems.add(company.getName());
                    hashMap.put(company.getName(),company.getSymbol());
                }
            }
        });



        viewModel.getStock().observe(this, new Observer<Stock>() {
            @Override
            public void onChanged(@Nullable Stock stock) {
                if(stock.getChange()>0) {
                    ChangePercentText.setTextColor(Color.parseColor("#54e031"));
                    ChangeText.setTextColor(Color.parseColor("#54e031"));
                }
                else {
                    ChangePercentText.setTextColor(Color.parseColor("#cc2c2c"));
                    ChangeText.setTextColor(Color.parseColor("#cc2c2c"));
                }

                currentStock = stock;
                TitleText.setText(stock.getCompanyName());
                SectorText.setText(stock.getSector());
                ChangePercentText.setText(String.valueOf(stock.getChangePercent())+"%");
                ChangeText.setText(String.valueOf(stock.getChange()));
                LatestPriceText.setText(String.valueOf(stock.getLatestPrice())+"USD");

            }
        });

        adapter.getIsEmpty().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean){
                    dividerTitle.setText("No Stocks are currently tracked");
                }else
                    dividerTitle.setText("Currently Tracking");
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycerView);
        adapter = new mRecyclerAdapter(this,this);
        recyclerView.addItemDecoration(new VerticalSpaceDecorator(30,this));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        viewModel.queryCompanies();


        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)     mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        searchAutoComplete.setThreshold(2);
        searchAutoComplete.setDropDownAnchor(R.id.action_search);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, searchItems);
        searchAutoComplete.setAdapter(adapter);

        SearchManager searchManager =
                (SearchManager) getSystemService(this.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String searchString=(String)parent.getItemAtPosition(position);
                searchAutoComplete.setText(""+searchString);

                viewModel.searchStock(hashMap.get(searchString));

                mSearchView.clearFocus();
            }
        });
        return true;

    }

    @Override
    public void onStockClick(int positrion) {

    }
}
