package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Search extends AppCompatActivity {

    private EditText keyWord;
    private Button goSearch;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
        if(PaceSettingManager.searchAction == "searchLogs"){
            Intent back= new Intent(Search.this,AllLogs.class);
            startActivity(back);
        }else if (PaceSettingManager.searchAction == "searchBorrowed") {
            Intent back= new Intent(Search.this,UserBorrowedItems.class);
            startActivity(back);
        }else if (PaceSettingManager.searchAction == "searchReturned") {
            Intent back= new Intent(Search.this,UserReturnedItem.class);
            startActivity(back);
        }else if (PaceSettingManager.searchAction == "searchUsers" || PaceSettingManager.searchAction == "searchQr"){
            Intent back= new Intent(Search.this,admin.class);
            startActivity(back);
        }
    }

//connecting to the database

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if( PaceSettingManager.searchAction == "searchLogs"){
                admin.transactionLogsData.clear();
            }else if (PaceSettingManager.searchAction == "searchBorrowed" || PaceSettingManager.searchAction == "searchReturned"){
                content.dataNeeded.clear();
            }else if (PaceSettingManager.searchAction == "searchUsers"){
                admin.allUsers.clear();
            }else if (PaceSettingManager.searchAction == "searchQr"){
                admin.allQrCodes.clear();
            }

            pDialog = new ProgressDialog(Search.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("keyword", PaceSettingManager.searchKeyword));
            params.add(new BasicNameValuePair("discriminator", PaceSettingManager.searchAction));
            params.add(new BasicNameValuePair("admin", String.valueOf(Login.adminRights)));
            params.add(new BasicNameValuePair("agent_id", String.valueOf(Login.agentId)));

            if (admin.actionTaken == "allLogsHistory"){
                params.add(new BasicNameValuePair("all_log", "true"));
            }

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"search.php",
                    "POST", params);

            try {
                if(null != json){
                    int success = json.getInt("success");

                    if (success == 1) {
                        if( PaceSettingManager.searchAction == "searchLogs" || admin.actionTaken == "allLogsHistory"){
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
                        }else if (PaceSettingManager.searchAction == "searchBorrowed" || PaceSettingManager.searchAction == "searchReturned") {
                            JSONArray items = json.getJSONArray("data_needed");
                            for(int ctr = 0;  ctr < items.length() ; ctr++){
                                content.dataNeeded.add(items.get(ctr)+"");
                            }
                        }else if (PaceSettingManager.searchAction == "searchUsers"){
                            JSONArray users = json.getJSONArray("data_needed");
                            List<Map<String, String>> usersValuePair = new ArrayList<>();
                            for (int ctr = 0; ctr < users.length();ctr++ ) {
                                Map<String, String> userMap = new HashMap<>();
                                userMap.put("agent_id",users.getJSONArray(ctr).getJSONObject(0).get("id_agent").toString());
                                userMap.put("student_number",users.getJSONArray(ctr).getJSONObject(3).get("student_number").toString());
                                userMap.put("full_name",users.getJSONArray(ctr).getJSONObject(4).get("full_name").toString());
                                userMap.put("not_clear",users.getJSONArray(ctr).getJSONObject(6).get("not_clear").toString());
                                usersValuePair.add(userMap);
                            }

                            for(Map<String, String> user : usersValuePair){
                                StringBuffer returnString = new StringBuffer();
                                returnString.append("Student No: "+user.get("student_number")+" \n");
                                returnString.append("Name: "+user.get("full_name")+" \n");
                                Map<String, String> value = new HashMap<>();
                                value.put("id",user.get("agent_id"));
                                value.put("value",returnString.toString());
                                value.put("not_clear",user.get("not_clear"));
                                admin.allUsers.add(value);
                            }
                        }

                        else if (PaceSettingManager.searchAction == "searchQr"){
                            JSONArray fetchedQrCodes = json.getJSONArray("data_needed");
                            for (int ctr = 0; ctr < fetchedQrCodes.length();ctr++ ) {
                                Map<String, String> qrMap = new HashMap<>();
                                qrMap.put("id_qr_codes",fetchedQrCodes.getJSONArray(ctr).getJSONObject(0).get("id_qr_codes").toString());
                                qrMap.put("item",fetchedQrCodes.getJSONArray(ctr).getJSONObject(1).get("item").toString());
                                admin.allQrCodes.add(qrMap);
                            }
                        }

                    }
                }else{

                }

            } catch (JSONException e) {

            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            if( PaceSettingManager.searchAction == "searchLogs"){
                Intent view= new Intent(Search.this,AllLogs.class);
                startActivity(view);
            }else if (PaceSettingManager.searchAction == "searchBorrowed"){
                Intent view = new Intent(Search.this, UserBorrowedItems.class);
                startActivity(view);
            }else if(PaceSettingManager.searchAction == "searchReturned"){
                Intent view = new Intent(Search.this, UserReturnedItem.class);
                startActivity(view);
            }else if (PaceSettingManager.searchAction == "searchUsers"){
                Intent int1= new Intent(Search.this,AllUsers.class);
                startActivity(int1);
            }else if (PaceSettingManager.searchAction == "searchQr"){
                Intent int1= new Intent(Search.this,AllQrCode.class);
                startActivity(int1);
            }
            pDialog.hide();
        }
    }
//end of connecting



}