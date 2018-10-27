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

/**
 * Created by jj on 10/21/18.
 */

public class TeacherTransactionLogBackgroundService extends Service {
    private static final String TAG = "DEBUGGING";
    private Thread backGround = null;
    private AtomicBoolean running = new AtomicBoolean(false);

    int teacherId;
    String entityType;

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        // Toast.makeText(this, " onStart ", Toast.LENGTH_LONG).show();
        //Log.i(TAG,"onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        teacherId = 0;
        Bundle extras = null;
        if(null != intent) {
            extras = intent.getExtras();
        }
        if(extras == null) {
            Log.d("Service", "null");
        }else {
            Log.d("Service","not null");

            if(null != extras.get("teacherId")){
                teacherId = (int) extras.get("teacherId");
            }

            entityType = (String)extras.get("entityType");

        }


        running.set(true);
        Runnable r = new Runnable() {
            @Override
            public void run() {

                if(teacherId > 0 && entityType.equals("Teacher")){
                    RequestQueue requestQueue1 = Volley.newRequestQueue(getBaseContext());
                    while (running.get()){
                        long futureTime = System.currentTimeMillis() + 1000;
                        while (System.currentTimeMillis() < futureTime) {
                            synchronized (this) {
                                try {
                                    wait(futureTime - System.currentTimeMillis());
                                    String url = PaceSettingManager.IP_ADDRESS + "getTransactionLogs/" + teacherId;
                                    MakeHttpRequest.getTeacherTransactionLog(getBaseContext(), url, requestQueue1,entityType);

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
