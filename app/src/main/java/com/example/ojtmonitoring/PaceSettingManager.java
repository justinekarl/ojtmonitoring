package com.example.ojtmonitoring;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/26/2017.
 * this will house all static values
 */

public class PaceSettingManager {
//for xammp
     public static final String IP_ADDRESS = "http://10.10.10.147/ojtmonitoring/";
       //public static final String IP_ADDRESS = "https://ojtapplication.000webhostapp.com/ojtmonitoring/";
    //   public static final String IP_ADDRESS = "http://192.168.1.12/filetracker/filetracker/";

    //for lamp
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



}