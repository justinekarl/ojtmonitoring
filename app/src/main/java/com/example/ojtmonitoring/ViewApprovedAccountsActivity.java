package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.jomer.filetracker.R;
import com.example.ojtmonitoring.info.UserAccountInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewApprovedAccountsActivity extends AppCompatActivity {
    ListView approvedListView;
    Button backBtn;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private List<UserAccountInfo> userAccountInfos;
    private int teacherId;
    private String college;


    String[] acceptedList;
    ListAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_approved_accounts);
        approvedListView = (ListView)findViewById(R.id.approvedListView);
        backBtn = (Button)findViewById(R.id.backBtn);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        teacherId = sharedPreferences.getInt("agent_id",0);
        college = sharedPreferences.getString("college","");

        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();

        backBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent backToPrev = new Intent(ViewApprovedAccountsActivity.this,NewStudentAccountsActivity.class);
                        startActivity(backToPrev);
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backToPrev = new Intent(ViewApprovedAccountsActivity.this,NewStudentAccountsActivity.class);
        startActivity(backToPrev);
    }

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewApprovedAccountsActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //   pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid",teacherId+""));
            params.add(new BasicNameValuePair("college",college));

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveApprovedStudentAccounts.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("accepted_accounts")){

                            userAccountInfos = new ArrayList<UserAccountInfo>();
                            JSONArray studentListArr =json.getJSONArray("accepted_accounts");
                            if(null != studentListArr){
                                acceptedList = new String[studentListArr.length()];
                                for(int i =0 ; i<=studentListArr.length() ; i++){
                                    UserAccountInfo userAccountInfo = new UserAccountInfo();
                                    for (int k = 0; k <= studentListArr.getJSONArray(i).length() - 1; k++) {
                                        String[] row = null;
                                        if(null != studentListArr.getJSONArray(i) && (studentListArr.getJSONArray(i).get(i) + "").contains("~")) {
                                            row = (studentListArr.getJSONArray(i).get(k) + "").split("~");
                                            if (null != row && row.length > 0) {
                                                String key = "";
                                                String value = "";
                                                key = row[0];
                                                if(row.length > 1) {
                                                    value = row[1];
                                                }

                                                if(key.equals("student_id")){
                                                    userAccountInfo.setId(Integer.parseInt(value));
                                                }
                                                if(key.equals("name")){
                                                    userAccountInfo.setName(value);
                                                }

                                                if(key.equals("college")){
                                                    userAccountInfo.setCollege(value);
                                                }

                                                if(key.equals("department")){
                                                    userAccountInfo.setDepartment(value);
                                                }

                                                if(key.equals("username")){
                                                    userAccountInfo.setUserName(value);
                                                }

                                                if(key.equals("accounttype")){
                                                    userAccountInfo.setAccountType(Integer.valueOf(value));
                                                }

                                                if(key.equals("accepted")){
                                                    userAccountInfo.setApproved(Boolean.valueOf(value));
                                                }

                                            }

                                        }
                                    }
                                    userAccountInfos.add(userAccountInfo);
                                    final StringBuffer sb = new StringBuffer("");
                                    sb.append(" Student Name : " +userAccountInfo.getName());
                                    sb.append("\n");
                                    sb.append(" College : " +userAccountInfo.getCollege());
                                    sb.append("\n");
                                    sb.append(" Username : " +userAccountInfo.getUserName());

                                    acceptedList[i] = sb.toString();

                                }

                            }
                        }

                    }

                }else{
                    //loginMessage="Invalid User";
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //loginMessage="Invalid User";
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(null != acceptedList && acceptedList.length > 0) {
                menuAdapter = new ArrayAdapter<String>(ViewApprovedAccountsActivity.this, android.R.layout.simple_list_item_1, acceptedList);
                approvedListView.setAdapter(menuAdapter);
            }

        }
    }
}
