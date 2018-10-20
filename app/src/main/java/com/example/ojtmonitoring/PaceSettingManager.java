package com.example.ojtmonitoring;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.Display;
import android.view.Surface;
import android.widget.Toast;

import java.util.List;

/**
 * Created by User on 3/26/2017.
 * this will house all static values
 */

public class PaceSettingManager {
//for xammp
        //public static final String IP_ADDRESS = "http://10.10.10.191/ojtmonitoring/";
        //public static final String IP_ADDRESS = "http://192.168.0.16/ojtmonitoring/";

    //sabahay
      /* public static final String IP_ADDRESS = "http://192.168.0.16/ojtmonitoring/";
       public static final String CHAT_SERVER_ADDRESS = "http://192.168.0.16:3000";*/

    /*public static final String IP_ADDRESS = "http://192.168.0.7/ojtmonitoring/";
    public static final String CHAT_SERVER_ADDRESS = "http://192.168.0.7:3000";*/


       /*public static final String IP_ADDRESS = "http://10.42.0.1/ojtmonitoring/";
       public static final String CHAT_SERVER_ADDRESS = "http://10.42.0.1:3000";*/


      // public static final String IP_ADDRESS = "https://ojtapplication.000webhostapp.com/ojtmonitoring/";
       // public static final String IP_ADDRESS = "http://192.168.43.53/ojtmonitoring/";

    //AWS SERVER
      //public static final String IP_ADDRESS = "http://18.191.44.167/ojtmonitoring/";
      public static final String IP_ADDRESS = "http://192.168.22.7/ojtmonitoring/";

      public static final String CHAT_SERVER_ADDRESS = "http://18.191.44.167:3000";

    //for droid
   //public static final String IP_ADDRESS = "https://spcffiletrackersystem.000webhostapp.com/filetracker/";

    public static final String USER_PREFERENCES = "userPreferences";

    public static String searchAction;
    public static String searchKeyword;
//
//

    public static String integerTooCommaSeparated(List<Integer> o){
        String returnValue="";
        if(null != o){
            returnValue = android.text.TextUtils.join(",",o);
        }return returnValue;
    }


    @SuppressWarnings("deprecation")
    public static void lockActivityOrientation(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int rotation = display.getRotation();
        int height;
        int width;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            height = display.getHeight();
            width = display.getWidth();
        } else {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
            width = size.x;
        }
        switch (rotation) {
            case Surface.ROTATION_90:
                if (width > height)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                else
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                break;
            case Surface.ROTATION_180:
                if (height > width)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                else
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;
            case Surface.ROTATION_270:
                if (width > height)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                else
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            default :
                if (height > width)
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                else
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }

    public static void sendNotification(Context context, String message, String sender,int senderId,int receiverId) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);

        Intent notifyIntent = new Intent(context.getApplicationContext(), ChatActivity.class);
        notifyIntent.putExtra("receiverId",receiverId);


        // Set the Activity to start in a new, empty task
                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                context.getApplicationContext(), 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        );


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Message from "+ sender)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        mBuilder.setContentIntent(notifyPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());


    }

    public static void createStudentLogNotification(Context context, String message, boolean login){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(400);

        String displayText = "";

        if(login){
            displayText = "Successfully Logged in.";
        }else{
            displayText = "Successfully Logged out.";
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(displayText)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());

    }

    private boolean isServiceRunning(Context context) {
        /*    ActivityManager manager = (ActivityManager) context.getSystemService();
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
                if("com.example.ojtmonitoring.BackgroundProcessService".equals(service.service.getClassName())) {
                    return true;
            }
        }*/
        return false;
    }



}