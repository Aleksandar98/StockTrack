package com.example.aca.stocktrack2;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.aca.stocktrack2.Models.Stock;
import com.example.aca.stocktrack2.requests.Responses.StockResponse;
import com.example.aca.stocktrack2.requests.ServiceGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aca.stocktrack2.Utils.App.CHANNEL_ID;

public class UpdateService extends Service {

    private static final String TAG = "UpdateService";

    Timer timer = new Timer();
    PendingIntent pendingIntent;
    Stock currentStock;
   // List<Stock> trackList = new ArrayList<>();
    HashMap<Stock,String> stockAndLimit = new HashMap<>();
    int timeInterval;




    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        stockAndLimit = (HashMap<Stock, String>) intent.getSerializableExtra("newStocks");
        timeInterval = Integer.parseInt(intent.getStringExtra("interval"));

        Log.d(TAG, "onStartCommand: " + stockAndLimit);

        Intent intent1 = new Intent(this,MainActivity.class);

        pendingIntent = PendingIntent.getActivity(this,
                0,intent1,0);


        TimerTask getUpdates = new TimerTask() {
            @Override
            public void run() {

                for(Stock stock:stockAndLimit.keySet()){
                    updateStock(stock,stockAndLimit.get(stock));
                }



            }
        };
        timer.schedule(getUpdates,0,timeInterval*60*1000);

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText("Tracking stocks")
                .setSmallIcon(R.drawable.ic_monetization_on_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(2,notification);

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    public void updateStock(final Stock stock, final String limit){

        final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        ServiceGenerator.getStockAPI().getStock(stock.getSymbol()).enqueue(new Callback<StockResponse>() {
            @Override
            public void onResponse(Call<StockResponse> call, Response<StockResponse> response) {

                currentStock = response.body().getStock();
                Log.d(TAG, "mSERVICE: " + currentStock);
                if(currentStock.getLatestPrice() > Double.valueOf(limit)) {

                    final Notification notification2 = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                            .setContentTitle(stock.getCompanyName() + " Exceeded the limit")
                            .setContentText(String.valueOf("Current price "+currentStock.getLatestPrice()))
                            .setSmallIcon(R.drawable.ic_monetization_on_black_24dp)
                            .setContentIntent(pendingIntent)
                            .build();

                    notificationManager.notify(122, notification2);
                }

            }

            @Override
            public void onFailure(Call<StockResponse> call, Throwable t) {
                Log.d("mSERVICE", "onFailure: " + t.getMessage());
            }
        });


    }

}
