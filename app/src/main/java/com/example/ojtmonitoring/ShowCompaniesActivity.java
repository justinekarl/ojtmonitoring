package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jomer.filetracker.R;
import com.example.ojtmonitoring.info.CompanyInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowCompaniesActivity extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private ListView companyList;

    private static int studentId;
    private Button sendResumeBtn;
    private TextView selectedCompanyCountTxt;

    List<Integer> selectedCompanyIds = new ArrayList<Integer>();

    public static ArrayList<HashMap<String,String>> companyLists = new ArrayList<HashMap<String,String>>();
    public static ArrayList<CompanyInfo> companyInfos ;

    private int selectCompanyCount;
    private static boolean hasResume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_companies);

        sendResumeBtn = (Button)findViewById(R.id.sendResumeBtn);

        companyList = (ListView)findViewById(R.id.companyList);

        selectedCompanyCountTxt = (TextView)findViewById(R.id.selectedCompanyCountTxt);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        studentId = sharedPreferences.getInt("agent_id",0);

        ShowCompaniesActivity.ConnectToDataBaseViaJson  showCompaniesAll = new ShowCompaniesActivity.ConnectToDataBaseViaJson();
        showCompaniesAll.execute();

        companyList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        );

        sendResumeBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        selectedCompanyIds.clear();
                        if(null != companyList && null != ((CustomCompanyListView)companyList.getAdapter()).companyInfos) {
                            if (((CustomCompanyListView)companyList.getAdapter()).companyInfos.size() > 0) {
                                for (final CompanyInfo companyInfo : ((CustomCompanyListView)companyList.getAdapter()).companyInfos) {
                                    if (companyInfo.getSelected() == 1) {
                                        selectedCompanyIds.add(companyInfo.getId());
                                    }
                                }
                            }

                            if (null != selectedCompanyIds && selectedCompanyIds.size() > 0) {
                                ProcessCompanies processCompanies = new ProcessCompanies();
                                processCompanies.execute();
                            } else {
                                Toast.makeText(ShowCompaniesActivity.this, "No Company Selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );


    }




    //connecting to the database

    class ConnectToDataBaseViaJson extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowCompaniesActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid",studentId+""));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveAllCompanyList.php",
                    "POST", params);


            try {
                if(null != json){
                    companyLists.clear();
                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                     if(success == 1) {


                         if(json.has("selected_company_count")){
                             selectCompanyCount = json.getInt("selected_company_count");
                         }

                         if(json.has("has_resume")){
                             hasResume = json.getInt("has_resume") > 0 ? true : false;
                         }

                         JSONArray items = json.getJSONArray("data_needed");

                        companyInfos = new ArrayList<CompanyInfo>();


                         for(int ctr = 0;  ctr < items.length() ; ctr++){
                            HashMap<String,String>map = new HashMap<String,String>();
                            CompanyInfo companyInfo = new CompanyInfo();

                            if(null != items.getJSONArray(ctr) && (items.getJSONArray(ctr).get(0)+"").contains("~")) {
                                String id = ((items.getJSONArray(ctr).get(0)+"").split("~"))[1];
                                companyInfo.setId(Integer.parseInt(id));
                            }

                            for(int i = 1 ; i < items.getJSONArray(ctr).length()-1 ; i++) {
                                String[] row = null;
                                if(null != items.getJSONArray(ctr) && (items.getJSONArray(ctr).get(i) + "").contains("~")) {
                                    row = (items.getJSONArray(ctr).get(i) + "").split("~");
                                    if (null != row && row.length > 0) {
                                        String key = "";
                                        String value = "";
                                        key = row[0];
                                        if(row.length > 1) {
                                            value = row[1];
                                        }

                                        if(key.equals("company_name")){
                                            companyInfo.setName(value);
                                        }

                                        if(key.equals("address")){
                                           companyInfo.setAddress(value);
                                        }

                                        if(key.equals("phone_number")){
                                            companyInfo.setPhoneNumber(value);
                                        }
                                        if(key.equals("email")){
                                            companyInfo.setEmailAddress(value);
                                        }
                                        if(key.equals("description")){
                                            companyInfo.setDescription(value);
                                        }



                                    }
                                }
                            }

                    companyInfos.add(companyInfo);
                    }

                    }else {
                        if(null != json.getString("message")){
                            //   loginMessage=json.getString("message");
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
            CustomCompanyListView companyListAdapter = new CustomCompanyListView(companyInfos,ShowCompaniesActivity.this);
            companyList.setAdapter(companyListAdapter);
            selectedCompanyCountTxt.setText(" Number of selected companies: " +selectCompanyCount+"");
            if(null != companyInfos && companyInfos.size() == 0){
                sendResumeBtn.setClickable(false);
            }

            if(!hasResume){
                sendResumeBtn.setVisibility(View.INVISIBLE);
            }

        }
    }
    //end of connecting


    class ProcessCompanies extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ShowCompaniesActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ProcessCompanies() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",studentId+""));
            if(null != selectedCompanyIds && selectedCompanyIds.size() > 0){
                params.add(new BasicNameValuePair("selectedCompanyIds",PaceSettingManager.integerTooCommaSeparated(selectedCompanyIds)+""));
            }


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"save_selected_companies.php",
                    "POST", params);


            try {
                if(null != json){
                    companyLists.clear();
                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {

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
            ShowCompaniesActivity.ConnectToDataBaseViaJson  showCompaniesAll = new ShowCompaniesActivity.ConnectToDataBaseViaJson();
            showCompaniesAll.execute();

        }
    }
}
