package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllLogsUser extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private ProgressDialog pDialog;
    private Button search;
    private Button showAll;
    public static String logAction;
    JSONParser jsonParser = new JSONParser();

    RelativeLayout relativeMain;
    int flag=0;
    ArrayList <Integer>checkedLogs = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_logs_user);

        search = (Button) findViewById(R.id.search);
        showAll = (Button) findViewById(R.id.showAll);

        //

        relativeMain = (RelativeLayout) findViewById(R.id.activity_all_logs);
        LinearLayout checkboxLayout = (LinearLayout) findViewById(R.id.chkboxlyt);

        int ctr=1;

        //eliminate double
        ArrayList<Map<String, String>> finalData = new ArrayList<>();
        finalData.clear();
        finalData.addAll(admin.transactionLogsData);
        //eliminate double
        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        for (Map<String, String> transaction : finalData) {
          //  allCheckedLogs.add(Integer.valueOf(transaction.get("id")));
            CheckBox cb = new CheckBox(this);
            cb.setText(transaction.get("value")+"                                                      ");
            cb.setId(Integer.valueOf(transaction.get("id")));
            cb.setTextSize(12);
            cb.setTextColor(Color.rgb(0, 0, 0));
            cb.setTypeface(Typeface.MONOSPACE);
            cb.setButtonDrawable(transparentDrawable);
            if((ctr%2) == 0){
                cb.setBackgroundColor(getResources().getColor(R.color.divider));
            }
            checkboxLayout.addView(cb);
            cb.setOnCheckedChangeListener(this);
            ctr++;
        }

        search.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaceSettingManager.searchAction = "searchLogs";
                Intent search = new Intent(AllLogsUser.this, SearchUser.class);
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

    }

    @Override
    public void onBackPressed() {
        Intent content = new Intent(AllLogsUser.this, content.class);
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
            pDialog = new ProgressDialog(AllLogsUser.this);
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
            params.add(new BasicNameValuePair("all_log", "true"));
            params.add(new BasicNameValuePair("agent_id", content.agentId+""));
            json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"get_all_logs.php",
                    "POST", params);

            // check log cat fro responsechar
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
                Intent int1= new Intent(AllLogsUser.this,AllLogsUser.class);
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
