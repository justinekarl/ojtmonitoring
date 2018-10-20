package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ojtmonitoring.info.UserInfo;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewCompaniesActivity extends AppCompatActivity{

    private ListView companyLstView;
    private int agentId;
    private String userName;
    private String college;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    private List<Pair<Integer,String>> companyList = new ArrayList<>();
    private List<UserInfo> companyInfoList = new ArrayList<>();
    ArrayAdapter<String> menuAdapter = null;
    CustomUserItemListView userItemAdapter;
    private Pair userNamePair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_companies);
        PaceSettingManager.lockActivityOrientation(this);
        companyLstView = (ListView)findViewById(R.id.companyLstView);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        agentId = sharedPreferences.getInt("agent_id",0);
        userName = sharedPreferences.getString("user_name","");
        college = sharedPreferences.getString("college","");

        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();

        companyLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //commented out socketio implementation
                //Intent messageCompany = new Intent(getApplicationContext(),MessageTeacherActivity.class);
                Intent messageCompany = new Intent(getApplicationContext(),ChatActivity.class);
                if(null != userItemAdapter && null != userItemAdapter.getUserInfoLists() && userItemAdapter.getUserInfoLists().size() > 0) {

                    UserInfo userInfo =  userItemAdapter.getUserInfoLists().get(position);

                    messageCompany.putExtra("receiverId", userInfo.getId());
                    messageCompany.putExtra("userName",userInfo.getUserName());


                }


                startActivity(messageCompany);
                finish();
            }
        });

    }


    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewCompaniesActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            //   pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid",agentId+""));
            params.add(new BasicNameValuePair("college",college));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"getCompaniesByCollege.php",
                    "POST", params);


            try {
                if(null != json){

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {
                        if(json.has("company_lists") && null != json.getJSONArray("company_lists")){

                            companyList.clear();
                            companyInfoList.clear();


                            for (int i=0;i<json.getJSONArray("company_lists").length() ; i++){
                                final StringBuffer sbName= new StringBuffer("");
                                int id = 0;

                                UserInfo companyInfo = new UserInfo(3);

                                for (int k = 0; k < json.getJSONArray("company_lists").getJSONArray(i).length() ; k++) {



                                    if(k==0) {
                                        companyInfo.setId(Integer.valueOf(json.getJSONArray("company_lists").getJSONArray(i).getInt(k) + ""));
                                    }
                                    if(k==1) {
                                        if(null != json.getJSONArray("company_lists").getJSONArray(i) && null != json.getJSONArray("company_lists").getJSONArray(i).get(k)) {
                                            companyInfo.setName(json.getJSONArray("company_lists").getJSONArray(i).get(k) + "");
                                        }
                                    }

                                    if(k==2) {
                                        if(null != json.getJSONArray("company_lists").getJSONArray(i) && null != json.getJSONArray("company_lists").getJSONArray(i).get(k)) {
                                            companyInfo.setUserName(json.getJSONArray("company_lists").getJSONArray(i).get(k) + "");
                                        }
                                    }

                                    if(k==3) {
                                        if(null != json.getJSONArray("company_lists").getJSONArray(i) && null != json.getJSONArray("company_lists").getJSONArray(i).get(k)) {
                                            companyInfo.setPhone(json.getJSONArray("company_lists").getJSONArray(i).get(k) + "");
                                        }
                                    }

                                    if(k==4) {
                                        if(null != json.getJSONArray("company_lists").getJSONArray(i) && null != json.getJSONArray("company_lists").getJSONArray(i).get(k)) {
                                            companyInfo.setAddress(json.getJSONArray("company_lists").getJSONArray(i).get(k) + "");
                                        }
                                    }

                                    if(k==5) {
                                        if(null != json.getJSONArray("company_lists").getJSONArray(i) && null != json.getJSONArray("company_lists").getJSONArray(i).get(k)) {
                                            companyInfo.setOnline( (json.getJSONArray("company_lists").getJSONArray(i).get(k) + "").equals("1") ? true : false);
                                        }
                                    }


                                }

                                Pair<Integer,String> teacherVals= new Pair<>(id,sbName.toString());

                                companyList.add(teacherVals);
                                companyInfoList.add(companyInfo);
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

            userItemAdapter = new CustomUserItemListView(companyInfoList,ViewCompaniesActivity.this);

            companyLstView.setAdapter(userItemAdapter);


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(this,TeacherLoginActivity.class);
        startActivity(back);
        finish();
    }
}
