package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jomer.filetracker.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllLogs extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private ProgressDialog pDialog;
    private Button clearLogs;
    private Button search;
    private Button showAll;
    private CheckBox checkAll;
    private TextView headerTransactionLogs;
    public static String logAction;
    JSONParser jsonParser = new JSONParser();

    RelativeLayout relativeMain;
    int flag=0;
    ArrayList <Integer>checkedLogs = new ArrayList<>();
    ArrayList <Integer>allCheckedLogs = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_logs);

        //pop confirmation
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete selected logs?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(logAction == "clearLogs"){
                            if(null != checkedLogs && checkedLogs.size() > 0){
                                ConnectToDataBaseViaJson clearLogs = new ConnectToDataBaseViaJson();
                                clearLogs.execute();
                            }
                        }
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*toastMessage("cancel");*/
            }
        });
        // end of confirmation

        clearLogs = (Button) findViewById(R.id.clearLogs);
        search = (Button) findViewById(R.id.search);
        showAll = (Button) findViewById(R.id.showAll);
        checkAll = (CheckBox) findViewById(R.id.checkAll);
        headerTransactionLogs = (TextView) findViewById(R.id.headerTransactionLogs);

        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        if(admin.actionTaken == "allLogsHistory"){
            headerTransactionLogs.setText("History");
            clearLogs.setVisibility(View.INVISIBLE);
            checkAll.setVisibility(View.INVISIBLE);
        }
        //

        relativeMain = (RelativeLayout) findViewById(R.id.activity_all_logs);
        LinearLayout checkboxLayout = (LinearLayout) findViewById(R.id.chkboxlyt);

        int ctr=1;

        //eliminate double
        ArrayList<Map<String, String>> finalData = new ArrayList<>();
        finalData.clear();
        finalData.addAll(admin.transactionLogsData);
        //eliminate double

        for (Map<String, String> transaction : finalData) {
        allCheckedLogs.add(Integer.valueOf(transaction.get("id")));
        CheckBox cb = new CheckBox(this);
        cb.setText(transaction.get("value"));
        cb.setId(Integer.valueOf(transaction.get("id")));
        cb.setTextSize(12);
        cb.setTextColor(Color.rgb(0, 0, 0));
        cb.setTypeface(Typeface.MONOSPACE);
        if(admin.actionTaken == "allLogsHistory") {
            cb.setButtonDrawable(transparentDrawable);
        }
        if((ctr%2) == 0){
            cb.setBackgroundColor(getResources().getColor(R.color.divider));
        }

        checkboxLayout.addView(cb);
        cb.setOnCheckedChangeListener(this);
        ctr++;
        }

        clearLogs.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkedLogs.size() > 0){
                    logAction = "clearLogs";
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    toastMessage("Nothing Selected");
                }
            }
        });

        search.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaceSettingManager.searchAction = "searchLogs";
                Intent search = new Intent(AllLogs.this, Search.class);
                startActivity(search);
            }
        });

        showAll.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logAction = "showAll";
                admin.transactionLogsData.clear();
                ConnectToDataBaseViaJson showAllLogs = new ConnectToDataBaseViaJson();
                showAllLogs.execute();
            }
        });

        checkAll.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                 for (Integer id :  allCheckedLogs){
                     ((CheckBox) findViewById(id)).setChecked(isChecked);
                 }
             }
         }
        );
}

    @Override
    public void onBackPressed() {
            Intent content = new Intent(AllLogs.this, admin.class);
            startActivity(content);
    }


    public void onCheckedChanged(CompoundButton cb, boolean isChecked){
        if(isChecked){
            checkedLogs.add(cb.getId());
        } else {
            ArrayList <Integer>checkedLogsRemove = new ArrayList<>();
            checkedLogsRemove.add(cb.getId());
            checkedLogs.removeAll(checkedLogsRemove);
        }
    }

    public void onSaveInstanceState(Bundle savedState){
        super.onSaveInstanceState(savedState);
        flag=1;
        savedState.putIntegerArrayList("checkedLogs", checkedLogs);
        savedState.putInt("savedflag", flag);
    }






    //connect database

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            admin.transactionLogsData.clear();
            super.onPreExecute();
            pDialog = new ProgressDialog(AllLogs.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", PaceSettingManager.integerTooCommaSeparated(checkedLogs)));
            // getting JSON Object
            // Note that create product url accepts POST method

            JSONObject json = new JSONObject();
            if(logAction == "clearLogs"){
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"clear_log.php",
                        "POST", params);
            }else{
                if (admin.actionTaken == "allLogsHistory"){
                    params.add(new BasicNameValuePair("all_log", "true"));
                }
                json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_all_logs.php",
                        "POST", params);
            }

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt("success");

                if(logAction == "clearLogs"){
                    if (success == 1) {
                        if (success == 1) {
                            JSONArray logs = json.getJSONArray("data_needed");
                            List<Map<String, String>> logValuePair = new ArrayList<>();
                            for (int ctr = 0; ctr < logs.length();ctr++ ) {
                                Map<String, String> logMap = new HashMap<>();
                                logMap.put("id",logs.getJSONArray(ctr).getJSONObject(0).get("id").toString());
                                logMap.put("item",logs.getJSONArray(ctr).getJSONObject(1).get("item").toString());
                                logMap.put("date_created",logs.getJSONArray(ctr).getJSONObject(3).get("date_created").toString());
                                logMap.put("borrowed",logs.getJSONArray(ctr).getJSONObject(4).get("borrowed").toString());
                                logMap.put("returned",logs.getJSONArray(ctr).getJSONObject(5).get("returned").toString());
                                logMap.put("full_name",logs.getJSONArray(ctr).getJSONObject(12).get("full_name").toString());
                                logValuePair.add(logMap);
                            }

                            for(Map<String, String> log : logValuePair){
                                StringBuffer returnString = new StringBuffer();
                                returnString.append("Transaction No: "+log.get("id")+" \n");
                                if(Long.valueOf(log.get("borrowed")) == 1){
                                    returnString.append("Transaction Type: Borrowed \n");
                                }else {
                                    returnString.append("Transaction Type: Returned \n");
                                }
                                returnString.append("Name: "+log.get("full_name")+" \n");
                                returnString.append("Date: "+log.get("date_created")+" \n \n");
                                returnString.append("Item: \n"+log.get("item")+" \n");
                                //content.dataNeeded.add(returnString+"");
                                Map<String, String> value = new HashMap<>();
                                value.put("id",log.get("id"));
                                value.put("value",returnString.toString());
                                admin.transactionLogsData.add(value);
                            }


                        }
                    } else {
                        content.dataNeeded.clear();

                    }
                }else{
                    if (success == 1) {
                        JSONArray logs = json.getJSONArray("data_needed");
                        List<Map<String, String>> logValuePair = new ArrayList<>();
                        for (int ctr = 0; ctr < logs.length();ctr++ ) {
                            Map<String, String> logMap = new HashMap<>();
                            logMap.put("id",logs.getJSONArray(ctr).getJSONObject(0).get("id").toString());
                            logMap.put("item",logs.getJSONArray(ctr).getJSONObject(1).get("item").toString());
                            logMap.put("date_created",logs.getJSONArray(ctr).getJSONObject(3).get("date_created").toString());
                            logMap.put("borrowed",logs.getJSONArray(ctr).getJSONObject(4).get("borrowed").toString());
                            logMap.put("returned",logs.getJSONArray(ctr).getJSONObject(5).get("returned").toString());
                            logMap.put("full_name",logs.getJSONArray(ctr).getJSONObject(12).get("full_name").toString());
                            logValuePair.add(logMap);
                        }

                        for(Map<String, String> log : logValuePair){
                            StringBuffer returnString = new StringBuffer();
                            returnString.append("Transaction No: "+log.get("id")+" \n");
                            if(Long.valueOf(log.get("borrowed")) == 1){
                                returnString.append("Transaction Type: Borrowed \n");
                            }else {
                                returnString.append("Transaction Type: Returned \n");
                            }
                            returnString.append("Name: "+log.get("full_name")+" \n");
                            returnString.append("Date: "+log.get("date_created")+" \n \n");
                            returnString.append("Item: \n"+log.get("item")+" \n");
                            //content.dataNeeded.add(returnString+"");
                            Map<String, String> value = new HashMap<>();
                            value.put("id",log.get("id"));
                            value.put("value",returnString.toString());
                            admin.transactionLogsData.add(value);
                        }


                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            if(logAction == "clearLogs" || logAction == "showAll" ){
                Intent int1= new Intent(AllLogs.this,AllLogs.class);
                startActivity(int1);
            }
            pDialog.hide();
        }
    }

    //connect database

    public void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }




}
