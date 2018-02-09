package com.example.jomer.filetracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SearchUser extends AppCompatActivity {

    private EditText keyWord;
    private Button goSearch;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        keyWord=(EditText)findViewById(R.id.searchKeyWord);
        goSearch = (Button)findViewById(R.id.goSearch);

        goSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v) {
                PaceSettingManager.searchKeyword= keyWord.getText().toString();
                if(null != PaceSettingManager.searchKeyword && PaceSettingManager.searchKeyword.trim().length() == 0){
                    toastMessage("All Fields are required!");
                }else{
                    ConnectToDataBaseViaJson search = new ConnectToDataBaseViaJson();
                    search.execute();
                }
            }
        });

    }
    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent back= new Intent(SearchUser.this,AllLogsUser.class);
        startActivity(back);
    }

//connecting to the database

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            admin.transactionLogsData.clear();
            pDialog = new ProgressDialog(SearchUser.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("keyword", PaceSettingManager.searchKeyword));
            params.add(new BasicNameValuePair("discriminator", "searchLogs"));
            params.add(new BasicNameValuePair("admin", String.valueOf(login.adminRights)));
            params.add(new BasicNameValuePair("agent_id", String.valueOf(login.agentId)));
            params.add(new BasicNameValuePair("all_log", "true"));
            params.add(new BasicNameValuePair("user", "true"));

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"search.php",
                    "POST", params);

            try {
                if(null != json){
                    int success = json.getInt("success");

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

            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            Intent view= new Intent(SearchUser.this,AllLogsUser.class);
            startActivity(view);
            pDialog.hide();
        }
    }
//end of connecting



}