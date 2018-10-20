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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateSectionCompanyActivity extends AppCompatActivity {
    String selectedCompaniesStr;
    TextView companyNamesSelTxt;
    TextView secNameTxt;
    TextView noOfStudTxt;
    Button saveBtn;
    Button cancelBtn;
    Map<Integer,String> selCompany = new HashMap<Integer,String>();
    List<Integer> selectedCompanyIds = new ArrayList<Integer>();

    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;

    private int teacherId;
    private String sectionName;
    private int noOfStud;
    boolean successfullySaved;
    boolean duplicateSection;

    Map<String,List<Map<Integer, String>>> selectedCompaniesListMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_section_company);
        companyNamesSelTxt = (TextView)findViewById(R.id.companyNamesSelTxt);
        secNameTxt = (TextView)findViewById(R.id.secNameTxt);
        noOfStudTxt = (TextView)findViewById(R.id.noOfStudTxt);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);

        SharedPreferences sharedPreferences = getSharedPreferences(PaceSettingManager.USER_PREFERENCES, MODE_PRIVATE);
        selectedCompaniesStr = sharedPreferences.getString("selectedCompaniesCreateSection","");
        teacherId = sharedPreferences.getInt("agent_id",0);

        try {

                Gson gson = new Gson();
                Type integerObjectMapType = new TypeToken<Map<String,List<Map<Integer, String>>>>(){}.getType();
                selectedCompaniesListMap = gson.fromJson(selectedCompaniesStr, integerObjectMapType);

                StringBuffer sb = new StringBuffer("");

                if(null != selectedCompaniesListMap && selectedCompaniesListMap.size() > 0){
                    for(Map.Entry<String,List<Map<Integer, String>>> stringListMap : selectedCompaniesListMap.entrySet()){
                        if(null != stringListMap && stringListMap.getKey().equals("selectedCompanies")){
                            if(null != stringListMap.getValue().get(0)){//List<Map<Integer, String>
                                for(Map.Entry<Integer, String> integerStringMap : stringListMap.getValue().get(0).entrySet()){
                                    sb.append(integerStringMap.getValue());
                                    sb.append("\n");

                                    if(null != integerStringMap.getKey()){
                                        selectedCompanyIds.add(integerStringMap.getKey());
                                    }

                                }
                            }
                        }
                    }
                }

            companyNamesSelTxt.setText(sb.toString());

        }catch (Exception e){
            e.printStackTrace();
        }



        cancelBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent backToPrevious = new Intent(CreateSectionCompanyActivity.this,CreateSectionActivity.class);
                        startActivity(backToPrevious);
                    }
                }
        );

        saveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null != secNameTxt.getText()) {
                            sectionName = secNameTxt.getText().toString();
                        }

                        if(null != noOfStudTxt.getText()){
                            noOfStud = Integer.parseInt(noOfStudTxt.getText().toString());
                        }

                        ProcessCompanies processCompanies = new ProcessCompanies();
                        processCompanies.execute();
                    }
                }
        );
    }




    class ProcessCompanies extends AsyncTask<String, String, String> { 


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CreateSectionCompanyActivity.this);
            pDialog.setMessage("Processing..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        public ProcessCompanies() {
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("agentId",teacherId+""));
            params.add(new BasicNameValuePair("sectionName",sectionName));
            params.add(new BasicNameValuePair("noOfStudents",noOfStud+""));
            if(null != selectedCompanyIds && selectedCompanyIds.size() > 0){
                params.add(new BasicNameValuePair("selectedCompanyIds",PaceSettingManager.integerTooCommaSeparated(selectedCompanyIds)+""));
            }

            JSONObject json = jsonParser.makeHttpRequest(PaceSettingManager.IP_ADDRESS+"save_section.php",
                    "POST", params);


            try {
                if(null != json){
                    duplicateSection = false;

                    // check log cat fro response
                    Log.d("Create Response", json.toString());

                    if(json.has("duplicate")){
                        duplicateSection = true;
                    }

                    int success = json.getInt("success");
                    if(success == 1) {
                        successfullySaved = true;
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
         * After completing background_light task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(successfullySaved){

                if(duplicateSection){
                    toastMessage("Section Name already used!");
                    return;
                }

                Intent viewApprovedAccts = new Intent(CreateSectionCompanyActivity.this,TeacherLoginActivity.class);
                startActivity(viewApprovedAccts);
            }else{
                toastMessage("An Error Occurred!");
            }

        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
