package com.example.ojtmonitoring;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.Surface;

import java.util.ArrayList;
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

    /*public static final String IP_ADDRESS = "http://192.168.0.11/ojtmonitoring/";
    public static final String CHAT_SERVER_ADDRESS = "http://192.168.0.11:3000";*/


       /*public static final String IP_ADDRESS = "http://10.42.0.1/ojtmonitoring/";
       public static final String CHAT_SERVER_ADDRESS = "http://10.42.0.1:3000";*/



      // public static final String IP_ADDRESS = "https://ojtapplication.000webhostapp.com/ojtmonitoring/";
       // public static final String IP_ADDRESS = "http://192.168.43.53/ojtmonitoring/";

    //AWS SERVER
    public static final String IP_ADDRESS = "http://18.191.44.167/ojtmonitoring/";

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

}