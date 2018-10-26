package com.example.ojtmonitoring;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.atomic.AtomicBoolean;
public class BackgroundProcessService extends Service {

    private static final String TAG = "DEBUGGING";
    private Thread backGround = null;
    private AtomicBoolean running = new AtomicBoolean(false);

    int studentId;

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        // Toast.makeText(this, " onStart ", Toast.LENGTH_LONG).show();
        //Log.i(TAG,"onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Log.i("LocalService", "Received start id " + startId + ": " + intent);
        //Toast.makeText(this, " onStartCommand ", Toast.LENGTH_LONG).show();
        //Log.i(TAG,"onStartCommand");
        //super.onStartCommand(intent,flags,startId);

        Bundle extras = intent.getExtras();
        if(extras == null) {
            Log.d("Service", "null");
        }else {
            Log.d("Service","not null");
            studentId = (int) extras.get("studentId");

        }


        running.set(true);
        Runnable r = new Runnable() {
            @Override
            public void run() {

                if(studentId == 0) {

                    RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());

                    while (running.get()) {
                        long futureTime = System.currentTimeMillis() + 1000;
                        while (System.currentTimeMillis() < futureTime) {
                            synchronized (this) {
                                try {
                                    wait(futureTime - System.currentTimeMillis());
                                    SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
                                    int senderId;
                                    senderId = sharedPreferences.getInt("agent_id", 0);

                                    if (senderId != 0) {
                                        Log.i(TAG, "get!");
                                        //ojtmonitoring/getLatestMessage
                                        String url = PaceSettingManager.IP_ADDRESS + "getLatestMessage/" + senderId;
                                        MakeHttpRequest.getBackGround(getBaseContext(), url, requestQueue);


                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                if(studentId > 0) {

                    RequestQueue requestQueue1 = Volley.newRequestQueue(getBaseContext());
                    while (running.get()) {
                        long futureTime = System.currentTimeMillis() + 1000;
                        while (System.currentTimeMillis() < futureTime) {
                            synchronized (this) {
                                try {
                                    wait(futureTime - System.currentTimeMillis());
                                    String url = PaceSettingManager.IP_ADDRESS + "getLatestStudentLog/" + studentId;
                                    MakeHttpRequest.getStudentLog(getBaseContext(), url, requestQueue1);

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



    /*@Overridetry {
            Log.i(TAG,"working");
            Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.

    }*/
}
