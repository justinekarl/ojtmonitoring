package com.example.ojtmonitoring;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Map;

public class CreateSectionActivity extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    ListView showCompaniesLstView;
    public ArrayList<CompanyInfo> companyInfos ;
    private ProgressDialog pDialog;
    private int teacherId;
    public  ArrayList<HashMap<String,String>> companyLists = new ArrayList<HashMap<String,String>>();
    CustomCompanyListView companyListAdapter;
    Button createSectionBtn;
    //Map<Integer,String> selectedCompanies = new HashMap<Integer,String>();
    Map<String,List<Map<Integer,String>>> selectedCompanies = new HashMap<String,List<Map<Integer,String>>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_section);

        showCompaniesLstView = (ListView)findViewById(R.id.showCompaniesLstView);
        createSectionBtn = (Button)findViewById(R.id.createSectionBtn);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        teacherId = sharedPreferences.getInt("agent_id",0);

        ConnectToDataBaseViaJson connectToDataBaseViaJson = new ConnectToDataBaseViaJson();
        connectToDataBaseViaJson.execute();

        createSectionBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedCompanies.clear();
                        if(null != companyInfos && companyInfos.size() > 0){
                            Map<Integer,String> integerStringMap  = new HashMap<Integer, String>();
                            List<Map<Integer,String>> mapList = new ArrayList<Map<Integer, String>>();
                            for(final CompanyInfo companyInfo:companyInfos){
                                if(companyInfo.getSelected() == 1 && !integerStringMap.containsKey(companyInfo.getId())){
                                    integerStringMap.put(companyInfo.getId(),companyInfo.getName());
                                    mapList.add(integerStringMap);
                                }
                            }

                            if(mapList.size() == 0){
                                toastMessage("Nothing selected.");
                                return;
                            }

                            selectedCompanies.put("selectedCompanies",mapList);

                        }

                        if(null != selectedCompanies && selectedCompanies.size() > 0){

                            final StringBuffer sb = new StringBuffer("");

                            Gson gson = new Gson();
                            Type integerObjectMapType = new TypeToken<Map<String,List<Map<Integer, String>>>>(){}.getType();
                            String json = gson.toJson(selectedCompanies, integerObjectMapType);
                            sb.append(json);

                            SharedPreferences preferences =getSharedPreferences(PaceSettingManager.USER_PREFERENCES,MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("selectedCompaniesCreateSection",sb.toString());
                            editor.commit();

                            Intent sectionCreation = new Intent(CreateSectionActivity.this,CreateSectionCompanyActivity.class);
                            startActivity(sectionCreation);
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
            pDialog = new ProgressDialog(CreateSectionActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ConnectToDataBaseViaJson() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentid",teacherId+""));


            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"retrieveAllCompanyListSection.php",
                    "POST", params);


            try {
                if(null != json){
                    companyLists.clear();
                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    int success = json.getInt("success");
                    if(success == 1) {

                         JSONArray items = json.getJSONArray("data_needed");

                        companyInfos = new ArrayList<CompanyInfo>();


                        for(int ctr = 0;  ctr < items.length() ; ctr++){
                            HashMap<String,String> map = new HashMap<String,String>();
                            CompanyInfo companyInfo = new CompanyInfo();

                            if(null != items.getJSONArray(ctr) && (items.getJSONArray(ctr).get(0)+"").contains("~")) {
                                String id = ((items.getJSONArray(ctr).get(0)+"").split("~"))[1];
                                companyInfo.setId(Integer.parseInt(id));
                            }

                            for(int i = 1 ; i <= items.getJSONArray(ctr).length()-1 ; i++) {
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
            companyListAdapter = new CustomCompanyListView(companyInfos,CreateSectionActivity.this);
            showCompaniesLstView.setAdapter(companyListAdapter);
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
