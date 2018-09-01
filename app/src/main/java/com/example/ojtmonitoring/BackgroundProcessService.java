package com.example.ojtmonitoring;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;
public class BackgroundProcessService extends Service {

    private static final String TAG = "DEBUGGING";
    private Thread backGround = null;
    private AtomicBoolean running = new AtomicBoolean(false);



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
        running.set(true);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(running.get()){
                    long futureTime = System.currentTimeMillis()+1000;
                    while (System.currentTimeMillis() < futureTime){
                        synchronized (this){
                            try{
                                wait(futureTime - System.currentTimeMillis());
                                SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
                                int senderId;
                                senderId = sharedPreferences.getInt("agent_id",0);

                                if(senderId != 0){
                                    Log.i(TAG,"get!");
                                    //ojtmonitoring/getLatestMessage
                                    String url = PaceSettingManager.IP_ADDRESS+"getLatestMessage/"+senderId;
                                    MakeHttpRequest.getBackGround(getBaseContext(), url);


                                }

                            }catch (Exception e){

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



    /*@Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {
            Log.i(TAG,"working");
            Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
    }*/
}
