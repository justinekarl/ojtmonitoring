package com.example.jomer.filetracker;

import java.util.ArrayList;

/**
 * Created by User on 3/26/2017.
 * this will house all static values
 */

public class PaceSettingManager {
//for xammp
      public static final String IP_ADDRESS = "http://192.168.1.11/filetracker/";

   //   public static final String IP_ADDRESS = "http://192.168.1.12/filetracker/filetracker/";

    //for lamp
   //public static final String IP_ADDRESS = "https://spcffiletrackersystem.000webhostapp.com/filetracker/";

    public static final String USER_PREFERENCES = "userPreferences";

    public static String searchAction;
    public static String searchKeyword;
//
//

    public static String integerTooCommaSeparated(ArrayList<Integer> o){
        String returnValue="";
        if(null != o){
            returnValue = android.text.TextUtils.join(",",o);
        }return returnValue;
    }



}