package com.example.ojtmonitoring;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.atomic.AtomicBoolean;

public class CompanyTransactionLogBackgroundService extends Service {
    private static final String TAG = "DEBUGGING";
    private Thread backGround = null;
    private AtomicBoolean running = new AtomicBoolean(false);

    int companyId;
    String entityType;

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        // Toast.makeText(this, " onStart ", Toast.LENGTH_LONG).show();
        //Log.i(TAG,"onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        companyId = 0;
        Bundle extras = intent.getExtras();
        if(extras == null) {
            Log.d("Service", "null");
        }else {
            Log.d("Service","not null");

            if(null != extras.get("companyId")){
                companyId = (int) extras.get("companyId");
            }

            entityType = (String)extras.get("entityType");

        }


        running.set(true);
        Runnable r = new Runnable() {
            @Override
            public void run() {

                if(companyId > 0 && entityType.equals("Company")){
                    RequestQueue requestQueue1 = Volley.newRequestQueue(getBaseContext());
                    while (running.get()){
                        long futureTime = System.currentTimeMillis() + 1000;
                        while (System.currentTimeMillis() < futureTime) {
                            synchronized (this) {
                                try {
                                    wait(futureTime - System.currentTimeMillis());
                                    String url = PaceSettingManager.IP_ADDRESS + "getTransactionLogs/" + companyId;
                                    MakeHttpRequest.getCompanyTransactionLog(getBaseContext(), url, requestQueue1,entityType);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

            }
        };
        backGround = new Thread(r);
        backGround.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        running.set(false);
        backGround.interrupt();
        //backGround.stop();
        //Log.i(TAG,"should stop the thread");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
